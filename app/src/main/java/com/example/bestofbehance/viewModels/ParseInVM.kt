package com.example.bestofbehance.viewModels

import android.arch.lifecycle.*
import com.example.bestofbehance.gson.CardBinding

class ParseInVM: ViewModel() {

    val recList: MutableLiveData<MutableList<CardBinding>> by lazy { MutableLiveData<MutableList<CardBinding>>() }

    fun getGeneral(): MutableLiveData<MutableList<CardBinding>> {
        recList.value?.clear()
        return ParseGeneralForVM().parseGeneral(recList)
    }



}
