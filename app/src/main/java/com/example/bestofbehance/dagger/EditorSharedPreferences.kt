package com.example.bestofbehance.dagger

import android.content.SharedPreferences

class EditorSharedPreference(val preference: SharedPreferences?){

    fun edit(destination: String, data: String){
        val editor = preference?.edit()
        editor?.putString(destination, data)
        editor?.apply()
    }
}