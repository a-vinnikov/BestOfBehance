package com.example.bestofbehance.dagger.module

import android.app.Activity
import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.bestofbehance.MainActivity
import com.example.bestofbehance.R
import com.example.bestofbehance.dagger.FragmentNavigate
import com.example.bestofbehance.dagger.scope.PerActivity
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule {

    @Provides
    fun provideController(activity: MainActivity) = activity.findNavController(R.id.fr)

    @Provides
    @PerActivity
    fun provideNavigate(nav: NavController): FragmentNavigate {
        return FragmentNavigate(nav)
    }
}