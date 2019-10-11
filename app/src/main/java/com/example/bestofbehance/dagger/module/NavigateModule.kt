package com.example.bestofbehance.dagger.module

import android.app.Activity
import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.bestofbehance.R
import com.example.bestofbehance.dagger.FragmentNavigate
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NavigateModule{

    @Provides
    fun provideController(context: Context) = Navigation.findNavController(context as Activity, R.id.fr)

    @Provides
    @Singleton
    fun provideNavigate(nav: NavController): FragmentNavigate{
        return FragmentNavigate(nav)
    }
}