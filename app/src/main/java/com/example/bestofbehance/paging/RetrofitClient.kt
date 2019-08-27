package com.example.bestofbehance.paging

import com.example.bestofbehance.retrofit.BehanceApiInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object {
        private var mInstance: RetrofitClient? = null
        private lateinit var retrofit: Retrofit

        fun getInstance(): RetrofitClient {
            if (mInstance == null) {
                mInstance =
                    RetrofitClient()
            }
            return mInstance as RetrofitClient
        }
    }

    init {
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.behance.net/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getApi(): BehanceApiInterface = retrofit.create(BehanceApiInterface::class.java)
}