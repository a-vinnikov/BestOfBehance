package com.example.bestofbehance.retrofit

import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class NetCall<R>(private val call: Call<R>) {
    fun run(responseHandler: (R?, Throwable?) -> Unit) {
        try {
            val response = call.execute()
            handleResponse(response, responseHandler)

        } catch (t: IOException) {
            responseHandler(null, t)
        }
    }

    fun process(responseHandler: (R?, Throwable?) -> Unit) {
        val callback = object : Callback<R> {

            override fun onFailure(call: Call<R>?, t: Throwable?) =
                responseHandler(null, t)

            override fun onResponse(call: Call<R>?, r: Response<R>?) =
                handleResponse(r, responseHandler)
        }
        call.enqueue(callback)
    }
    private fun handleResponse(response: Response<R>?, handler: (R?, Throwable?) -> Unit) {
        if (response?.isSuccessful == true) {
            handler(response.body(), null)
        } else {
            if (response?.code() in 400..511)
                handler(null, HttpException(response as Response<*>))

            else handler(response?.body(), null)

        }
    }
}