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
import com.example.bestofbehance.dagger.NavigateModule
import com.example.bestofbehance.dagger.StorageModule
import com.example.bestofbehance.database.ProjectsDataBase
import com.example.bestofbehance.adapter.AdapterProjects
import com.example.bestofbehance.binding.ProjectsBinding
import com.example.bestofbehance.viewModel.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_projects.*

class ProjectsFragment : Fragment() {

    private var currentViewMode = VIEW_MODE_LISTVIEW

    lateinit var jsonModel: VMForParse
    lateinit var adapterProjects: AdapterProjects


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.one_button_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)

        when (currentViewMode) {
            VIEW_MODE_LISTVIEW -> {
                menu.findItem(R.id.menu_switcher)?.setIcon(R.drawable.tile)
            }
            VIEW_MODE_GRIDVIEW -> {
                menu.findItem(R.id.menu_switcher)?.setIcon(R.drawable.list)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        currentViewMode = StorageModule.getPreferences(context!!, resources.getString(R.string.current_view_mode_projects), currentViewMode)
        when (item.itemId) {
            R.id.menu_switcher -> {
                if (currentViewMode == VIEW_MODE_GRIDVIEW) {
                    item.setIcon(R.drawable.tile)
                    StorageModule.editorPreferences(context!!, resources.getString(R.string.current_view_mode_projects), VIEW_MODE_LISTVIEW)
                    recyclerViewProjects.layoutManager = LinearLayoutManager(activity)
                } else if (currentViewMode == VIEW_MODE_LISTVIEW) {
                    item.setIcon(R.drawable.list)
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

        jsonModel = ViewModelProviders.of(this, ViewModelFactory(context!!)).get(VMForParse::class.java)
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
            if (ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.all?.size != 0) {
                textProjects.visibility = GONE
            } else {
                textProjects.visibility = VISIBLE
            }
        activity?.navigation?.menu?.findItem(R.id.projects)?.isChecked = true
    }

    private fun createRecyclerView() {

        ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.all?.let { convertProjectsToCard(it){ result -> adapterProjects = adapterFun(result)} }
        recyclerViewProjects.adapter = adapterProjects

    }

    fun adapterFun(list: MutableList<CardBinding>): AdapterProjects {

        return AdapterProjects(list, object : InClick {
            override fun onItemClick(item: CardBinding, position: Int) {
                NavigateModule(context!!).toDetailsFromProjects(item)
            }

        }, object : BookmarkClick {
            override fun setPosition(position: Int) {
                if (ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.getById(list[position].id!!) != null) {
                    ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.deleteById(list[position].id!!)
                    adapterProjects.list.removeAt(position)
                    adapterProjects.notifyDataSetChanged()
                        if (ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.all?.size == 0) {
                            textProjects.visibility = VISIBLE
                        }
                }
            }
        })
    }

    private fun convertProjectsToCard(list: MutableList<ProjectsBinding>, myCallBack: (result: MutableList<CardBinding>) -> Unit){
        val listCard: MutableList<CardBinding> = mutableListOf()
        for (i in 0 until list.size){
            listCard.add(CardBinding.ModelMapper.from(list[i]))
        }
        myCallBack.invoke(listCard)
    }
}
