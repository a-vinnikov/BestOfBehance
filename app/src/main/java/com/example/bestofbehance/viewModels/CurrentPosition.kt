package com.example.bestofbehance.viewModels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class CurrentPosition : ViewModel(){

    private val currentPosition: MutableLiveData<Int> = MutableLiveData()

    fun setCurrentPosition(position:Int){
        currentPosition.postValue(position)
    }

    fun getCurrentPosition(): MutableLiveData<Int> {
        return currentPosition
    }
}