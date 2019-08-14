package com.example.bestofbehance.layout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bestofbehance.BR
import com.example.bestofbehance.R
import com.example.bestofbehance.databinding.DetailsCardBinding
import com.example.bestofbehance.databinding.FragmentDetailsBinding
import com.example.bestofbehance.room.DBMain
import com.example.bestofbehance.viewModels.VMForParse
import com.example.bestofbehance.viewModels.ViewModelFactory
import kotlinx.android.synthetic.main.details_card.*
import kotlinx.android.synthetic.main.fragment_details.*


class DetailsFragment : Fragment() {

    private val args: DetailsFragmentArgs by navArgs()
    lateinit var jsonModel: VMForParse
    var position = 0

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.two_buttons_toolbar, menu)

        if (DBMain.find(context!!, args.cardBindingArg.id) == null) {
            menu.findItem(R.id.menu_bookmark)
                ?.setIcon(R.drawable.ic_bookmarks_normal)
        } else {
            menu.findItem(R.id.menu_bookmark)
                ?.setIcon(R.drawable.ic_bookmarks_pressed)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_bookmark -> {
                if (DBMain.find(context!!, args.cardBindingArg.id) == null) {
                    DBMain.add(context!!, args.cardBindingArg)
                    item.setIcon(R.drawable.ic_bookmarks_pressed)
                } else {
                    DBMain.delete(context!!, args.cardBindingArg.id)
                    item.setIcon(R.drawable.ic_bookmarks_normal)
                }
            }

            R.id.menu_share -> {
                Toast.makeText(activity, "Work in progress", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
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
        binding.notifyPropertyChanged(BR._all)

        jsonModel = ViewModelProviders.of(this, ViewModelFactory()).get(VMForParse::class.java)

        return fragmentDetailsView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        comments.text = this.resources.getQuantityString(
            R.plurals.commentsCount1,
            args.cardBindingArg.comments!!.toInt(),
            args.cardBindingArg.comments!!.toInt()
        )

        val liveData = fetchData()

        var imageItems: MutableList<MultiList>? = null
        var commentsItems: MutableList<MultiList>? = null

        liveData.observe(this,
            Observer<MutableList<MultiList>> {
                when (it[position]) {
                    is MultiList.ImageList -> imageItems = it
                    is MultiList.TextList -> imageItems = it
                    is MultiList.CountList -> commentsItems = it
                }

                if (imageItems != null && commentsItems != null) {

                    val temp: List<MultiList> = imageItems.orEmpty() + commentsItems.orEmpty()
                    recycler_view_details.adapter = AdapterGeneralComments(temp)
                    recycler_view_details.layoutManager = LinearLayoutManager(activity)

                    comments.setOnClickListener {
                        (recycler_view_details.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(imageItems!!.lastIndex + 1, 0)
                    }
                }
            })
    }



    private fun fetchData(): MediatorLiveData<MutableList<MultiList>> {
        val liveDataMulti = MediatorLiveData<MutableList<MultiList>>()
        mediatorAdd(liveDataMulti, jsonModel.setContent(args.cardBindingArg.id))
        mediatorAdd(liveDataMulti, jsonModel.setComments(args.cardBindingArg.id))
        return liveDataMulti
    }

    private fun mediatorAdd(mediator: MediatorLiveData<MutableList<MultiList>>, list: LiveData<MutableList<MultiList>>){
        mediator.addSource(list) {
            if (it != null) {
                mediator.value = it
            }
        }
    }
}
