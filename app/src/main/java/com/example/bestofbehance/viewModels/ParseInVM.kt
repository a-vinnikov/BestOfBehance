package com.example.bestofbehance.viewModels

import android.arch.lifecycle.*
import com.example.bestofbehance.gson.CardBinding

class ParseInVM: ViewModel() {

    val recList: MutableLiveData<MutableList<CardBinding>> by lazy { MutableLiveData<MutableList<CardBinding>>() }

    fun setGeneral(){
        recList.value?.clear()
        ParseGeneralForVM().parseGeneral{ result -> recList.postValue(result) }
    }



}
