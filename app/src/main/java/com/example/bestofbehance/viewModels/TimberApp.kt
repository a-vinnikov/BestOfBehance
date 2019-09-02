package com.example.bestofbehance.viewModels

import android.app.Application
import com.example.bestofbehance.BuildConfig
import timber.log.Timber

class TimberApp: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
        Timber.plant(Timber.DebugTree())
        }
    }
}