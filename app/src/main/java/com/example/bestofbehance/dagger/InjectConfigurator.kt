package com.example.bestofbehance.dagger

import android.app.Application

class InjectorConfigurator : Configurator {

    override fun configure(app: Application) {
        AppInjector.init(app)
    }

}