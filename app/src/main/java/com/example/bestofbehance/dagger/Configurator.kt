package com.example.bestofbehance.dagger

import android.app.Application

interface Configurator {
    fun configure(app: Application)
}