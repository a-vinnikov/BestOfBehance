package com.example.bestofbehance.layout

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
import com.example.bestofbehance.room.DBMain
import com.example.bestofbehance.room.SharedPreferenceForFragments
import com.example.bestofbehance.viewModels.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_projects.*

class ProjectsFragment : Fragment() {

    private var currentViewMode = "list"

    lateinit var jsonModel: VMForParse
    lateinit var adapterProjects: AdapterViewHolder


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.one_button_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)

        when (currentViewMode) {
            "list" -> {
                menu.findItem(R.id.menu_switcher)?.setIcon(R.drawable.list)
            }
            "tile" -> {
                menu.findItem(R.id.menu_switcher)?.setIcon(R.drawable.tile)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        currentViewMode = SharedPreferenceForFragments.sharedCurrentViewMode(context!!, "currentViewModeProjects", currentViewMode)
        when (item.itemId) {
            R.id.menu_switcher -> {
                if (currentViewMode == "tile") {
                    item.setIcon(R.drawable.list)
                    createRecyclerView(VIEW_MODE_LISTVIEW)
                    SharedPreferenceForFragments.editorSharedPreference(context!!, "currentViewModeProjects", VIEW_MODE_LISTVIEW)
                } else if (currentViewMode == "list") {
                    item.setIcon(R.drawable.tile)
                    createRecyclerView(VIEW_MODE_GRIDVIEW)
                    SharedPreferenceForFragments.editorSharedPreference(context!!, "currentViewModeProjects", VIEW_MODE_GRIDVIEW)
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

        jsonModel = ViewModelProviders.of(this, ViewModelFactory()).get(VMForParse::class.java)
        currentViewMode = SharedPreferenceForFragments.sharedCurrentViewMode(context!!, "currentViewModeProjects", currentViewMode)

        if (recycler_view_projects.adapter == null) {
            createRecyclerView(currentViewMode)
        }
    }

    override fun onResume() {
        super.onResume()
        DBMain.read(context!!) { result ->
            if (result.size != 0) {
                text_projects.visibility = GONE
            } else {
                text_projects.visibility = VISIBLE
            }
        }
        activity?.navigation?.menu?.findItem(R.id.projects)?.isChecked = true
    }

    override fun onDestroy() {
        super.onDestroy()
        DBMain.close(context!!)
    }

    private fun createRecyclerView(currentViewMode: String) {

        when (currentViewMode) {
            "list" -> {
                DBMain.read(context!!) { result -> adapterProjects = adapterFun(result, "list") }
            }
            "tile" -> {
                DBMain.read(context!!) { result -> adapterProjects = adapterFun(result, "tile") }
            }
        }

        recycler_view_projects.adapter = adapterProjects

        when (currentViewMode) {
            "list" -> {
                recycler_view_projects.layoutManager = LinearLayoutManager(activity)
            }
            "tile" -> {
                recycler_view_projects.layoutManager = GridLayoutManager(activity, 2)
            }
        }
    }

    private fun adapterFun(list: MutableList<CardBinding>, viewMode: String): AdapterViewHolder {

        return AdapterViewHolder(list, viewMode, object : InClick {
            override fun onItemClick(item: CardBinding) {
                NaviController(context!!).toDetailsFromProjects(item)
            }
        }, object : BookmarkClick {
            override fun setPosition(position: Int) {
                if (DBMain.find(context!!, list[position].id) != null) {
                    DBMain.delete(context!!, list[position].id)
                    adapterProjects.list.removeAt(position)
                    adapterProjects.notifyDataSetChanged()
                    DBMain.read(context!!) { result ->
                        if (result.size == 0) {
                            text_projects.visibility = VISIBLE
                        }
                    }
                }
            }
        })
    }

}
