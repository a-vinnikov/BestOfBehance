package com.example.bestofbehance.classesToSupport

import android.app.Application
import com.example.bestofbehance.dagger.Configurator
import com.example.bestofbehance.dagger.DaggerBehanceComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject


class BehanceApp: Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector() = androidInjector

    @Inject
    lateinit var configurators: Set<@JvmSuppressWildcards Configurator>

    override fun onCreate() {
        super.onCreate()
        /*if (BuildConfig.DEBUG) {
        Timber.plant(Timber.DebugTree())
        }*/
        initializeInjector()
        configure()
    }

    private fun initializeInjector() {
        DaggerBehanceComponent.builder().application(this).build().inject(this)
    }

    private fun configure() {
        configurators.forEach { it.configure(this) }
    }
}