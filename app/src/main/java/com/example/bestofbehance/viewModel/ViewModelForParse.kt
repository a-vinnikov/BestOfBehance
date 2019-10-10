package com.example.bestofbehance.viewModel

import android.app.Application
import android.content.Context
import android.view.MenuItem
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.bestofbehance.R
import com.example.bestofbehance.binding.*
import com.example.bestofbehance.binding.mapper.MapperForProjectsBinding
import com.example.bestofbehance.classesToSupport.MultiList
import com.example.bestofbehance.classesToSupport.PAGING_PAGE_SIZE
import com.example.bestofbehance.database.CardDataBase
import com.example.bestofbehance.database.PeopleDataBase
import com.example.bestofbehance.database.ProjectsDataBase
import com.example.bestofbehance.paging.ProfileDataSource
import com.example.bestofbehance.paging.BestDataSource
import com.example.bestofbehance.paging.DataSourceFactory



class ViewModelForParse(val context: Context) : AndroidViewModel(Application()) {

    val listForContents: MutableLiveData<MutableList<MultiList>> by lazy { MutableLiveData<MutableList<MultiList>>() }
    val listForComments: MutableLiveData<MutableList<MultiList>> by lazy { MutableLiveData<MutableList<MultiList>>() }
    val listForProject: MutableLiveData<MutableList<CardBinding>> by lazy { MutableLiveData<MutableList<CardBinding>>() }
    val listForUser: MutableLiveData<MutableList<ProfileBinding>> by lazy { MutableLiveData<MutableList<ProfileBinding>>() }
    val listForLinks: MutableLiveData<MutableMap<String, String?>> by lazy { MutableLiveData<MutableMap<String, String?>>() }
    val liveDataMulti = MediatorLiveData<MutableList<MultiList>>()

    var itemPagedList: LiveData<PagedList<CardBinding>>? = null
    var profilePagedList: LiveData<PagedList<CardBinding>>? = null



    fun setGeneral(){
        val itemDataSourceFactory = DataSourceFactory(BestDataSource(context))
        val config =
            PagedList.Config.Builder().setEnablePlaceholders(true).setPageSize(PAGING_PAGE_SIZE).build()

        itemPagedList = LivePagedListBuilder(itemDataSourceFactory, config).build()
    }

    private fun setContent(id: Int): MutableLiveData<MutableList<MultiList>> {
        ParseForViewModel(context).fetchProject(id) { result -> listForContents.postValue(result) }
        return listForContents
    }

    fun setSingleProject(id: Int): MutableLiveData<MutableList<CardBinding>> {
        ParseForViewModel(context).fetchSingleProject(id) { result -> listForProject.postValue(result) }
        return listForProject
    }

    private fun setComments(id: Int): MutableLiveData<MutableList<MultiList>> {
        ParseForViewModel(context).fetchComments(id) { result -> listForComments.postValue(result) }
        return listForComments
    }

    fun setUser(username: String): Pair<MutableLiveData<MutableList<ProfileBinding>>, MutableLiveData<MutableMap<String, String?>>> {
        ParseForViewModel(context).fetchUser(username) { result, links -> listForUser.postValue(result)
            listForLinks.postValue(links)}
        return Pair(listForUser, listForLinks)
    }

    fun setUserProjects(username: String){
        profilePagedList?.value?.clear()
        val itemDataSourceFactory = DataSourceFactory(ProfileDataSource(username, context))
        val config =
            PagedList.Config.Builder().setEnablePlaceholders(true).setPageSize(PAGING_PAGE_SIZE).build()

        profilePagedList = LivePagedListBuilder(itemDataSourceFactory, config).build()
    }

    fun fetchData(id: Int): MediatorLiveData<MutableList<MultiList>> {
        liveDataMulti.value?.clear()
        mediatorAdd(liveDataMulti, setContent(id))
        mediatorAdd(liveDataMulti, setComments(id))
        return liveDataMulti
    }

    private fun mediatorAdd(mediator: MediatorLiveData<MutableList<MultiList>>, list: LiveData<MutableList<MultiList>>){
            try {
                mediator.addSource(list) {
                    if (it != null) {
                        mediator.value = it
                    }
                }
            }
            catch (e: IllegalArgumentException){}
    }

    fun bookmarksToolbar(binding: CardBinding?, item: MenuItem){
        if (ProjectsDataBase.getDatabase(context)?.getProjectsDao()?.getById(binding?.id!!) == null) {
            ProjectsDataBase.getDatabase(context)?.getProjectsDao()?.insert(MapperForProjectsBinding.from(binding!!))
            item.setIcon(R.drawable.ic_bookmarks_pressed)
        } else {
            ProjectsDataBase.getDatabase(context)?.getProjectsDao()?.deleteById(binding?.id!!)
            item.setIcon(R.drawable.ic_bookmarks_normal)
        }
    }

    fun bookmarksProjects(binding: CardBinding){
        if (ProjectsDataBase.getDatabase(context)?.getProjectsDao()?.getById(binding.id!!) == null) {
            ProjectsDataBase.getDatabase(context)?.getProjectsDao()?.insert(MapperForProjectsBinding.from(binding))
        } else {
            ProjectsDataBase.getDatabase(context)?.getProjectsDao()?.deleteById(binding.id!!)
        }
    }

    fun getAllFromCardDB() = CardDataBase.getDatabase(context)?.getCardDao()?.all!!

    fun getAllFromPeopleDB() = PeopleDataBase.getDatabase(context)?.getPeopleDao()?.all

    fun getByUsernameFromPeopleDB(username: String) = PeopleDataBase.getDatabase(context)?.getPeopleDao()?.getByUsername(username)

    fun deleteByUsernameFromPeopleDB(username: String) = PeopleDataBase.getDatabase(context)?.getPeopleDao()?.deleteByUsername(username)

    fun insertInPeopleDB(data: PeopleBinding) = PeopleDataBase.getDatabase(context)?.getPeopleDao()?.insert(data)

    fun checkInProjectsDB(id: Int) = ProjectsDataBase.getDatabase(context)?.getProjectsDao()?.getById(id)

    fun getAllFromProjectsDB() = ProjectsDataBase.getDatabase(context)?.getProjectsDao()?.all

    fun getByIdFromProjectsDB(id: Int) = ProjectsDataBase.getDatabase(context)?.getProjectsDao()?.getById(id)

    fun deleteByIdFromProjectsDB(id: Int) = ProjectsDataBase.getDatabase(context)?.getProjectsDao()?.deleteById(id)
}
