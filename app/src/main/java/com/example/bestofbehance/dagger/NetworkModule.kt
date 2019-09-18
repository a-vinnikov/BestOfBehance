package com.example.bestofbehance.dagger

import android.content.Context
import com.example.bestofbehance.retrofit.BehanceApiInterface
import dagger.Module
import dagger.Provides
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import android.net.ConnectivityManager
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit
import com.example.bestofbehance.R

@Module
class NetworkModule(val context: Context) {

    @Singleton
    @Provides
    fun providesBehanceApi(retrofit: Retrofit): BehanceApiInterface =
        retrofit.create(BehanceApiInterface::class.java)

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(context.resources.getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()


    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient.Builder {
        var api_key: String = context.resources.getString(R.string.firstKey)
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.HEADERS

        val httpClient = OkHttpClient.Builder()
        httpClient.addNetworkInterceptor (object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()
                val originalHttpUrl = original.url

                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", api_key)
                    .build()

                val requestBuilder = original.newBuilder().url(url)

                val request = requestBuilder.build()

                val response = chain.proceed(request)

                if (!response.isSuccessful && response.code == 429) {
                    when (api_key){
                        context.resources.getString(R.string.firstKey) -> api_key = context.resources.getString(R.string.secondKey)
                        context.resources.getString(R.string.secondKey) -> api_key = context.resources.getString(R.string.firstKey)
                    }
                }

                return response
            }
        })

        httpClient.connectTimeout(5, TimeUnit.MINUTES)
        httpClient.writeTimeout(5, TimeUnit.MINUTES)
        httpClient.readTimeout(5, TimeUnit.MINUTES)

        httpClient.addNetworkInterceptor(logging)

        return httpClient
    }

    fun hasNetwork(): Boolean {
        val con = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = con.activeNetworkInfo
        return !(ni == null || !ni.isConnected)
    }
}