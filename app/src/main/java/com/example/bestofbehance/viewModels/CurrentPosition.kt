package com.example.bestofbehance.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CurrentPosition : ViewModel(){

    private val currentPosition: MutableLiveData<Int> = MutableLiveData()

    fun setCurrentPosition(position:Int){
        currentPosition.postValue(position)
    }

    fun getCurrentPosition(): MutableLiveData<Int> {
        return currentPosition
    }
}