package com.example.bestofbehance

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.bestofbehance.databases.forRoom.CardDataBase
import com.example.bestofbehance.viewModels.ConnectChecking
import com.example.bestofbehance.viewModels.NaviController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.withTimeout


open class MainActivity : AppCompatActivity() {

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
        setupNavigation()
        ConnectChecking.check(this, snack_coordinator)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}