package com.example.bestofbehance.viewModels

import androidx.lifecycle.*
import com.example.bestofbehance.gson.CardBinding
import com.example.bestofbehance.layout.Ilist

class ParseInVM: ViewModel() {

    val recList: MutableLiveData<MutableList<CardBinding>> by lazy { MutableLiveData<MutableList<CardBinding>>() }
    val listForIlisit: MutableLiveData<MutableList<Ilist>> by lazy { MutableLiveData<MutableList<Ilist>>() }
    val listForIlisit1: MutableLiveData<MutableList<Ilist>> by lazy { MutableLiveData<MutableList<Ilist>>() }

    fun setGeneral(){
        ParseForVM().parseGeneral{ result -> recList.postValue(result)}
    }

    fun setImage(id: String){
        val pepega: MutableList<Ilist> = mutableListOf()

        ParseForVM().parseProject(id.toInt()) { result ->

            for (i in 0 until result.size) {
                pepega.add(i, Ilist.ImageList(result[i]))
            }
            listForIlisit.postValue(pepega)
        }
    }

    fun setComments(id: String){
        val pepega: MutableList<Ilist> = mutableListOf()

        ParseForVM().parseComments(id.toInt()) { result ->
            for (i in 0 until result.size) {
                pepega.add(i, Ilist.CommentsList(result[i]))
            }
            listForIlisit.postValue(pepega)
        }

    }



}
