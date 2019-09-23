package com.example.bestofbehance.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bestofbehance.R
import com.example.bestofbehance.binding.PeopleBinding
import com.example.bestofbehance.database.PeopleDataBase
import com.example.bestofbehance.classesToSupport.BookmarkClick
import com.example.bestofbehance.classesToSupport.MultiList
import com.example.bestofbehance.adapter.AdapterMulti
import com.example.bestofbehance.binding.ProjectsBinding
import com.example.bestofbehance.viewModel.VMForParse
import com.example.bestofbehance.viewModel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_people.*


class PeopleFragment : Fragment() {

    lateinit var jsonModel: VMForParse
    lateinit var adapterPeople: AdapterMulti

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onResume() {
        super.onResume()
        if (PeopleDataBase.getDatabase(context!!)?.getPeopleDao()?.all?.size != 0) {
            textPeople.visibility = GONE
        } else {
            textPeople.visibility = VISIBLE
        }
        activity?.navigation?.menu?.findItem(R.id.people)?.isChecked = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jsonModel = ViewModelProviders.of(this, ViewModelFactory(context!!)).get(VMForParse::class.java)

        if (recyclerViewPeople.adapter == null) {
            createRecyclerView()
        }
    }

    private fun createRecyclerView() {

        convertProjectsToMulti(PeopleDataBase.getDatabase(context!!)?.getPeopleDao()?.all) { result ->
            if (result.size != 0){
                val temp: MutableList<MultiList> = result
                 adapterPeople = AdapterMulti(temp, object :
                     BookmarkClick {
                     override fun setPosition(position: Int) {
                         if (PeopleDataBase.getDatabase(context!!)?.getPeopleDao()?.getByUsername((temp[position] as MultiList.PeopleList).multiPeople.username!!) != null) {
                             PeopleDataBase.getDatabase(context!!)?.getPeopleDao()?.deleteByUsername((temp[position] as MultiList.PeopleList).multiPeople.username!!)
                             adapterPeople.list.removeAt(position)
                             adapterPeople.notifyDataSetChanged()
                             if (PeopleDataBase.getDatabase(context!!)?.getPeopleDao()?.all?.size == 0) {
                                 textPeople.visibility = VISIBLE
                             }
                         }
                     }
                 })

                recyclerViewPeople.adapter = adapterPeople
                recyclerViewPeople.layoutManager = LinearLayoutManager(activity)
            }
        }
    }

    private fun convertProjectsToMulti(list: MutableList<PeopleBinding>?, myCallBack: (result: MutableList<MultiList>) -> Unit) {
        val listMulti: MutableList<MultiList> = mutableListOf()
        for (i in 0 until list!!.size) {
            listMulti.add(
                MultiList.PeopleList.ModelMapper.from(list[i])
            )
        }
        myCallBack.invoke(listMulti)
    }

}
