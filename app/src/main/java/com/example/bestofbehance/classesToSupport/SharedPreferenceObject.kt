package com.example.bestofbehance.classesToSupport

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

object SharedPreferenceObject {

    fun getSharedPreference(context: Context, destination: String, recipient: String): String {
        val sharedPreference = context.getSharedPreferences("viewMode", AppCompatActivity.MODE_PRIVATE)
        return sharedPreference!!.getString(destination, recipient)!!
    }

    fun editorSharedPreference(context: Context, destination: String, data: String){
        val editor = context.getSharedPreferences("viewMode", AppCompatActivity.MODE_PRIVATE)?.edit()
        editor?.putString(destination, data)
        editor?.apply()
    }
}