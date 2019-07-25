package com.example.bestofbehance.layout

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.*
import androidx.navigation.fragment.navArgs
import com.example.bestofbehance.R
import com.example.bestofbehance.databinding.FragmentDetailsBinding
import com.example.bestofbehance.gson.CardBinding
import kotlinx.android.synthetic.main.fragment_details.*


class DetailsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val args: DetailsFragmentArgs by navArgs()
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

        val binding : FragmentDetailsBinding = DataBindingUtil.inflate(inflater ,R.layout.fragment_details,container , false)
        val fragmentDetailsView : View  = binding.root

        // setting values to model
        val argsBinding = args.cardBindingArg
        binding.cardView = argsBinding
        CardBinding().setImageUrl(binding.bigImageView1, args.cardBindingArg.bigImage.toString(), "not rounded")
        CardBinding().setImageUrl(binding.avatarView1, args.cardBindingArg.avatar.toString(), "not rounded")

        return fragmentDetailsView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreference = activity?.getSharedPreferences("ViewMode", AppCompatActivity.MODE_PRIVATE)
        position = sharedPreference!!.getInt("position", position)
        list_name1.text = position.toString()

        //list_post1.text = args.cardBindingArg.id
    }
}
