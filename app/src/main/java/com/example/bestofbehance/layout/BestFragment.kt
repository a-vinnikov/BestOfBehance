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
import com.example.bestofbehance.viewModels.*


class Best : Fragment(), SwipeRefreshLayout.OnRefreshListener{

    lateinit var jsonModel: ParseInVM

    val VIEW_MODE_LISTVIEW = 0
    val VIEW_MODE_GRIDVIEW = 1
    var currentViewMode = 0
    var position = 0
    private val PAGE_START = 1
    private val CURRENT_PAGE = PAGE_START
    private var isLoading = false
    private val isLastPage = false


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(com.example.bestofbehance.R.menu.one_button_toolbar, menu)
        if (currentViewMode == 0) {
            menu.findItem(com.example.bestofbehance.R.id.menu_switcher)?.setIcon(com.example.bestofbehance.R.drawable.list)
        } else {
            menu.findItem(com.example.bestofbehance.R.id.menu_switcher)?.setIcon(com.example.bestofbehance.R.drawable.tile)
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
        jsonModel = ViewModelProviders.of(this, ViewModelFactory()).get(ParseInVM::class.java)

        sharedCurrentViewMode()
        if (recycler_view.adapter == null) {
            createRecyclerView(currentViewMode)
        }


        /*recycler_view.addOnScrollListener(object : PaginationScrollListener(LinearLayoutManager(activity)) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                getMoreItems()
            }
        })*/

    }

    /*fun getMoreItems() {
        isLoading = false
        jsonModel.setGeneral()
        recycler_view.adapter.addData(list)
    }*/


    override fun onResume() {
        super.onResume()
        activity?.navigation?.menu?.findItem(com.example.bestofbehance.R.id.best)?.isChecked = true
    }

    fun createRecyclerView(currentViewMode: Int) {
        if (jsonModel.recList.value == null || swipe.isRefreshing) {
            jsonModel.setGeneral()
        }

        val observerGSON = Observer<MutableList<CardBinding>> {
            if (currentViewMode == 0) {
                recycler_view.adapter = AdapterViewHolder(it!!, 0, object : InClick {
                    override fun setPosition(position: Int) {
                        sharedPosition(position)
                    }
                    override fun onItemClick(item: CardBinding) {
                        NaviController(activity).toDetails(item) } })
            } else {
                recycler_view.adapter = AdapterViewHolder(it!!, 1, object : InClick {
                    override fun setPosition(position: Int) {
                        sharedPosition(position)
                    }
                    override fun onItemClick(item: CardBinding) {
                        NaviController(activity).toDetails(item) } })
            }
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
}