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
import android.widget.Toast
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit
import com.example.bestofbehance.R
import com.example.bestofbehance.classesToSupport.SharedPreferenceObject

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
            .baseUrl(context.resources.getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()


    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient.Builder {
        var api_key = context.resources.getString(R.string.first_key)
        api_key = SharedPreferenceObject.getSharedPreference(context, context.resources.getString(R.string.api_key),api_key)
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.HEADERS

        val httpClient = OkHttpClient.Builder()
        httpClient.addNetworkInterceptor (object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()
                val originalHttpUrl = original.url

                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter(context.resources.getString(R.string.api_key), api_key)
                    .build()

                val requestBuilder = original.newBuilder().url(url)

                val request = requestBuilder.build()

                val response = chain.proceed(request)

                if (!response.isSuccessful && response.code == 429) {
                    Toast.makeText(context, context.resources.getString(R.string.refresh_message), Toast.LENGTH_SHORT).show()
                    when (api_key){
                        context.resources.getString(R.string.first_key) -> SharedPreferenceObject.editorSharedPreference(context, context.resources.getString(R.string.api_key), context.resources.getString(R.string.second_key))
                        context.resources.getString(R.string.second_key) -> SharedPreferenceObject.editorSharedPreference(context, context.resources.getString(R.string.api_key), context.resources.getString(R.string.first_key))
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
        val conManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = conManager.activeNetworkInfo
        return !(ni == null || !ni.isConnected)
    }
}