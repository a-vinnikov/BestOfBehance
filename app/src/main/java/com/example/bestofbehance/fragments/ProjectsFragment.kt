package com.example.bestofbehance.fragments

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
import com.example.bestofbehance.databases.SharedPreferenceObject
import com.example.bestofbehance.databases.forRoom.ProjectsDataBase
import com.example.bestofbehance.viewModels.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_projects.*

class ProjectsFragment : Fragment() {

    private var currentViewMode = "list"

    lateinit var jsonModel: VMForParse
    lateinit var adapterProjects: AdapterNonPaging


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
        currentViewMode = SharedPreferenceObject.sharedCurrentViewMode(context!!, "currentViewModeProjects", currentViewMode)
        when (item.itemId) {
            R.id.menu_switcher -> {
                if (currentViewMode == "tile") {
                    item.setIcon(R.drawable.list)
                    createRecyclerView(VIEW_MODE_LISTVIEW)
                    SharedPreferenceObject.editorSharedPreference(context!!, "currentViewModeProjects", VIEW_MODE_LISTVIEW)
                } else if (currentViewMode == "list") {
                    item.setIcon(R.drawable.tile)
                    createRecyclerView(VIEW_MODE_GRIDVIEW)
                    SharedPreferenceObject.editorSharedPreference(context!!, "currentViewModeProjects", VIEW_MODE_GRIDVIEW)
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
        currentViewMode = SharedPreferenceObject.sharedCurrentViewMode(context!!, "currentViewModeProjects", currentViewMode)

        if (recycler_view_projects.adapter == null) {
            createRecyclerView(currentViewMode)
        }
    }

    override fun onResume() {
        super.onResume()
            if (ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.all?.size != 0) {
                text_projects.visibility = GONE
            } else {
                text_projects.visibility = VISIBLE
            }
        activity?.navigation?.menu?.findItem(R.id.projects)?.isChecked = true
    }

    private fun createRecyclerView(currentViewMode: String) {

        when (currentViewMode) {
            "list" -> {
                //DBProjectsDao.read(context!!) { result -> adapterProjects = adapterFun(result, "list") }
                convertProjectsToCard{result -> adapterProjects = adapterFun(result, "list")}

            }
            "tile" -> {
                //DBProjectsDao.read(context!!) { result -> adapterProjects = adapterFun(result, "tile") }
                convertProjectsToCard{result -> adapterProjects = adapterFun(result, "tile")}
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

    fun adapterFun(list: MutableList<CardBinding>, viewMode: String): AdapterNonPaging {

        return AdapterNonPaging(list, viewMode, object : InClick {
            override fun onItemClick(item: CardBinding, position: Int) {
                NaviController(context!!).toDetailsFromProjects(item)
            }

        }, object : BookmarkClick {
            override fun setPosition(position: Int) {
                if (ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.getById(list[position].id) != null) {
                    ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.deleteById(list[position].id)
                    adapterProjects.list.removeAt(position)
                    adapterProjects.notifyDataSetChanged()
                        if (ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.all?.size == 0) {
                            text_projects.visibility = VISIBLE
                        }
                }
            }
        }, "Projects")
    }

    private fun convertProjectsToCard(myCallBack: (result: MutableList<CardBinding>) -> Unit){
        val list = ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.all
        val listCard: MutableList<CardBinding> = mutableListOf()
        for (i in 0 until list!!.size){
            listCard.add(CardBinding(list[i].id, list[i].bigImage, list[i].avatar, list[i].artistName, list[i].artName, list[i].views, list[i].appreciations, list[i].comments, list[i].username, list[i].published))
        }
        myCallBack.invoke(listCard)
    }
}