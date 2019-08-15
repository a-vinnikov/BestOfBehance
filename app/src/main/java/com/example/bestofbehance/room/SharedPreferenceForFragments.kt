package com.example.bestofbehance.room

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

object SharedPreferenceForFragments {

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