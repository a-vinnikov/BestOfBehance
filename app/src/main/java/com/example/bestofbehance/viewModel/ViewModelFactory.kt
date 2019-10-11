package com.example.bestofbehance.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ViewModelForParse::class.java)) {
            ViewModelForParse(context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}