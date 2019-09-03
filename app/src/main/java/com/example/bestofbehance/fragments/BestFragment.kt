package com.example.bestofbehance.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
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
import com.example.bestofbehance.databases.SharedPreferenceObject.editorSharedPreference
import com.example.bestofbehance.databases.SharedPreferenceObject.sharedCurrentViewMode
import com.example.bestofbehance.databases.forRoom.CardDataBase
import com.example.bestofbehance.databases.forRoom.ProjectsDataBase
import com.example.bestofbehance.viewModels.*
import com.example.bestofbehance.viewModels.ConnectChecking.isOnline
import java.text.SimpleDateFormat
import java.util.*






const val VIEW_MODE_LISTVIEW = "list"
const val VIEW_MODE_GRIDVIEW = "tile"

class Best : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var jsonModel: VMForParse
    private var currentViewMode = "list"
    var page = 1
    lateinit var adapterBest: PagingAdapterViewHolder
    private var isLoading = false
    private val isLastPage = false

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.one_button_toolbar, menu)

        when (currentViewMode) {
            "list" -> {
                menu.findItem(R.id.menu_switcher)
                    ?.setIcon(R.drawable.list)
            }
            "tile" -> {
                menu.findItem(R.id.menu_switcher)
                    ?.setIcon(R.drawable.tile)
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        currentViewMode = sharedCurrentViewMode(context!!, "currentViewMode", currentViewMode)
        when (item.itemId) {
            R.id.menu_switcher -> {
                if (currentViewMode == "tile") {
                    item.setIcon(R.drawable.list)
                    createRecyclerView(VIEW_MODE_LISTVIEW)
                    editorSharedPreference(context!!, "currentViewMode", VIEW_MODE_LISTVIEW)
                } else if (currentViewMode == "list") {
                    item.setIcon(R.drawable.tile)
                    createRecyclerView(VIEW_MODE_GRIDVIEW)
                    editorSharedPreference(context!!, "currentViewMode", VIEW_MODE_GRIDVIEW)
                }
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
        currentViewMode = sharedCurrentViewMode(context!!, "currentViewMode", currentViewMode)
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
        jsonModel = ViewModelProviders.of(this, ViewModelFactory()).get(VMForParse::class.java)

        currentViewMode = sharedCurrentViewMode(context!!, "currentViewMode", currentViewMode)
        if (recycler_view.adapter == null) {
            createRecyclerView(currentViewMode)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.navigation?.menu?.findItem(R.id.best)?.isChecked = true
    }

    private fun createRecyclerView(currentViewMode: String) {

        val connectionLiveData = ConnectionLiveData(context!!)
        connectionLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { isConnected ->
            isConnected?.let {
                when (it){
                    true -> {if (swipe.isRefreshing || jsonModel.itemPagedList?.value?.size == null){
                        jsonModel.setGeneral()
                        jsonModel.setDBGeneral(1)
                    }
                        when (currentViewMode) {
                            "list" -> {
                                recycler_view.layoutManager = LinearLayoutManager(activity)
                                jsonModel.itemPagedList?.observe(viewLifecycleOwner,
                                    Observer<PagedList<CardBinding>> { list ->
                                        adapterBest = adapterFun(list, "list") as PagingAdapterViewHolder
                                        adapterBest.submitList(list)
                                        recycler_view.adapter = adapterBest
                                    })
                            }
                            "tile" -> {
                                recycler_view.layoutManager = GridLayoutManager(activity, 2)
                                jsonModel.itemPagedList?.observe(viewLifecycleOwner,
                                    Observer<PagedList<CardBinding>> { list ->
                                        adapterBest = adapterFun(list, "tile") as PagingAdapterViewHolder
                                        adapterBest.submitList(list)
                                        recycler_view.adapter = adapterBest
                                    })
                            }
                        }

                        val observerGSON = Observer<MutableList<CardBinding>> { list ->

                            CardDataBase.getDatabase(context!!)?.getCardDao()?.deleteAll()
                            for (i in 0 until list.size) {
                                CardDataBase.getDatabase(context!!)?.getCardDao()?.insert(list[i])
                            }
                        }
                        jsonModel.mainContentList.observe(viewLifecycleOwner, observerGSON)

                    }
                    false -> {
                        recycler_view.apply {
                            when (currentViewMode) {
                            "list" -> {
                                adapter = adapterOffline(CardDataBase.getDatabase(context!!)?.getCardDao()?.all!!, "list")
                                layoutManager = LinearLayoutManager(activity) }
                            "tile" -> {
                                adapter = adapterOffline(CardDataBase.getDatabase(context!!)?.getCardDao()?.all!!, "tile")
                                layoutManager = GridLayoutManager(activity, 2) }
                        } }

                    }
                }
            }
        })

    }

    private fun adapterFun(
        list: MutableList<CardBinding>,
        viewMode: String
    ): PagedListAdapter<CardBinding, PagingAdapterViewHolder.ViewHolder> {

        return PagingAdapterViewHolder(viewMode, object : InClick {
            override fun onItemClick(item: CardBinding, position: Int) {
                NaviController(context!!).toDetailsFromBest(item)
            }
        }, object : BookmarkClick {
            override fun setPosition(position: Int) {
                if (ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.getById(list[position].id!!) == null) {
                    ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.insert(
                        ProjectsBinding(list[position].id!!, list[position].bigImage, list[position].thumbnail,
                            list[position].avatar, list[position].artistName,
                            list[position].artName, list[position].views,
                            list[position].appreciations, list[position].comments,
                            list[position].username, list[position].published, list[position].url,
                            getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss"))
                    )
                } else {
                    ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.deleteById(list[position].id!!)
                }
            }
        }, "Best")
    }

    fun adapterOffline(list: MutableList<CardBinding>, viewMode: String): AdapterNonPaging {

        return AdapterNonPaging(list, viewMode, object : InClick {
            override fun onItemClick(item: CardBinding, position: Int) {}

        }, object : BookmarkClick {
            override fun setPosition(position: Int) {} }, "Best")
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

}