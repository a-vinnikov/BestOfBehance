package com.example.bestofbehance.classesToSupport

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

object SharedPreferenceObject {

    fun sharedCurrentViewMode(context: Context, destination: String, currentViewMode: String): String {
        val sharedPreference = context.getSharedPreferences("viewMode", AppCompatActivity.MODE_PRIVATE)
        return sharedPreference!!.getString(destination, currentViewMode)!!
    }

    fun editorSharedPreference(context: Context, destination: String, mode: String){
        val editor = context.getSharedPreferences("viewMode", AppCompatActivity.MODE_PRIVATE)?.edit()
        editor?.putString(destination, mode)
        editor?.apply()
    }
}