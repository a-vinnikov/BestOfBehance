package com.example.bestofbehance.dagger

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import java.util.prefs.Preferences
import javax.inject.Inject

object StorageModule {

        private fun getPreferences(context: Context): SharedPreferences? = context.getSharedPreferences("viewMode", AppCompatActivity.MODE_PRIVATE)

        fun getPreferences(context: Context, destination: String, recipient: String): String{
            return getPreferences(context)?.getString(destination, recipient)!!
        }

        fun editorPreferences(context: Context, destination: String, data: String){
            val editor = getPreferences(context)?.edit()
            editor?.putString(destination, data)
            editor?.apply()
        }
}