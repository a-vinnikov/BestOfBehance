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
import com.example.bestofbehance.module.FragmentNavigate
import com.example.bestofbehance.adapter.AdapterOfflineBest
import com.example.bestofbehance.adapter.PagingAdapterBest
import com.example.bestofbehance.classesToSupport.*
import com.example.bestofbehance.module.StorageModule
import com.example.bestofbehance.viewModel.*



class Best : Fragment(), SwipeRefreshLayout.OnRefreshListener {

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
        currentViewMode = StorageModule.getPreferences(context!!, resources.getString(R.string.current_view_mode), currentViewMode)
        when (item.itemId) {
            R.id.menu_switcher -> {

                when(currentViewMode){
                    VIEW_MODE_GRIDVIEW -> {
                        StorageModule.editorPreferences(context!!, resources.getString(R.string.current_view_mode), VIEW_MODE_LISTVIEW)
                        recyclerView.layoutManager = LinearLayoutManager(context!!)
                        item.setIcon(R.drawable.ic_tile)
                    }
                    VIEW_MODE_LISTVIEW -> {
                        StorageModule.editorPreferences(context!!, resources.getString(R.string.current_view_mode), VIEW_MODE_GRIDVIEW)
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
        currentViewMode = StorageModule.getPreferences(context!!, resources.getString(R.string.current_view_mode), currentViewMode)
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
        jsonModel = ViewModelProviders.of(this, ViewModelFactory(context!!)).get(ViewModelForParse::class.java)

        currentViewMode = StorageModule.getPreferences(context!!, resources.getString(R.string.current_view_mode), currentViewMode)
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

        return PagingAdapterBest(object : InClick {
            override fun onItemClick(item: CardBinding, position: Int) {
                FragmentNavigate(context!!).toDetailsFromBest(item)
            }
        }, object : BookmarkClick {
            override fun setPosition(position: Int) {
                jsonModel.bookmarksProjects(jsonModel.itemPagedList?.value?.get(position)!!)
            }
        })
    }

    private fun adapterOffline(list: MutableList<CardBinding>): AdapterOfflineBest {

        return AdapterOfflineBest(list, object : InClick {
            override fun onItemClick(item: CardBinding, position: Int) {
                FragmentNavigate(context!!).toDetailsFromBest(item)
            }

        }, object : BookmarkClick {
            override fun setPosition(position: Int) {
                jsonModel.bookmarksProjects(list[position])
            }
        })
    }

}