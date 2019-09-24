package com.example.bestofbehance.dagger

import android.content.SharedPreferences

class GetSharedPreference(val preference: SharedPreferences?){

    fun getPreferences(destination: String, recipient: String): String{
        return preference?.getString(destination, recipient)!!
    }
}