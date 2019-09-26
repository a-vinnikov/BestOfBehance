package com.example.bestofbehance.fragment

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bestofbehance.R
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.classesToSupport.BookmarkClick
import com.example.bestofbehance.classesToSupport.InClick
import com.example.bestofbehance.module.FragmentNavigate
import com.example.bestofbehance.module.StorageModule
import com.example.bestofbehance.adapter.AdapterProjects
import com.example.bestofbehance.classesToSupport.VIEW_MODE_GRIDVIEW
import com.example.bestofbehance.classesToSupport.VIEW_MODE_LISTVIEW
import com.example.bestofbehance.extension.Converter
import com.example.bestofbehance.viewModel.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_projects.*

class ProjectsFragment : Fragment() {

    private var currentViewMode = VIEW_MODE_LISTVIEW

    lateinit var jsonModel: ViewModelForParse
    lateinit var adapterProjects: AdapterProjects


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.one_button_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)

        when (currentViewMode) {
            VIEW_MODE_LISTVIEW -> {
                menu.findItem(R.id.menu_switcher)?.setIcon(R.drawable.ic_tile)
            }
            VIEW_MODE_GRIDVIEW -> {
                menu.findItem(R.id.menu_switcher)?.setIcon(R.drawable.ic_list)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        currentViewMode = StorageModule.getPreferences(context!!, resources.getString(R.string.current_view_mode_projects), currentViewMode)
        when (item.itemId) {
            R.id.menu_switcher -> {
                if (currentViewMode == VIEW_MODE_GRIDVIEW) {
                    item.setIcon(R.drawable.ic_tile)
                    StorageModule.editorPreferences(context!!, resources.getString(R.string.current_view_mode_projects), VIEW_MODE_LISTVIEW)
                    recyclerViewProjects.layoutManager = LinearLayoutManager(activity)
                } else if (currentViewMode == VIEW_MODE_LISTVIEW) {
                    item.setIcon(R.drawable.ic_list)
                    StorageModule.editorPreferences(context!!, resources.getString(R.string.current_view_mode_projects), VIEW_MODE_GRIDVIEW)
                    recyclerViewProjects.layoutManager = GridLayoutManager(activity, 2)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_projects, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jsonModel = ViewModelProviders.of(this, ViewModelFactory(context!!)).get(ViewModelForParse::class.java)
        currentViewMode = StorageModule.getPreferences(context!!, resources.getString(R.string.current_view_mode_projects), currentViewMode)

        if (recyclerViewProjects.adapter == null) {
            when (currentViewMode) {
                VIEW_MODE_LISTVIEW -> {
                    recyclerViewProjects.layoutManager = LinearLayoutManager(activity)
                }
                VIEW_MODE_GRIDVIEW -> {
                    recyclerViewProjects.layoutManager = GridLayoutManager(activity, 2)
                }
            }
            createRecyclerView()
        }
    }

    override fun onResume() {
        super.onResume()
            if (jsonModel.getAllFromProjectsDB()?.size != 0) {
                textProjects.visibility = GONE
            } else {
                textProjects.visibility = VISIBLE
            }
        activity?.navigation?.menu?.findItem(R.id.projects)?.isChecked = true
    }

    private fun createRecyclerView() {

        jsonModel.getAllFromProjectsDB()?.let { Converter.convertProjectsToCard(it){ result -> adapterProjects = adapterFun(result)} }
        recyclerViewProjects.adapter = adapterProjects

    }

    fun adapterFun(list: MutableList<CardBinding>): AdapterProjects {

        return AdapterProjects(list, object : InClick {
            override fun onItemClick(item: CardBinding, position: Int) {
                FragmentNavigate(context!!).toDetailsFromProjects(item)
            }

        }, object : BookmarkClick {
            override fun setPosition(position: Int) {
                if (jsonModel.getByIdFromProjectsDB(list[position].id!!) != null) {
                    jsonModel.deleteByIdFromProjectsDB(list[position].id!!)
                    adapterProjects.list.removeAt(position)
                    adapterProjects.notifyDataSetChanged()
                        if (jsonModel.getAllFromProjectsDB()?.size == 0) {
                            textProjects.visibility = VISIBLE
                        }
                }
            }
        })
    }
}
