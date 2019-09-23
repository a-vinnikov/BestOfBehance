package com.example.bestofbehance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.bestofbehance.classesToSupport.ConnectionLiveData
import com.example.bestofbehance.dagger.NaviController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.textView


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
        NavigationUI.setupActionBarWithNavController(this, NaviController(
            this
        ).controller, appBarConfiguration)
        navigation.setupWithNavController(
            NaviController(
                this
            ).controller)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        snackBar = Snackbar.make(snackCoordinator, resources.getString(R.string.no_connection), Snackbar.LENGTH_INDEFINITE)
        setupNavigation()

        val connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this, androidx.lifecycle.Observer { isConnected ->
            isConnected?.let {
                    when (it){
                        true -> {snackBar.dismiss()}
                        false -> {
                            val layout = snackBar.view as Snackbar.SnackbarLayout
                            layout.apply{
                                setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
                                textView().textSize = 20F}

                            snackBar.apply{
                                setActionTextColor(ContextCompat.getColor(context, R.color.colorMain))
                                show()}
                        }
                    }
                }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}