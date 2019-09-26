package com.example.bestofbehance.extension

import java.text.SimpleDateFormat
import java.util.*

object CurrentDate {

    fun Date.toString(locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}