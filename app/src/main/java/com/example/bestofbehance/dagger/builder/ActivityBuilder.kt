package com.example.bestofbehance.dagger.builder

import com.example.bestofbehance.MainActivity
import com.example.bestofbehance.dagger.module.FragmentModule
import com.example.bestofbehance.dagger.module.ViewModelModule
import com.example.bestofbehance.dagger.scope.PerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @PerActivity
    @ContributesAndroidInjector(modules = [FragmentModule::class, ViewModelModule::class])
    abstract fun bindMainActivity(): MainActivity
}