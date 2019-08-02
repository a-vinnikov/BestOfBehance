package com.example.bestofbehance.layout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bestofbehance.R
import com.example.bestofbehance.databinding.FragmentDetailsBinding
import com.example.bestofbehance.gson.CardBinding
import com.example.bestofbehance.viewModels.VMForParse
import com.example.bestofbehance.viewModels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_details.*


class DetailsFragment : Fragment(){

    private val args: DetailsFragmentArgs by navArgs()
    lateinit var jsonModel: VMForParse
    var position = 0

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.two_buttons_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentDetailsBinding.inflate(inflater)
        val fragmentDetailsView: View = binding.root

        binding.cardViewDetails = args.cardBindingArg
        CardBinding().setImageUrl(binding.avatarView1, args.cardBindingArg.avatar.toString())

        jsonModel = ViewModelProviders.of(this, ViewModelFactory()).get(VMForParse::class.java)

        return fragmentDetailsView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val liveData = fetchData()

        var imageItems: MutableList<Ilist>? = null
        var commentsItems: MutableList<Ilist>? = null

        liveData.observe(this,
            Observer<MutableList<Ilist>> {
                when (it[position]) {
                    is Ilist.ImageList -> imageItems = it
                    is Ilist.CommentsList -> commentsItems = it
                }
                if (imageItems != null && commentsItems != null) {
                    val temp: List<Ilist> = imageItems.orEmpty() + commentsItems.orEmpty()
                    recycler_view1.adapter = AdapterGeneralComments(temp)
                    recycler_view1.layoutManager = LinearLayoutManager(activity)
                    //liveData.removeObserver(this)
                }
            })

        /*val sharedPreference = activity?.getSharedPreferences("ViewMode", AppCompatActivity.MODE_PRIVATE)
        position = sharedPreference!!.getInt("position", position)
        list_name1.text = position.toString()*/
    }

    private fun fetchData(): MediatorLiveData<MutableList<Ilist>> {
        val liveDataIlist = MediatorLiveData<MutableList<Ilist>>()

        liveDataIlist.addSource(jsonModel.setImage(args.cardBindingArg.id!!)) {
            if (it != null) {
                liveDataIlist.value = it
                println("Длина листа c изображениями " + liveDataIlist.value?.size)
            }
        }
        liveDataIlist.addSource(jsonModel.setComments(args.cardBindingArg.id!!)) {
            if (it != null) {
                liveDataIlist.value = it
                println("Длина листа комментариев  " + liveDataIlist.value?.size)
            }
        }

        println("Длина медиатора-листа " + liveDataIlist.value?.size)
        return liveDataIlist
    }
}
