package com.example.bestofbehance.viewModels

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import com.google.android.material.snackbar.Snackbar

object ConnectChecking {


    fun check(context: Context, v: View): Boolean {
        return if (isOnline(context) == null || isOnline(context) == false){
            Snackbar.make(v, "No internet connection", Snackbar.LENGTH_INDEFINITE).setAction("No internet connection", null).show()
            false
        } else { true }
    }

    private fun isOnline(context: Context): Boolean? {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected
    }
}