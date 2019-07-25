package com.example.bestofbehance.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ParseInVM::class.java)) {
            ParseInVM() as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
