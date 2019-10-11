package com.example.bestofbehance.dagger.module

import com.example.bestofbehance.fragment.*
import com.example.bestofbehance.dagger.scope.PerFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @PerFragment
    abstract fun provideBestFragment(): Best

    @ContributesAndroidInjector
    @PerFragment
    abstract fun provideDetailsFragment(): DetailsFragment

    @ContributesAndroidInjector
    @PerFragment
    abstract fun providePeopleFragment(): PeopleFragment

    @ContributesAndroidInjector
    @PerFragment
    abstract fun provideProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    @PerFragment
    abstract fun provideProjectsFragment(): ProjectsFragment
}