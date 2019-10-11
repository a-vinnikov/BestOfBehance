package com.example.bestofbehance.dagger.module

import android.content.Context
import android.content.SharedPreferences
import com.example.bestofbehance.dagger.AllAboutSharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {

    @Provides
    fun providePreferences(context: Context): SharedPreferences = context.getSharedPreferences("viewMode", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideSharedPreferences(preferences: SharedPreferences): AllAboutSharedPreferences {
        return AllAboutSharedPreferences(preferences)
    }
}