package com.example.bestofbehance.dagger

import android.app.Application
import com.example.bestofbehance.BuildConfig
import timber.log.Timber

class TimberConfigurator : Configurator {
    override fun configure(app: Application) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Timber.tag("Behance")
    }
}