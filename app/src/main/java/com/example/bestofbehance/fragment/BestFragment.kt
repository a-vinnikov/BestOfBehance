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
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import com.example.bestofbehance.R
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.classesToSupport.BookmarkClick
import com.example.bestofbehance.classesToSupport.InClick
import com.example.bestofbehance.dagger.NavigateModule
import com.example.bestofbehance.dagger.NetworkModule
import com.example.bestofbehance.database.CardDataBase
import com.example.bestofbehance.adapter.AdapterOfflineBest
import com.example.bestofbehance.adapter.PagingAdapterBest
import com.example.bestofbehance.dagger.StorageModule
import com.example.bestofbehance.viewModel.*


const val VIEW_MODE_LISTVIEW = "list"
const val VIEW_MODE_GRIDVIEW = "tile"

class Best : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var jsonModel: VMForParse
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
        currentViewMode = StorageModule.getPreferences(context!!, resources.getString(R.string.current_view_mode), currentViewMode)
        when (item.itemId) {
            R.id.menu_switcher -> {

                when(currentViewMode){
                    VIEW_MODE_GRIDVIEW -> {
                        StorageModule.editorPreferences(context!!, resources.getString(R.string.current_view_mode), VIEW_MODE_LISTVIEW)
                        recyclerView.layoutManager = LinearLayoutManager(activity)
                        item.setIcon(R.drawable.tile)
                    }
                    VIEW_MODE_LISTVIEW -> {
                        StorageModule.editorPreferences(context!!, resources.getString(R.string.current_view_mode), VIEW_MODE_GRIDVIEW)
                        recyclerView.layoutManager = GridLayoutManager(activity, 2)
                        item.setIcon(R.drawable.list)
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
        currentViewMode = StorageModule.getPreferences(context!!, resources.getString(R.string.current_view_mode), currentViewMode)
        createRecyclerView(currentViewMode)
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
        jsonModel = ViewModelProviders.of(this, ViewModelFactory(context!!)).get(VMForParse::class.java)

        currentViewMode = StorageModule.getPreferences(context!!, resources.getString(R.string.current_view_mode), currentViewMode)
        if (recyclerView.adapter == null) {
            when(currentViewMode){
                VIEW_MODE_LISTVIEW -> {recyclerView.layoutManager = LinearLayoutManager(activity)}
                VIEW_MODE_GRIDVIEW -> {recyclerView.layoutManager = GridLayoutManager(activity, 2)}
            }
            createRecyclerView(currentViewMode)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.navigation?.menu?.findItem(R.id.best)?.isChecked = true
    }

    private fun createRecyclerView(currentViewMode: String) {
        NetworkModule(context!!).hasNetwork().let {
            when (it) {
                true -> {
                    if (swipe.isRefreshing || jsonModel.itemPagedList?.value?.size == null) {
                        jsonModel.setGeneral()
                    }

                    jsonModel.itemPagedList?.observe(viewLifecycleOwner,
                        Observer<PagedList<CardBinding>> { list ->
                            adapterBest = adapterFun(list) as PagingAdapterBest
                            adapterBest.submitList(list)
                            recyclerView.adapter = adapterBest
                        })

                }
                false -> {
                    recyclerView.apply {
                        adapter = adapterOffline(CardDataBase.getDatabase(context!!)?.getCardDao()?.all!!)
                        when (currentViewMode) {
                            VIEW_MODE_LISTVIEW -> {
                                layoutManager = LinearLayoutManager(activity)
                            }
                            VIEW_MODE_GRIDVIEW -> {
                                layoutManager = GridLayoutManager(activity, 2)
                            }
                        }
                    }

                }
            }
        }

    }

    private fun adapterFun(
        list: MutableList<CardBinding>): PagedListAdapter<CardBinding, PagingAdapterBest.ViewHolder> {

        return PagingAdapterBest(object : InClick {
            override fun onItemClick(item: CardBinding, position: Int) {
                NavigateModule(context!!).toDetailsFromBest(item)
            }
        }, object : BookmarkClick {
            override fun setPosition(position: Int) {
                jsonModel.bookmarksProjects(list[position])
            }
        })
    }

    private fun adapterOffline(list: MutableList<CardBinding>): AdapterOfflineBest {

        return AdapterOfflineBest(list, object : InClick {
            override fun onItemClick(item: CardBinding, position: Int) {
                NavigateModule(context!!).toDetailsFromBest(item)
            }

        }, object : BookmarkClick {
            override fun setPosition(position: Int) {
                jsonModel.bookmarksProjects(list[position])
            }
        })
    }

}