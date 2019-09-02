package com.example.bestofbehance

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.bestofbehance.viewModels.ConnectionLiveData
import com.example.bestofbehance.viewModels.NaviController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




open class MainActivity : AppCompatActivity() {

    lateinit var snackBar: Snackbar

    private val appBarConfiguration = AppBarConfiguration
        .Builder(
            R.id.best,
            R.id.projects,
            R.id.people
        )
        .build()

    private fun setupNavigation() {
        NavigationUI.setupActionBarWithNavController(this, NaviController(this).controller, appBarConfiguration)
        navigation.setupWithNavController(NaviController(this).controller)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        snackBar = Snackbar.make(snack_coordinator, "No internet connection", Snackbar.LENGTH_INDEFINITE)
        setupNavigation()

        val connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this, androidx.lifecycle.Observer { isConnected ->
            isConnected?.let {
                    when (it){
                        true -> {snackBar.dismiss()}
                        false -> {
                            val layout = snackBar.view as Snackbar.SnackbarLayout
                            layout.setBackgroundColor(Color.parseColor("#43A6F6"))
                            snackBar.setActionTextColor(Color.parseColor("#FFFFFF"))
                            snackBar.show()}
                    }
                }
        })
        //ConnectChecking.check(this, snack_coordinator)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}