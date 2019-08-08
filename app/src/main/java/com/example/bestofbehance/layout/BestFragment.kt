package com.example.bestofbehance.layout

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.fragment_best.*
import kotlinx.android.synthetic.main.activity_main.*
import android.view.MenuInflater
import com.example.bestofbehance.gson.CardBinding
import com.example.bestofbehance.paging.PaginationScrollListener
import com.example.bestofbehance.room.DBMain
import com.example.bestofbehance.viewModels.*


val VIEW_MODE_LISTVIEW = "list"
val VIEW_MODE_GRIDVIEW = "tile"

class Best : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var jsonModel: VMForParse

    var currentViewMode = "list"
    var page = 1
    lateinit var adapterBest: AdapterViewHolder

    private var isLoading = false
    private val isLastPage = false


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(com.example.bestofbehance.R.menu.one_button_toolbar, menu)
        if (currentViewMode == "list") {
            menu.findItem(com.example.bestofbehance.R.id.menu_switcher)
                ?.setIcon(com.example.bestofbehance.R.drawable.list)
        } else if (currentViewMode == "tile"){
            menu.findItem(com.example.bestofbehance.R.id.menu_switcher)
                ?.setIcon(com.example.bestofbehance.R.drawable.tile)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        sharedCurrentViewMode()
        val editor = activity?.getSharedPreferences("viewMode", AppCompatActivity.MODE_PRIVATE)?.edit()
        when (item.itemId) {
            com.example.bestofbehance.R.id.menu_switcher -> {
                if (currentViewMode == "tile") {
                    item.setIcon(com.example.bestofbehance.R.drawable.list)
                    createRecyclerView(VIEW_MODE_LISTVIEW)
                    editor?.putString("currentViewMode", VIEW_MODE_LISTVIEW)
                    editor?.apply()
                } else if (currentViewMode == "list"){
                    item.setIcon(com.example.bestofbehance.R.drawable.tile)
                    createRecyclerView(VIEW_MODE_GRIDVIEW)
                    editor?.putString("currentViewMode", VIEW_MODE_GRIDVIEW)
                    editor?.apply()
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
        DBMain.close(context!!)
        sharedCurrentViewMode()
        createRecyclerView(currentViewMode)
        swipe.isRefreshing = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.example.bestofbehance.R.layout.fragment_best, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe.setOnRefreshListener(this)
        jsonModel = ViewModelProviders.of(this, ViewModelFactory()).get(VMForParse::class.java)

        sharedCurrentViewMode()
        if (recycler_view.adapter == null) {
            createRecyclerView(currentViewMode)
        }

        recycler_view.addOnScrollListener(object : PaginationScrollListener(LinearLayoutManager(context)) {
            override fun getTotalPageCount(): Int {
                //return TOTAL_PAGES
                return 0
            }

            override fun isLastPage(): Boolean {
                if (adapterBest.position == (adapterBest.list.size - 1)) {
                    isLoading = true
                    page += 1
                    jsonModel.setNextPage(page)
                    val observerGSON0 = Observer<MutableList<CardBinding>> {
                        adapterBest.addData(it)
                        isLoading = false
                        Log.d("mLog", "Loaded page $page")
                    }
                    jsonModel.pagingResponseList.observe(this@Best, observerGSON0)
                }
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
            }
        })

    }

    override fun onResume() {
        super.onResume()
        activity?.navigation?.menu?.findItem(com.example.bestofbehance.R.id.best)?.isChecked = true
    }

    override fun onDestroy() {
        super.onDestroy()
        DBMain.close(context!!)
    }

    private fun createRecyclerView(currentViewMode: String) {
        if (jsonModel.mainContentList.value == null || swipe.isRefreshing) {
            jsonModel.setGeneral(page)
        }

        /*val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()*/

        val observerGSON = Observer<MutableList<CardBinding>> {
            // val pagedList = PagedList.Builder<Any, Any>(dataSource, config).setBackgroundThreadExecutor(Executors.newSingleThreadExecutor()).setMainThreadExecutor(MainThreadExecutor()).build()

            //CardDataBase.getDatabase(context!!)

            if (currentViewMode == "list") {
                adapterBest = adapterFun(it, "list")
            } else if (currentViewMode == "tile"){
                adapterBest = adapterFun(it, "tile")
            }
            recycler_view.adapter = adapterBest
        }
        jsonModel.mainContentList.observe(this, observerGSON)


        if (currentViewMode == "list") {
            recycler_view.layoutManager = LinearLayoutManager(activity)
        } else if(currentViewMode == "tile"){
            recycler_view.layoutManager = GridLayoutManager(activity, 2)
        }
        //recycler_view.layoutManager?.scrollToPosition(position)
    }

    fun sharedCurrentViewMode() {
        val sharedPreference = activity?.getSharedPreferences("viewMode", AppCompatActivity.MODE_PRIVATE)
        currentViewMode = sharedPreference!!.getString("currentViewMode", currentViewMode)!!
    }

    fun sharedPosition(position: Int) {
        val editor = activity?.getSharedPreferences("viewMode", AppCompatActivity.MODE_PRIVATE)?.edit()
        editor?.putInt("position", position)
        editor?.apply()
    }

    private fun adapterFun(list: MutableList<CardBinding>, viewMode: String): AdapterViewHolder {

        return AdapterViewHolder(recycler_view, list, viewMode, object : InClick {
            override fun onItemClick(item: CardBinding) {
                NaviController(activity).toDetails(item)
            }
        }, object : BookmarkClick {
            override fun setPosition(position: Int) {
                if (DBMain.find(context!!, list[position].id) == null) {
                    DBMain.add(list[position], context!!)
                    DBMain.read(context!!)
                } else {
                    DBMain.delete(context!!, list[position].id)
                }
            }
        })
    }

}