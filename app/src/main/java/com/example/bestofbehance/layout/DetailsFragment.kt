package com.example.bestofbehance.layout

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.example.bestofbehance.R
import com.example.bestofbehance.viewModels.ParseInVM
import com.example.bestofbehance.viewModels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_details.*


class DetailsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var jsonModel: ParseInVM
    var position = 0

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.two_buttons_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onRefresh() {
        //swipe.isRefreshing = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jsonModel = ViewModelProviders.of(this, ViewModelFactory()).get(ParseInVM::class.java)

        val sharedPreference = activity?.getSharedPreferences("ViewMode", AppCompatActivity.MODE_PRIVATE)
        position = sharedPreference!!.getInt("position", position)
        list_name1.text = position.toString()

        list_post1.text = jsonModel.getGeneral().value?.get(position)?.post.toString()

    }

    override fun onResume() {
        super.onResume()
        //textDetails.text = "123"
    }
}
