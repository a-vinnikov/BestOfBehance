package com.example.bestofbehance.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.layout.MultiList


class VMForParse : AndroidViewModel(Application()) {

    val mainContentList: MutableLiveData<MutableList<CardBinding>> by lazy { MutableLiveData<MutableList<CardBinding>>() }
    val pagingResponseList: MutableLiveData<MutableList<CardBinding>> by lazy { MutableLiveData<MutableList<CardBinding>>() }
    val listForContents: MutableLiveData<MutableList<MultiList>> by lazy { MutableLiveData<MutableList<MultiList>>() }
    val listForComments: MutableLiveData<MutableList<MultiList>> by lazy { MutableLiveData<MutableList<MultiList>>() }

    fun setGeneral(page: Int): MutableLiveData<MutableList<CardBinding>> {
        ParseForVM().parseGeneral(page) { result -> mainContentList.postValue(result)}
        return mainContentList
    }

    fun setNextPage(page: Int): MutableLiveData<MutableList<CardBinding>> {
        pagingResponseList.value?.clear()
        ParseForVM().parseGeneral(page) { result -> pagingResponseList.postValue(result)}
        return pagingResponseList
    }

    fun setContent(id: Int): MutableLiveData<MutableList<MultiList>> {
        ParseForVM().parseProject(id) { result -> listForContents.postValue(result) }
        return listForContents
    }

    fun setComments(id: Int): MutableLiveData<MutableList<MultiList>> {
        ParseForVM().parseComments(id) { result -> listForComments.postValue(result) }
        return listForComments
    }

    /*fun insertData(context: Context, it: MutableList<CardBinding>) = viewModelScope.launch(Dispatchers.IO) {
        for (i in 0 until it.size){
            CardDataBase.DatabaseProvider.getDatabase(context).cardDao().insert(it[i])
        }
    }*/

}
