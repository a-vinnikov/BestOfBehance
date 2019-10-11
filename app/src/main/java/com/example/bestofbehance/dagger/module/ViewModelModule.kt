package com.example.bestofbehance.dagger.module

import androidx.lifecycle.ViewModel
import com.example.bestofbehance.dagger.ViewModelKey
import com.example.bestofbehance.viewModel.ViewModelForParse
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelForParse::class)
    abstract fun bindViewModelForParse(viewModel: ViewModelForParse): ViewModel
}