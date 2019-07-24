package com.example.bestofbehance.layout

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.fragment_best.*
import kotlinx.android.synthetic.main.activity_main.*
import android.view.MenuInflater
import com.example.bestofbehance.gson.CardBinding
import com.example.bestofbehance.R
import com.example.bestofbehance.viewModels.*


class Best : Fragment(), SwipeRefreshLayout.OnRefreshListener{

    lateinit var jsonModel: ParseInVM

    val VIEW_MODE_LISTVIEW = 0
    val VIEW_MODE_GRIDVIEW = 1
    var currentViewMode = 0
    var position = 0


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.one_button_toolbar, menu)
        if (currentViewMode == 0) {
            menu.findItem(R.id.menu_switcher)?.setIcon(R.drawable.list)
        } else {
            menu.findItem(R.id.menu_switcher)?.setIcon(R.drawable.tile)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        sharedCurrentViewMode()
        val editor = activity?.getSharedPreferences("ViewMode", AppCompatActivity.MODE_PRIVATE)?.edit()
        when (item.itemId) {
            R.id.menu_switcher -> {
                if (currentViewMode == 1) {
                    item.setIcon(R.drawable.list)
                    createRecyclerView(VIEW_MODE_LISTVIEW)
                    editor?.putInt("currentViewMode", VIEW_MODE_LISTVIEW)
                    editor?.apply()
                } else {
                    item.setIcon(R.drawable.tile)
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
        return inflater.inflate(R.layout.fragment_best, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe.setOnRefreshListener(this)
        jsonModel = ViewModelProviders.of(this, ViewModelFactory()).get(ParseInVM::class.java)

        sharedCurrentViewMode()
        if (recycler_view.adapter == null) {
            createRecyclerView(currentViewMode)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.navigation?.menu?.findItem(R.id.best)?.isChecked = true
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