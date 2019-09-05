package com.example.bestofbehance.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.binding.ProfileBinding
import com.example.bestofbehance.fragments.MultiList
import com.example.bestofbehance.paging.ProfileDataSource
import com.example.bestofbehance.paging.BestDataSource
import com.example.bestofbehance.paging.DataSourceFactory

const val PAGING_PAGE_SIZE = 10

class VMForParse : AndroidViewModel(Application()) {

    val mainContentList: MutableLiveData<MutableList<CardBinding>> by lazy { MutableLiveData<MutableList<CardBinding>>() }
    val listForContents: MutableLiveData<MutableList<MultiList>> by lazy { MutableLiveData<MutableList<MultiList>>() }
    val listForComments: MutableLiveData<MutableList<MultiList>> by lazy { MutableLiveData<MutableList<MultiList>>() }
    val listForUser: MutableLiveData<MutableList<ProfileBinding>> by lazy { MutableLiveData<MutableList<ProfileBinding>>() }
    val listForLinks: MutableLiveData<MutableMap<String, String?>> by lazy { MutableLiveData<MutableMap<String, String?>>() }

    var itemPagedList: LiveData<PagedList<CardBinding>>? = null
    var profilePagedList: LiveData<PagedList<CardBinding>>? = null



    fun setGeneral(context: Context){
        val itemDataSourceFactory = DataSourceFactory(BestDataSource(context))
        val config =
            PagedList.Config.Builder().setEnablePlaceholders(true).setPageSize(PAGING_PAGE_SIZE).build()

        itemPagedList = LivePagedListBuilder(itemDataSourceFactory, config).build()
    }

    fun setDBGeneral(page: Int): MutableLiveData<MutableList<CardBinding>> {
        ParseForVM().generalRetrofit(page) { result -> mainContentList.postValue(result) }
        return mainContentList
    }

    fun setContent(id: Int): MutableLiveData<MutableList<MultiList>> {
        ParseForVM().projectRetrofit(id) { result -> listForContents.postValue(result) }
        return listForContents
    }

    fun setComments(id: Int): MutableLiveData<MutableList<MultiList>> {
        ParseForVM().commentsRetrofit(id) { result -> listForComments.postValue(result) }
        return listForComments
    }

    fun setUser(username: String): Pair<MutableLiveData<MutableList<ProfileBinding>>, MutableLiveData<MutableMap<String, String?>>> {
        ParseForVM().userRetrofit(username) { result, links -> listForUser.postValue(result)
            listForLinks.postValue(links)}
        return Pair(listForUser, listForLinks)
    }

    fun setUserProjects(username: String){
        val itemDataSourceFactory = DataSourceFactory(ProfileDataSource(username))
        val config =
            PagedList.Config.Builder().setEnablePlaceholders(true).setPageSize(PAGING_PAGE_SIZE).build()

        profilePagedList = LivePagedListBuilder(itemDataSourceFactory, config).build()
    }
}
