package com.example.bestofbehance.fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.fragment_best.*
import kotlinx.android.synthetic.main.activity_main.*
import android.view.MenuInflater
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import com.example.bestofbehance.R
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.adapter.AdapterOfflineBest
import com.example.bestofbehance.adapter.PagingAdapterBest
import com.example.bestofbehance.classesToSupport.*
import com.example.bestofbehance.classesToSupport.listeners.BookmarkClick
import com.example.bestofbehance.classesToSupport.listeners.LayoutClick
import com.example.bestofbehance.classesToSupport.listeners.UserClick
import com.example.bestofbehance.dagger.AllAboutSharedPreferences
import com.example.bestofbehance.dagger.FragmentNavigate
import com.example.bestofbehance.dagger.Injectable
import com.example.bestofbehance.viewModel.ViewModelForParse
import javax.inject.Inject


class BestFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, Injectable{

    @Inject
    lateinit var preferences: AllAboutSharedPreferences

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigate: FragmentNavigate

    lateinit var jsonModel: ViewModelForParse
    private var currentViewMode = VIEW_MODE_LISTVIEW
    lateinit var adapterBest: PagingAdapterBest

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.one_button_toolbar, menu)

        when (currentViewMode) {
            VIEW_MODE_LISTVIEW-> {
                menu.findItem(R.id.menu_switcher)
                    ?.setIcon(R.drawable.ic_tile)
            }
            VIEW_MODE_GRIDVIEW -> {
                menu.findItem(R.id.menu_switcher)
                    ?.setIcon(R.drawable.ic_list)
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        currentViewMode = preferences.stringGet(resources.getString(R.string.current_view_mode), currentViewMode)
        when (item.itemId) {
            R.id.menu_switcher -> {

                when(currentViewMode){
                    VIEW_MODE_GRIDVIEW -> {
                        preferences.stringEdit(resources.getString(R.string.current_view_mode), VIEW_MODE_LISTVIEW)
                        adapterBest.viewMode = VIEW_MODE_LISTVIEW
                        recyclerView.layoutManager = LinearLayoutManager(context!!)
                        item.setIcon(R.drawable.ic_tile)
                    }
                    VIEW_MODE_LISTVIEW -> {
                        preferences.stringEdit(resources.getString(R.string.current_view_mode), VIEW_MODE_GRIDVIEW)
                        adapterBest.viewMode = VIEW_MODE_GRIDVIEW
                        recyclerView.layoutManager = GridLayoutManager(context!!, 2)
                        item.setIcon(R.drawable.ic_list)
                    }
                }
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onRefresh() {
        currentViewMode = preferences.stringGet(resources.getString(R.string.current_view_mode), currentViewMode)
        createRecyclerView()
        swipe.isRefreshing = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_best, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe.setOnRefreshListener(this)
        jsonModel = ViewModelProviders.of(this, viewModelFactory).get(ViewModelForParse::class.java)

        currentViewMode = preferences.stringGet(resources.getString(R.string.current_view_mode), currentViewMode)
        if (recyclerView.adapter == null) {
            when(currentViewMode){
                VIEW_MODE_LISTVIEW -> {recyclerView.layoutManager = LinearLayoutManager(activity)}
                VIEW_MODE_GRIDVIEW -> {recyclerView.layoutManager = GridLayoutManager(activity, 2)}
            }
            createRecyclerView()
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.navigation?.menu?.findItem(R.id.best)?.isChecked = true
    }

    private fun createRecyclerView() {
        val connectionLiveData = ConnectionLiveData(context!!)
        connectionLiveData.observe(this, androidx.lifecycle.Observer { isConnected ->
            isConnected?.let {
                when (it) {
                    true -> {
                        if (swipe.isRefreshing || jsonModel.itemPagedList?.value?.size == null) {
                            jsonModel.setGeneral()
                        }
                        adapterBest = adapterFun() as PagingAdapterBest
                        recyclerView.adapter = adapterBest

                        jsonModel.itemPagedList?.observe(viewLifecycleOwner,
                            Observer<PagedList<CardBinding>> { list ->
                                adapterBest.submitList(list)
                            })
                    }
                    false -> {
                        recyclerView.apply { adapter = adapterOffline(jsonModel.getAllFromCardDB()) }
                    }
                }
            }
        })

    }

    private fun adapterFun(): PagedListAdapter<CardBinding, PagingAdapterBest.ViewHolder> {

        return PagingAdapterBest(currentViewMode, object :
            LayoutClick {
            override fun onItemClick(item: CardBinding, position: Int) {
                navigate.toDetailsFromBest(item.id.toString())
            }
        }, object : BookmarkClick {
            override fun setPosition(position: Int) {
                jsonModel.bookmarksProjects(jsonModel.itemPagedList?.value?.get(position)!!)
            }
        }, object: UserClick {
            override fun onUserClick(username: String) {
                navigate.toProfileFromBest(username)
            }

        })
    }

    private fun adapterOffline(list: MutableList<CardBinding>): AdapterOfflineBest {

        return AdapterOfflineBest(currentViewMode, list, object :
            LayoutClick {
            override fun onItemClick(item: CardBinding, position: Int) {
                navigate.toDetailsFromBest(item.id.toString())
            }

        }, object : BookmarkClick {
            override fun setPosition(position: Int) {
                jsonModel.bookmarksProjects(list[position])
            }
        }, object: UserClick {
            override fun onUserClick(username: String) {
                navigate.toProfileFromBest(username)
            }}
        )
    }

}