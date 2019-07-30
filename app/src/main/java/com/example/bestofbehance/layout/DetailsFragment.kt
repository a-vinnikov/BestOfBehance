package com.example.bestofbehance.layout

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bestofbehance.R
import com.example.bestofbehance.databinding.FragmentDetailsBinding
import com.example.bestofbehance.gson.CardBinding
import com.example.bestofbehance.viewModels.ParseForVM
import com.example.bestofbehance.viewModels.ParseInVM
import com.example.bestofbehance.viewModels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.list_details.*


class DetailsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val args: DetailsFragmentArgs by navArgs()
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //val binding0: FragmentDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        val binding = FragmentDetailsBinding.inflate(inflater)
        val fragmentDetailsView: View = binding.root

        binding.cardViewDetails = args.cardBindingArg
        CardBinding().setImageUrl(binding.avatarView1, args.cardBindingArg.avatar.toString())

        jsonModel = ViewModelProviders.of(this, ViewModelFactory()).get(ParseInVM::class.java)

        return fragmentDetailsView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jsonModel.setImage(args.cardBindingArg.id!!)
        jsonModel.setComments(args.cardBindingArg.id!!)

        val observerGSON = Observer<MutableList<Ilist>> {
            recycler_view1.adapter = AdapterGeneralComments(it)
            recycler_view1.layoutManager = LinearLayoutManager(activity)
        }
        jsonModel.listForIlisit.observe(this, observerGSON)

        /*val sharedPreference = activity?.getSharedPreferences("ViewMode", AppCompatActivity.MODE_PRIVATE)
        position = sharedPreference!!.getInt("position", position)
        list_name1.text = position.toString()*/

        //list_post1.text = args.cardBindingArg.id
    }
}
