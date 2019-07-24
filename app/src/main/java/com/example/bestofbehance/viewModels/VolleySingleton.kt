package com.example.bestofbehance.viewModels

import android.annotation.SuppressLint
import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley


@SuppressLint("StaticFieldLeak")
object VolleySingleton {
    private lateinit var context: Context

    val requestQueue: RequestQueue by lazy { Volley.newRequestQueue(context) }

    fun initialize(context: Context) {
        this.context = context.applicationContext
    }
}