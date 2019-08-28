package com.example.bestofbehance.viewModels

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.binding.ProfileBinding
import com.example.bestofbehance.fragments.MultiList
import com.example.bestofbehance.paging.ProfileDataSource
import com.example.bestofbehance.paging.BestDataSource
import com.example.bestofbehance.paging.DataSourceFactory


class VMForParse : AndroidViewModel(Application()) {

    val PAGE_SIZE = 10
    val mainContentList: MutableLiveData<MutableList<CardBinding>> by lazy { MutableLiveData<MutableList<CardBinding>>() }
    val pagingResponseList: MutableLiveData<MutableList<CardBinding>> by lazy { MutableLiveData<MutableList<CardBinding>>() }
    val listForContents: MutableLiveData<MutableList<MultiList>> by lazy { MutableLiveData<MutableList<MultiList>>() }
    val listForComments: MutableLiveData<MutableList<MultiList>> by lazy { MutableLiveData<MutableList<MultiList>>() }
    val listForUser: MutableLiveData<MutableList<ProfileBinding>> by lazy { MutableLiveData<MutableList<ProfileBinding>>() }
    val listForUserProjects: MutableLiveData<MutableList<CardBinding>> by lazy { MutableLiveData<MutableList<CardBinding>>() }

    var itemPagedList: LiveData<PagedList<CardBinding>>? = null
    var profilePagedList: LiveData<PagedList<CardBinding>>? = null

/*    init {
        val itemDataSourceFactory = TestDataSourceFactory(TestDataSource())
        val config =
            PagedList.Config.Builder().setEnablePlaceholders(true).setPageSize(PAGE_SIZE).build()

        itemPagedList = LivePagedListBuilder(itemDataSourceFactory, config).build()
    }*/



    fun setGeneral(){
        /*ParseForVM().generalRetrofit(page) { result ->
            val abc = result.sortedByDescending { it.published }
            mainContentList.postValue(abc as MutableList<CardBinding>) }
        return mainContentList*/
        val itemDataSourceFactory = DataSourceFactory(BestDataSource())
        val config =
            PagedList.Config.Builder().setEnablePlaceholders(true).setPageSize(PAGE_SIZE).build()

        itemPagedList = LivePagedListBuilder(itemDataSourceFactory, config).build()
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

    fun setUserProjects(username: String){
        /*ParseForVM().userProjectsRetrofit(username, page) { result -> listForUserProjects.postValue(result) }
        return listForUserProjects*/
        val itemDataSourceFactory = DataSourceFactory(ProfileDataSource(username))
        val config =
            PagedList.Config.Builder().setEnablePlaceholders(true).setPageSize(PAGE_SIZE).build()

        profilePagedList = LivePagedListBuilder(itemDataSourceFactory, config).build()
    }
}
