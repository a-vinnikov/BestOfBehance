package com.example.bestofbehance.fragments

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
import com.example.bestofbehance.databases.forRoom.PeopleDataBase
import com.example.bestofbehance.viewModels.VMForParse
import com.example.bestofbehance.viewModels.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_people.*
import kotlinx.android.synthetic.main.fragment_projects.*


class PeopleFragment : Fragment() {

    lateinit var jsonModel: VMForParse
    lateinit var adapterPeople: AdapterNonPaging

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
            text_people.visibility = GONE
        } else {
            text_people.visibility = VISIBLE
        }
        activity?.navigation?.menu?.findItem(R.id.people)?.isChecked = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jsonModel = ViewModelProviders.of(this, ViewModelFactory()).get(VMForParse::class.java)

        if (recycler_view_people.adapter == null) {
            createRecyclerView()
        }
    }

    private fun createRecyclerView() {

        convertProjectsToCard { result ->
            if (result.size != 0){
                val temp: List<MultiList> = result
                recycler_view_people.adapter = AdapterMulti(temp)
                recycler_view_people.layoutManager = LinearLayoutManager(activity)
            }
        }
    }

    private fun convertProjectsToCard(myCallBack: (result: MutableList<MultiList>) -> Unit) {
        val list = PeopleDataBase.getDatabase(context!!)?.getPeopleDao()?.all
        val listCard: MutableList<MultiList> = mutableListOf()
        for (i in 0 until list!!.size) {
            listCard.add(
                MultiList.PeopleList(
                    PeopleBinding(
                        list[i].id,
                        list[i].username,
                        list[i].avatar,
                        list[i].name,
                        list[i].post,
                        list[i].views,
                        list[i].appreciations,
                        list[i].followers,
                        list[i].following,
                        list[i].added
                    )
                )
            )
        }
        myCallBack.invoke(listCard)
    }

}
