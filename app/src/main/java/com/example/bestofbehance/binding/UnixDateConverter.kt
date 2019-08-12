package com.example.bestofbehance.binding

import android.annotation.SuppressLint
import androidx.databinding.BindingConversion
import java.text.SimpleDateFormat
import java.util.*


object UnixDateConverter {
    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    fun convert(timestamp: String): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy")
        val netDate = Date(timestamp.toLong() * 1000)
        return sdf.format(netDate)
    }
}