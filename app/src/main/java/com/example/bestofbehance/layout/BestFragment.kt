package com.example.bestofbehance.layout

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
import com.example.bestofbehance.R
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.databases.CacheDBMain
import com.example.bestofbehance.paging.PaginationScrollListener
import com.example.bestofbehance.databases.DBMain
import com.example.bestofbehance.databases.SharedPreferenceForFragments.editorSharedPreference
import com.example.bestofbehance.databases.SharedPreferenceForFragments.sharedCurrentViewMode
import com.example.bestofbehance.viewModels.*
import timber.log.Timber


const val VIEW_MODE_LISTVIEW = "list"
const val VIEW_MODE_GRIDVIEW = "tile"

class Best : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var jsonModel: VMForParse

    private var currentViewMode = "list"
    var page = 1
    lateinit var adapterBest: AdapterViewHolder

    private var isLoading = false
    private val isLastPage = false

    init {
        Timber.plant(Timber.DebugTree())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.one_button_toolbar, menu)

        when (currentViewMode) {
            "list" -> {
                menu.findItem(R.id.menu_switcher)?.setIcon(R.drawable.list)
            }
            "tile" -> {
                menu.findItem(R.id.menu_switcher)?.setIcon(R.drawable.tile)
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
                    editorSharedPreference(context!!,"currentViewMode", VIEW_MODE_GRIDVIEW)
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
        CacheDBMain.clear(context!!)
        currentViewMode = sharedCurrentViewMode(context!!, "currentViewMode", currentViewMode)
        createRecyclerView(currentViewMode)
        swipe.isRefreshing = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

        recycler_view.addOnScrollListener(object : PaginationScrollListener(LinearLayoutManager(context)) {
            override fun getTotalPageCount(): Int {
                //return TOTAL_PAGES
                return 0
            }

            override fun isLastPage(): Boolean {

                if (adapterBest.position == (adapterBest.list.size - 1)) {
                    isLoading = true
                    page += 1
                    jsonModel.setNextPage(context!!, page)
                    val observerGSONPaging = Observer<MutableList<CardBinding>> {
                        adapterBest.addData(it)
                        isLoading = false
                        Timber.d("Loaded page $page")
                    }
                    jsonModel.pagingResponseList.observe(this@Best, observerGSONPaging)
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
        activity?.navigation?.menu?.findItem(R.id.best)?.isChecked = true
    }

    override fun onDestroy() {
        super.onDestroy()
        DBMain.close(context!!)
    }

    private fun createRecyclerView(currentViewMode: String) {
        if (jsonModel.mainContentList.value == null || swipe.isRefreshing) {
            jsonModel.setGeneral(context!!, page)
        }

        /*val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()*/



        val observerGSON = Observer<MutableList<CardBinding>> { list ->
            // val pagedList = PagedList.Builder<Any, Any>(dataSource, config).setBackgroundThreadExecutor(Executors.newSingleThreadExecutor()).setMainThreadExecutor(MainThreadExecutor()).build()

            when (currentViewMode) {
                "list" -> {
                    adapterBest = adapterFun(list, "list")
                }
                "tile" -> {
                    adapterBest = adapterFun(list, "tile")
                }
            }
            recycler_view.adapter = adapterBest

            for(i in 0 until list.size){
                if(DBMain.find(context!!, list[i].id) != null){
                    DBMain.update(context!!, list[i])
                }
            }
        }
        jsonModel.mainContentList.observe(this, observerGSON)

        when (currentViewMode) {
            "list" -> { recycler_view.layoutManager = LinearLayoutManager(activity) }
            "tile" -> { recycler_view.layoutManager = GridLayoutManager(activity, 2) }
        }

    }

    private fun adapterFun(list: MutableList<CardBinding>, viewMode: String): AdapterViewHolder {

        return AdapterViewHolder(list, viewMode, object : InClick {
            override fun onItemClick(item: CardBinding, position: Int) {
                NaviController(context!!).toDetailsFromBest(item)
            }
        }, object : BookmarkClick {
            override fun setPosition(position: Int) {
                if (DBMain.find(context!!, list[position].id) == null) {
                    DBMain.add(context!!, list[position])
                } else {
                    DBMain.delete(context!!, list[position].id)
                }
            }
        }, "Best")
    }

}