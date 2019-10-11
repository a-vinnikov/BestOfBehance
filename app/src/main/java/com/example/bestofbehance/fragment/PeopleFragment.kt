package com.example.bestofbehance.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bestofbehance.R
import com.example.bestofbehance.classesToSupport.BookmarkClick
import com.example.bestofbehance.classesToSupport.MultiList
import com.example.bestofbehance.adapter.AdapterMulti
import com.example.bestofbehance.dagger.Injectable
import com.example.bestofbehance.extension.Converter
import com.example.bestofbehance.viewModel.ViewModelForParse
import com.example.bestofbehance.viewModel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_people.*
import javax.inject.Inject


class PeopleFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var jsonModel: ViewModelForParse
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
        if (jsonModel.getAllFromPeopleDB()?.size != 0) {
            textPeople.visibility = GONE
        } else {
            textPeople.visibility = VISIBLE
        }
        activity?.navigation?.menu?.findItem(R.id.people)?.isChecked = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jsonModel = ViewModelProviders.of(this, viewModelFactory).get(ViewModelForParse::class.java)

        if (recyclerViewPeople.adapter == null) {
            createRecyclerView()
        }
    }

    private fun createRecyclerView() {

        Converter.convertProjectsToMulti(jsonModel.getAllFromPeopleDB()) { result ->
            if (result.size != 0){
                val temp: MutableList<MultiList> = result
                 adapterPeople = AdapterMulti(temp, object :
                     BookmarkClick {
                     override fun setPosition(position: Int) {
                         if (jsonModel.getByUsernameFromPeopleDB((temp[position] as MultiList.PeopleList).multiPeople.username!!) != null) {
                             jsonModel.deleteByUsernameFromPeopleDB((temp[position] as MultiList.PeopleList).multiPeople.username!!)
                             adapterPeople.list.removeAt(position)
                             adapterPeople.notifyDataSetChanged()
                             if (jsonModel.getAllFromPeopleDB()?.size == 0) {
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



}
