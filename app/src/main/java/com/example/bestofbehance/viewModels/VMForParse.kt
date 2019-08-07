package com.example.bestofbehance.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.bestofbehance.gson.CardBinding
import com.example.bestofbehance.layout.Ilist


class VMForParse : AndroidViewModel(Application()) {

    val recList: MutableLiveData<MutableList<CardBinding>> by lazy { MutableLiveData<MutableList<CardBinding>>() }
    val recList1: MutableLiveData<MutableList<CardBinding>> by lazy { MutableLiveData<MutableList<CardBinding>>() }
    val listForIlisit: MutableLiveData<MutableList<Ilist>> by lazy { MutableLiveData<MutableList<Ilist>>() }
    val listForIlisit1: MutableLiveData<MutableList<Ilist>> by lazy { MutableLiveData<MutableList<Ilist>>() }

    fun setGeneral(page: Int): MutableLiveData<MutableList<CardBinding>> {
        ParseForVM().parseGeneral(page) { result -> recList.postValue(result)}
        return recList
    }

    fun setAlterGeneral(page: Int): MutableLiveData<MutableList<CardBinding>> {
        recList1.value?.clear()
        ParseForVM().parseGeneral(page) { result -> recList1.postValue(result)}
        return recList1
    }

    fun setContent(id: Int): MutableLiveData<MutableList<Ilist>> {
        ParseForVM().parseProject(id) { result -> listForIlisit.postValue(result) }
        return listForIlisit
    }

    fun setComments(id: Int): MutableLiveData<MutableList<Ilist>> {
        ParseForVM().parseComments(id) { result -> listForIlisit1.postValue(result) }
        return listForIlisit1
    }

    /*fun insertData(context: Context, it: MutableList<CardBinding>) = viewModelScope.launch(Dispatchers.IO) {
        for (i in 0 until it.size){
            CardDataBase.DatabaseProvider.getDatabase(context).cardDao().insert(it[i])
        }
    }*/

}
