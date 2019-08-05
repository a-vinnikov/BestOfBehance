package com.example.bestofbehance.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.bestofbehance.gson.CardBinding
import com.example.bestofbehance.layout.Ilist


class VMForParse : AndroidViewModel(Application()) {

    val recList: MutableLiveData<MutableList<CardBinding>> by lazy { MutableLiveData<MutableList<CardBinding>>() }
    val listForIlisit: MutableLiveData<MutableList<Ilist>> by lazy { MutableLiveData<MutableList<Ilist>>() }
    val listForIlisit1: MutableLiveData<MutableList<Ilist>> by lazy { MutableLiveData<MutableList<Ilist>>() }

    fun setGeneral(): MutableLiveData<MutableList<CardBinding>> {
        ParseForVM().parseGeneral { result -> recList.postValue(result) }
        return recList
    }

    fun setImage(id: Int): MutableLiveData<MutableList<Ilist>> {
        val temp: MutableList<Ilist> = mutableListOf()

        ParseForVM().parseProject(id) { result ->

            for (i in 0 until result.size) {
                temp.add(i, Ilist.ImageList(result[i]))
            }
            listForIlisit.postValue(temp)
        }
        return listForIlisit
    }

    fun setComments(id: Int): MutableLiveData<MutableList<Ilist>> {
        val temp: MutableList<Ilist> = mutableListOf()

        ParseForVM().parseComments(id) { result ->
            for (i in 0 until result.size) {
                temp.add(i, Ilist.CommentsList(result[i]))
            }
            listForIlisit1.postValue(temp)
        }
        return listForIlisit1

    }

    /*fun insertData(context: Context, it: MutableList<CardBinding>) = viewModelScope.launch(Dispatchers.IO) {
        for (i in 0 until it.size){
            CardDataBase.DatabaseProvider.getDatabase(context).cardDao().insert(it[i])
        }
    }*/

}
