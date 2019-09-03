package com.example.bestofbehance.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit


var API_KEY = "xMrW480v8SrR9J02koQXiIEEMr3uzIfd"

object BehanceOkHtttpClient {

    //xMrW480v8SrR9J02koQXiIEEMr3uzIfd
    //0QmPh684DRz1SpWHDikkyFCzLShGiHPi

    fun okHttpBuilder(): OkHttpClient.Builder {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.HEADERS

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()
                val originalHttpUrl = original.url

                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", API_KEY)
                    .build()

                val requestBuilder = original.newBuilder()
                    .url(url)

                val request = requestBuilder.build()

                val response = chain.proceed(request)

                if (!response.isSuccessful && response.code == 429) {
                    when (API_KEY){
                        "xMrW480v8SrR9J02koQXiIEEMr3uzIfd" -> API_KEY = "0QmPh684DRz1SpWHDikkyFCzLShGiHPi"
                        "0QmPh684DRz1SpWHDikkyFCzLShGiHPi" -> API_KEY = "xMrW480v8SrR9J02koQXiIEEMr3uzIfd"
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


}