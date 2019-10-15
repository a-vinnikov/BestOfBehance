package com.example.bestofbehance.dagger.module

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.bestofbehance.classesToSupport.BehanceApp
import com.example.bestofbehance.viewModel.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class CommonModule {

    @Binds
    abstract fun bindContext(application: Application): Context

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}