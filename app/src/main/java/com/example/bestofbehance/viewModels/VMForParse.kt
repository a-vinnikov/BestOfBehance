package com.example.bestofbehance.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.binding.ProfileBinding
import com.example.bestofbehance.databases.CacheDBMain
import com.example.bestofbehance.databases.DBMain
import com.example.bestofbehance.layout.MultiList


class VMForParse : AndroidViewModel(Application()) {

    val mainContentList: MutableLiveData<MutableList<CardBinding>> by lazy { MutableLiveData<MutableList<CardBinding>>() }
    val pagingResponseList: MutableLiveData<MutableList<CardBinding>> by lazy { MutableLiveData<MutableList<CardBinding>>() }
    val listForContents: MutableLiveData<MutableList<MultiList>> by lazy { MutableLiveData<MutableList<MultiList>>() }
    val listForComments: MutableLiveData<MutableList<MultiList>> by lazy { MutableLiveData<MutableList<MultiList>>() }
    val listForUser: MutableLiveData<MutableList<ProfileBinding>> by lazy { MutableLiveData<MutableList<ProfileBinding>>() }
    val listForUserProjects: MutableLiveData<MutableList<CardBinding>> by lazy { MutableLiveData<MutableList<CardBinding>>() }

    fun setGeneral(context: Context, page: Int): MutableLiveData<MutableList<CardBinding>> {
        CacheDBMain.read(context) { list ->
            if (list.size == 0) {
                ParseForVM().generalRetrofit(page) { result ->
                    for (i in 0 until result.size) {
                        CacheDBMain.add(context, result[i])
                    }
                }
            }
            CacheDBMain.read(context) { mainContentList.postValue(it) }
        }
        CacheDBMain.close(context)
        return mainContentList
    }

    fun setNextPage(page: Int): MutableLiveData<MutableList<CardBinding>> {
        pagingResponseList.value?.clear()
        ParseForVM().generalRetrofit(page) { result ->
            val abc = result.sortedByDescending { it.appreciations }
            pagingResponseList.postValue(abc as MutableList<CardBinding>)
        }
        return pagingResponseList
    }

    fun setNextPageForUser(
        username: String,
        page: Int
    ): MutableLiveData<MutableList<CardBinding>> {
        pagingResponseList.value?.clear()
        ParseForVM().userProjectsRetrofit(username, page) { result ->
            pagingResponseList.postValue(result)
        }
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

    fun setUserProjects(
        username: String,
        page: Int
    ): MutableLiveData<MutableList<CardBinding>> {
        ParseForVM().userProjectsRetrofit(username, page) { result ->
            listForUserProjects.postValue(
                result
            )
        }
        return listForUserProjects
    }

}
