package com.example.bestofbehance.module

import android.content.Context
import com.example.bestofbehance.retrofit.BehanceApiInterface
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit
import com.example.bestofbehance.R
import com.example.bestofbehance.classesToSupport.API_KEY
import com.example.bestofbehance.classesToSupport.BASE_URL
import com.example.bestofbehance.classesToSupport.SECOND_KEY
import com.example.bestofbehance.retrofit.BehanceCallAdapter
import com.example.bestofbehance.retrofit.BehanceCallAdapterFactory


class NetworkModule(val context: Context) {

    fun providesBehanceApi(retrofit: Retrofit): BehanceApiInterface =
        retrofit.create(BehanceApiInterface::class.java)

    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(BehanceCallAdapterFactory.create())
            .client(okHttpClient)
            .build()

    fun providesOkHttpClient(): OkHttpClient.Builder {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.HEADERS

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor (object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                lateinit var temp: Response

                temp = try {
                    getResponse(chain, API_KEY)
                } catch (e: IOException){
                    temp.close()
                    getResponse(chain, SECOND_KEY)
                }

                return temp
            }
        })

        httpClient.connectTimeout(5, TimeUnit.MINUTES)
        httpClient.writeTimeout(5, TimeUnit.MINUTES)
        httpClient.readTimeout(5, TimeUnit.MINUTES)

        httpClient.addNetworkInterceptor(logging)

        return httpClient
    }

    fun getResponse(chain: Interceptor.Chain, key: String): Response {
        val original = chain.request()
        val originalHttpUrl = original.url

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter(context.resources.getString(R.string.api_key), key)
            .build()

        val requestBuilder = original.newBuilder().url(url)

        val request = requestBuilder.build()

        val response = chain.proceed(request)

        return response
    }
}