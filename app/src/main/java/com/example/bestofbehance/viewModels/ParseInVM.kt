package com.example.bestofbehance.viewModels

import androidx.lifecycle.*
import com.example.bestofbehance.gson.CardBinding

class ParseInVM: ViewModel() {

    val recList: MutableLiveData<MutableList<CardBinding>> by lazy { MutableLiveData<MutableList<CardBinding>>() }

    fun setGeneral(){
        ParseForVM().parseGeneral{ result -> recList.postValue(result)}
    }



}
