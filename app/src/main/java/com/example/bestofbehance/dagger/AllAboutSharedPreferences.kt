package com.example.bestofbehance.dagger

import android.content.SharedPreferences

class AllAboutSharedPreferences(private val preference: SharedPreferences){

    fun stringEdit(destination: String, data: String) = preference.edit()?.putString(destination, data)?.apply()

    fun stringGet(destination: String, data: String): String = preference.getString(destination, data)!!

}