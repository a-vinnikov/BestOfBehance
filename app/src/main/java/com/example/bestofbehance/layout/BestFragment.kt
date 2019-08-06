package com.example.bestofbehance.layout

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
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
import com.example.bestofbehance.room.CardDataBase
import com.example.bestofbehance.room.DBMain
import com.example.bestofbehance.viewModels.*
import kotlinx.android.synthetic.main.list_item.*


class Best : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var jsonModel: VMForParse

    val VIEW_MODE_LISTVIEW = 0
    val VIEW_MODE_GRIDVIEW = 1
    var currentViewMode = 0
    var page = 1
    lateinit var adapterAbc: AdapterViewHolder


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(com.example.bestofbehance.R.menu.one_button_toolbar, menu)
        if (currentViewMode == 0) {
            menu.findItem(com.example.bestofbehance.R.id.menu_switcher)
                ?.setIcon(com.example.bestofbehance.R.drawable.list)
        } else {
            menu.findItem(com.example.bestofbehance.R.id.menu_switcher)
                ?.setIcon(com.example.bestofbehance.R.drawable.tile)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        sharedCurrentViewMode()
        val editor = activity?.getSharedPreferences("ViewMode", AppCompatActivity.MODE_PRIVATE)?.edit()
        when (item.itemId) {
            com.example.bestofbehance.R.id.menu_switcher -> {
                if (currentViewMode == 1) {
                    item.setIcon(com.example.bestofbehance.R.drawable.list)
                    createRecyclerView(VIEW_MODE_LISTVIEW)
                    editor?.putInt("currentViewMode", VIEW_MODE_LISTVIEW)
                    editor?.apply()
                } else {
                    item.setIcon(com.example.bestofbehance.R.drawable.tile)
                    createRecyclerView(VIEW_MODE_GRIDVIEW)
                    editor?.putInt("currentViewMode", VIEW_MODE_GRIDVIEW)
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

    }

    override fun onResume() {
        super.onResume()
        activity?.navigation?.menu?.findItem(com.example.bestofbehance.R.id.best)?.isChecked = true
    }

    private fun createRecyclerView(currentViewMode: Int) {
        if (jsonModel.recList.value == null || swipe.isRefreshing) {
            jsonModel.setGeneral(page)
        }

        /*val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()*/

        val observerGSON = Observer<MutableList<CardBinding>> {
            // val pagedList = PagedList.Builder<Any, Any>(dataSource, config).setBackgroundThreadExecutor(Executors.newSingleThreadExecutor()).setMainThreadExecutor(MainThreadExecutor()).build()

            //CardDataBase.getDatabase(context!!)

            adapterAbc = if (currentViewMode == 0) {
                adapterFun(it, 0)
            } else {
                adapterFun(it, 1)
            }
            recycler_view.adapter = adapterAbc
        }
        jsonModel.recList.observe(this, observerGSON)


        if (currentViewMode == 0) {
            recycler_view.layoutManager = LinearLayoutManager(activity)
        } else {
            recycler_view.layoutManager = GridLayoutManager(activity, 2)
        }
        //recycler_view.layoutManager?.scrollToPosition(position)
    }

    fun sharedCurrentViewMode() {
        val sharedPreference = activity?.getSharedPreferences("ViewMode", AppCompatActivity.MODE_PRIVATE)
        currentViewMode = sharedPreference!!.getInt("currentViewMode", currentViewMode)
    }

    fun sharedPosition(position: Int) {
        val editor = activity?.getSharedPreferences("ViewMode", AppCompatActivity.MODE_PRIVATE)?.edit()
        editor?.putInt("position", position)
        editor?.apply()
    }

    private fun adapterFun(list: MutableList<CardBinding>, ViewMode: Int): AdapterViewHolder {

        return AdapterViewHolder(recycler_view, list, ViewMode, object : InClick {
            override fun onItemClick(item: CardBinding) {
                NaviController(activity).toDetails(item)
            }
        }, object : BookmarkClick{
            override fun setPosition(position: Int) {
                DBMain.add(list[position], context!!)
                DBMain.read(context!!)
            }

        })
    }

}