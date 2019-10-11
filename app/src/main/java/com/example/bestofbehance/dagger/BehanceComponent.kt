package com.example.bestofbehance.dagger

import android.app.Application
import com.example.bestofbehance.classesToSupport.BehanceApp
import com.example.bestofbehance.dagger.builder.ActivityBuilder
import com.example.bestofbehance.dagger.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        StorageModule::class,
        NavigateModule::class,
        CommonModule::class,
        ConfiguratorModule::class,
        NetworkModule::class,
        ActivityBuilder::class])
interface BehanceComponent{

    fun inject(instance: BehanceApp)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): BehanceComponent
    }
}