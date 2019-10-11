package com.example.bestofbehance.dagger

import android.app.Application
import com.example.bestofbehance.classesToSupport.BehanceApp
import com.example.bestofbehance.dagger.module.StorageModule
import com.example.bestofbehance.dagger.builder.ActivityBuilder
import com.example.bestofbehance.dagger.module.CommonModule
import com.example.bestofbehance.dagger.module.ConfiguratorModule
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Component(
    modules = [
        AndroidInjectionModule::class,
        StorageModule::class,
        CommonModule::class,
        ConfiguratorModule::class,
        ActivityBuilder::class])
interface BehanceComponent: AndroidInjector<BehanceApp> {

    override fun inject(instance: BehanceApp)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): BehanceComponent
    }
}