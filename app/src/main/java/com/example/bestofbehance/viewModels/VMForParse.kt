package com.example.bestofbehance.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.binding.ProfileBinding
import com.example.bestofbehance.layout.MultiList


class VMForParse : AndroidViewModel(Application()) {

    val mainContentList: MutableLiveData<MutableList<CardBinding>> by lazy { MutableLiveData<MutableList<CardBinding>>() }
    val pagingResponseList: MutableLiveData<MutableList<CardBinding>> by lazy { MutableLiveData<MutableList<CardBinding>>() }
    val listForContents: MutableLiveData<MutableList<MultiList>> by lazy { MutableLiveData<MutableList<MultiList>>() }
    val listForComments: MutableLiveData<MutableList<MultiList>> by lazy { MutableLiveData<MutableList<MultiList>>() }
    val listForUser: MutableLiveData<MutableList<ProfileBinding>> by lazy { MutableLiveData<MutableList<ProfileBinding>>() }

    fun setGeneral(page: Int): MutableLiveData<MutableList<CardBinding>> {
        ParseForVM().generalRetrofit(page) { result -> mainContentList.postValue(result) }
        return mainContentList
    }

    fun setNextPage(page: Int): MutableLiveData<MutableList<CardBinding>> {
        pagingResponseList.value?.clear()
        ParseForVM().generalRetrofit(page) { result -> pagingResponseList.postValue(result) }
        return pagingResponseList
    }

    fun setContent(id: Int): MutableLiveData<MutableList<MultiList>> {
        ParseForVM().projectRetrofit(id) { result -> listForContents.postValue(result) }
        return listForContents
    }

    fun setComments(id: Int): MutableLiveData<MutableList<MultiList>> {
        ParseForVM().commentsRetrofit(id) { result -> listForComments.postValue(result) }
        return listForComments
    }

    fun setUser(username: String): MutableLiveData<MutableList<ProfileBinding>> {
        ParseForVM().userRetrofit(username) { result -> listForUser.postValue(result) }
        return listForUser
    }

}
