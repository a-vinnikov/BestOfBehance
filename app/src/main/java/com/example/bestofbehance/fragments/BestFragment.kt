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
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.runBlocking
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
        inflater.inflate(com.example.bestofbehance.R.menu.one_button_toolbar, menu)

        when (currentViewMode) {
            "list" -> {
                menu.findItem(com.example.bestofbehance.R.id.menu_switcher)
                    ?.setIcon(com.example.bestofbehance.R.drawable.list)
            }
            "tile" -> {
                menu.findItem(com.example.bestofbehance.R.id.menu_switcher)
                    ?.setIcon(com.example.bestofbehance.R.drawable.tile)
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

    }

    override fun onResume() {
        super.onResume()
        activity?.navigation?.menu?.findItem(com.example.bestofbehance.R.id.best)?.isChecked = true
    }

    private fun createRecyclerView(currentViewMode: String) {
        if (isOnline(context!!) == true && isOnline(context!!) != null || jsonModel.itemPagedList == null) {
            jsonModel.setGeneral()
        } else if (isOnline(context!!) == null || isOnline(context!!) == false) {
            //Snackbar.make(snack, "No internet connection", Snackbar.LENGTH_SHORT).setAction("No internet connection", null).show()
            //DBBestCacheDao.read(context!!) { jsonModel.mainContentList.postValue(it) }
            jsonModel.mainContentList.postValue(CardDataBase.getDatabase(context!!)?.getCardDao()?.all)
        }

        when (currentViewMode) {
            "list" -> {
                recycler_view.layoutManager = LinearLayoutManager(activity)
                jsonModel.itemPagedList?.observe(this,
                    Observer<PagedList<CardBinding>> {
                        adapterBest = adapterFun(it, "list") as PagingAdapterViewHolder
                        adapterBest.submitList(it)
                        recycler_view.adapter = adapterBest
                    })
            }
            "tile" -> {
                recycler_view.layoutManager = GridLayoutManager(activity, 2)
                jsonModel.itemPagedList?.observe(this,
                    Observer<PagedList<CardBinding>> {
                        adapterBest = adapterFun(it, "tile") as PagingAdapterViewHolder
                        adapterBest.submitList(it)
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
        jsonModel.mainContentList.observe(this, observerGSON)

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
                if (ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.getById(list[position].id) == null) {
                    ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.insert(
                        ProjectsBinding(list[position].id, list[position].bigImage,
                            list[position].avatar, list[position].artistName,
                            list[position].artName, list[position].views,
                            list[position].appreciations, list[position].comments,
                            list[position].username, list[position].published,
                            getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss"))
                    )
                } else {
                    ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.deleteById(list[position].id)
                }
            }
        }, "Best")
    }

    private fun isOnline(context: Context): Boolean? {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

}