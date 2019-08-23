package com.example.bestofbehance.layout

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
import android.widget.Toast
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.databases.DBProjectsDao
import com.example.bestofbehance.databases.SharedPreferenceObject.editorSharedPreference
import com.example.bestofbehance.databases.SharedPreferenceObject.sharedCurrentViewMode
import com.example.bestofbehance.databases.forRoom.CardDataBase
import com.example.bestofbehance.paging.ItemSourceFactory
import com.example.bestofbehance.viewModels.*
import kotlinx.coroutines.Deferred
import timber.log.Timber
import java.util.concurrent.Executors
import com.example.bestofbehance.paging.ItemSourceFactory.Companion.providePagingConfig
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking


const val VIEW_MODE_LISTVIEW = "list"
const val VIEW_MODE_GRIDVIEW = "tile"

class Best : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var jsonModel: VMForParse
    private var currentViewMode = "list"
    var page = 1
    lateinit var adapterBest: PagingAdapterViewHolder
    private var isLoading = false
    private val isLastPage = false

    init {
        Timber.plant(Timber.DebugTree())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(com.example.bestofbehance.R.menu.one_button_toolbar, menu)

        when (currentViewMode) {
            "list" -> {
                menu.findItem(com.example.bestofbehance.R.id.menu_switcher)?.setIcon(com.example.bestofbehance.R.drawable.list)
            }
            "tile" -> {
                menu.findItem(com.example.bestofbehance.R.id.menu_switcher)?.setIcon(com.example.bestofbehance.R.drawable.tile)
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        currentViewMode = sharedCurrentViewMode(context!!, "currentViewMode", currentViewMode)
        when (item.itemId) {
            com.example.bestofbehance.R.id.menu_switcher -> {
                if (currentViewMode == "tile") {
                    item.setIcon(com.example.bestofbehance.R.drawable.list)
                    createRecyclerView(VIEW_MODE_LISTVIEW)
                    editorSharedPreference(context!!, "currentViewMode", VIEW_MODE_LISTVIEW)
                } else if (currentViewMode == "list") {
                    item.setIcon(com.example.bestofbehance.R.drawable.tile)
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
        DBProjectsDao.close(context!!)
        currentViewMode = sharedCurrentViewMode(context!!, "currentViewMode", currentViewMode)
        createRecyclerView(currentViewMode)
        swipe.isRefreshing = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.example.bestofbehance.R.layout.fragment_best, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe.setOnRefreshListener(this)
        jsonModel = ViewModelProviders.of(this, ViewModelFactory()).get(VMForParse::class.java)

        currentViewMode = sharedCurrentViewMode(context!!, "currentViewMode", currentViewMode)
        if (recycler_view.adapter == null) {
            createRecyclerView(currentViewMode)
        }

        /*recycler_view.addOnScrollListener(object :
            PaginationScrollListener(LinearLayoutManager(context)) {
            override fun getTotalPageCount(): Int {
                //return TOTAL_PAGES
                return 0
            }

            override fun isLastPage(): Boolean {

                if (adapterBest.position == (adapterBest.list.size - 1)) {
                    isLoading = true
                    page += 1
                    jsonModel.setNextPage(page)
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
        })*/

    }

    override fun onResume() {
        super.onResume()
        activity?.navigation?.menu?.findItem(com.example.bestofbehance.R.id.best)?.isChecked = true
    }

    override fun onDestroy() {
        super.onDestroy()
        DBProjectsDao.close(context!!)
    }

    private fun createRecyclerView(currentViewMode: String) {
        if (isOnline(context!!) == true && isOnline(context!!) != null) {
            jsonModel.setGeneral(page)
        } else if (isOnline(context!!) == null || isOnline(context!!) == false) {
            Toast.makeText(context!!, "No internet connection", Toast.LENGTH_LONG).show()
            //DBBestCacheDao.read(context!!) { jsonModel.mainContentList.postValue(it) }
            jsonModel.mainContentList.postValue(CardDataBase.getDatabase(context!!)?.getCardDao()?.all)
        }

        val observerGSON = Observer<MutableList<CardBinding>> { list ->
            // val pagedList = PagedList.Builder<Any, Any>(dataSource, config).setBackgroundThreadExecutor(Executors.newSingleThreadExecutor()).setMainThreadExecutor(MainThreadExecutor()).build()
            //CacheDBMain.read(context!!) { list ->

            CardDataBase.getDatabase(context!!)?.getCardDao()?.deleteAll()
            for (i in 0 until list.size) {
                CardDataBase.getDatabase(context!!)?.getCardDao()?.insert(list[i])
            }

            when (currentViewMode) {
                "list" -> {
                    runBlocking {
                        abc(list, "list")
                        //recycler_view.adapter = adapterFun(list, "list")
                    }
                }
                "tile" -> {
                    runBlocking {
                        abc(list, "tile")
                        //recycler_view.adapter = adapterFun(list, "tile")

                    }
                }
            }


            when (currentViewMode) {
                "list" -> {
                    adapterBest = adapterFun(list, "list") as PagingAdapterViewHolder
                }
                "tile" -> {
                    adapterBest = adapterFun(list, "tile") as PagingAdapterViewHolder
                }
            }
            recycler_view.adapter = adapterBest


            when (currentViewMode) {
                "list" -> {
                    recycler_view.layoutManager = LinearLayoutManager(activity)
                }
                "tile" -> {
                    recycler_view.layoutManager = GridLayoutManager(activity, 2)
                }
                /*DBBestCacheDao.clear(context!!)
            for (i in 0 until list.size) {
                DBBestCacheDao.add(context!!, list[i])
            }*/
            }


            /* for(i in 0 until list.size){
             if(DBMain.find(context!!, list[i].id) != null){
                 DBMain.update(context!!, list[i])
             }
         }*/
        }
        jsonModel.mainContentList.observe(this, observerGSON)

    }

    private fun adapterFun(list: MutableList<CardBinding>, viewMode: String): PagedListAdapter<CardBinding, PagingAdapterViewHolder.ViewHolder> {

        return PagingAdapterViewHolder(viewMode, object : InClick {
            override fun onItemClick(item: CardBinding, position: Int) {
                NaviController(context!!).toDetailsFromBest(item)
            }
        }, object : BookmarkClick {
            override fun setPosition(position: Int) {
                if (DBProjectsDao.find(context!!, list[position].id) == null) {
                    DBProjectsDao.add(context!!, list[position])
                } else {
                    DBProjectsDao.delete(context!!, list[position].id)
                }
            }
        }, "Best")
    }

    fun isOnline(context: Context): Boolean? {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected
    }

    private fun updateAdapter(asyncList: Deferred<MutableList<CardBinding>>, config: PagedList.Config, pagedListPagingAdapter: PagedListAdapter<CardBinding, PagingAdapterViewHolder.ViewHolder>) {
        val turnoverDataSourceFactory = ItemSourceFactory(asyncList) {}

        val items = PagedList.Builder<Int, CardBinding>(turnoverDataSourceFactory.create(), config)
            .setNotifyExecutor { Executors.newSingleThreadExecutor() }
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .build()

        //view!!.post(it)

        pagedListPagingAdapter.submitList(items)
    }

    suspend fun abc(list: MutableList<CardBinding>, viewMode: String) = coroutineScope {
        val cba = async { list }
        updateAdapter(cba, providePagingConfig(), adapterFun(list, viewMode))}
}