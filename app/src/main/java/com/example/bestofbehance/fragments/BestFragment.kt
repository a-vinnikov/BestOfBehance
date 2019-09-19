package com.example.bestofbehance.fragments

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
import com.example.bestofbehance.binding.ProjectsBinding
import com.example.bestofbehance.classesToSupport.BookmarkClick
import com.example.bestofbehance.classesToSupport.CurrentDate
import com.example.bestofbehance.classesToSupport.InClick
import com.example.bestofbehance.classesToSupport.NaviController
import com.example.bestofbehance.dagger.NetworkModule
import com.example.bestofbehance.classesToSupport.SharedPreferenceObject.editorSharedPreference
import com.example.bestofbehance.classesToSupport.SharedPreferenceObject.sharedCurrentViewMode
import com.example.bestofbehance.databases.CardDataBase
import com.example.bestofbehance.databases.ProjectsDataBase
import com.example.bestofbehance.forAdapters.AdapterNonPaging
import com.example.bestofbehance.forAdapters.PagingAdapterViewHolder
import com.example.bestofbehance.viewModels.*


const val VIEW_MODE_LISTVIEW = "list"
const val VIEW_MODE_GRIDVIEW = "tile"

class Best : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var jsonModel: VMForParse
    private var currentViewMode = VIEW_MODE_LISTVIEW
    lateinit var adapterBest: PagingAdapterViewHolder

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.one_button_toolbar, menu)

        when (currentViewMode) {
            VIEW_MODE_LISTVIEW-> {
                menu.findItem(R.id.menu_switcher)
                    ?.setIcon(R.drawable.tile)
            }
            VIEW_MODE_GRIDVIEW -> {
                menu.findItem(R.id.menu_switcher)
                    ?.setIcon(R.drawable.list)
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        currentViewMode = sharedCurrentViewMode(context!!, resources.getString(R.string.currentViewMode), currentViewMode)
        when (item.itemId) {
            R.id.menu_switcher -> {

                when(currentViewMode){
                    VIEW_MODE_GRIDVIEW -> {
                        editorSharedPreference(context!!, resources.getString(R.string.currentViewMode), VIEW_MODE_LISTVIEW)
                        recycler_view.layoutManager = LinearLayoutManager(activity)
                        item.setIcon(R.drawable.tile)
                    }
                    VIEW_MODE_LISTVIEW -> {
                        editorSharedPreference(context!!, resources.getString(R.string.currentViewMode), VIEW_MODE_GRIDVIEW)
                        recycler_view.layoutManager = GridLayoutManager(activity, 2)
                        item.setIcon(R.drawable.list)
                    }
                }
                recycler_view.adapter?.notifyDataSetChanged()
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
        currentViewMode = sharedCurrentViewMode(context!!, resources.getString(R.string.currentViewMode), currentViewMode)
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

        currentViewMode = sharedCurrentViewMode(context!!, resources.getString(R.string.currentViewMode), currentViewMode)
        if (recycler_view.adapter == null) {
            when(currentViewMode){
                VIEW_MODE_LISTVIEW -> {recycler_view.layoutManager = LinearLayoutManager(activity)}
                VIEW_MODE_GRIDVIEW -> {recycler_view.layoutManager = GridLayoutManager(activity, 2)}
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
                            adapterBest =
                                adapterFun(list) as PagingAdapterViewHolder
                            adapterBest.submitList(list)
                            recycler_view.adapter = adapterBest
                        })

                }
                false -> {
                    recycler_view.apply {
                        when (currentViewMode) {
                            VIEW_MODE_LISTVIEW -> {
                                adapter = adapterOffline(
                                    CardDataBase.getDatabase(context!!)?.getCardDao()?.all!!)
                                layoutManager = LinearLayoutManager(activity)
                            }
                            VIEW_MODE_GRIDVIEW -> {
                                adapter = adapterOffline(
                                    CardDataBase.getDatabase(context!!)?.getCardDao()?.all!!)
                                layoutManager = GridLayoutManager(activity, 2)
                            }
                        }
                    }

                }
            }
        }

    }

    private fun adapterFun(
        list: MutableList<CardBinding>): PagedListAdapter<CardBinding, PagingAdapterViewHolder.ViewHolder> {

        return PagingAdapterViewHolder(object : InClick {
            override fun onItemClick(item: CardBinding, position: Int) {
                NaviController(context!!).toDetailsFromBest(item)
            }
        }, object : BookmarkClick {
            override fun setPosition(position: Int) {
                if (ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.getById(list[position].id!!) == null) {
                    ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.insert(
                        ProjectsBinding(
                            list[position].id!!, list[position].bigImage, list[position].thumbnail,
                            list[position].avatar, list[position].artistName,
                            list[position].artName, list[position].views,
                            list[position].appreciations, list[position].comments,
                            list[position].username, list[position].published, list[position].url,
                            CurrentDate.getCurrentDateTime().toString()
                        )
                    )
                } else {
                    ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()
                        ?.deleteById(list[position].id!!)
                }
            }
        }, resources.getString(R.string.best))
    }

    private fun adapterOffline(list: MutableList<CardBinding>): AdapterNonPaging {

        return AdapterNonPaging(list, object : InClick {
            override fun onItemClick(item: CardBinding, position: Int) {}

        }, object : BookmarkClick {
            override fun setPosition(position: Int) {}
        }, resources.getString(R.string.best))
    }

}