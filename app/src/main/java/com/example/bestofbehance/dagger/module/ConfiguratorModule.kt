package com.example.bestofbehance.dagger.module

import com.example.bestofbehance.dagger.Configurator
import com.example.bestofbehance.dagger.InjectorConfigurator
import com.example.bestofbehance.dagger.TimberConfigurator
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

/**
 * Created by gelort on 2019-05-21.
 */

@Module
class ConfiguratorModule {

    @Provides
    @IntoSet
    fun provideInjectorConfigure(): Configurator = InjectorConfigurator()

    @Provides
    @IntoSet
    fun provideTimberConfigure(): Configurator = TimberConfigurator()

}