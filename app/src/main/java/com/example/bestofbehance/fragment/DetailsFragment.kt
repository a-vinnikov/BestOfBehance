package com.example.bestofbehance.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bestofbehance.BR
import com.example.bestofbehance.R
import com.example.bestofbehance.classesToSupport.MultiList
import com.example.bestofbehance.module.FragmentNavigate
import com.example.bestofbehance.databinding.FragmentDetailsBinding
import com.example.bestofbehance.adapter.AdapterMulti
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.extension.WebOpening
import com.example.bestofbehance.viewModel.ViewModelForParse
import com.example.bestofbehance.viewModel.ViewModelFactory
import kotlinx.android.synthetic.main.details_card.*
import kotlinx.android.synthetic.main.fragment_details.*
import java.lang.reflect.InvocationTargetException

class DetailsFragment : Fragment() {

    private val args: DetailsFragmentArgs by navArgs()
    lateinit var jsonModel: ViewModelForParse
    private var cardArguments: CardBinding? = null
    var position = 0

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.two_buttons_toolbar, menu)

        if (jsonModel.checkInProjectsDB(cardArguments?.id!!) == null) {
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
                jsonModel.bookmarksToolbar(cardArguments!!, item)
            }

            R.id.menu_share -> {
                startActivity(Intent.createChooser(WebOpening.share(cardArguments?.url!!, context!!), resources.getString(R.string.share)))
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

        jsonModel = ViewModelProviders.of(this, ViewModelFactory(context!!)).get(ViewModelForParse::class.java)

        try {
            if (args.detailsBindingArgs?.id != null){
                cardArguments = args.detailsBindingArgs
            }
        } catch (e: InvocationTargetException){
            jsonModel.setSingleProject(arguments?.getString("projectId")!!.toInt())
        }

        jsonModel.listForProject.observe(viewLifecycleOwner, Observer<MutableList<CardBinding>>{
            cardArguments = it[0]
        })

        val binding = FragmentDetailsBinding.inflate(inflater)
        val fragmentDetailsView: View = binding.root

        binding.cardViewDetails = cardArguments
        binding.notifyPropertyChanged(BR._all)

        return fragmentDetailsView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        comments.text = this.resources.getQuantityString(
            R.plurals.big_comments_count,
            cardArguments?.comments!!.toInt(),
            cardArguments?.comments!!.toInt()
        )

        jsonModel.fetchData(cardArguments?.id!!)

        var imageItems: MutableList<MultiList>? = null
        var commentsItems: MutableList<MultiList>? = null

        jsonModel.liveDataMulti.observe(viewLifecycleOwner,
            Observer<MutableList<MultiList>> {
                if(it.size != 0) when (it[position]) {
                    is MultiList.ImageList -> imageItems = it
                    is MultiList.TextList -> imageItems = it
                    is MultiList.CountList -> commentsItems = it
                }

                if (imageItems != null && commentsItems != null) {

                    val temp: List<MultiList> = imageItems.orEmpty() + commentsItems.orEmpty()
                    recyclerViewDetails.adapter = AdapterMulti(temp as MutableList<MultiList>, null)
                    recyclerViewDetails.layoutManager = LinearLayoutManager(activity)

                    comments.setOnClickListener {
                        if (commentsItems!!.size < 7){
                            detailsCard.setExpanded(false, true)
                            (recyclerViewDetails.layoutManager as LinearLayoutManager).scrollToPosition(temp.size - 1)
                        } else {
                            detailsCard.setExpanded(false, true)
                            (recyclerViewDetails.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(imageItems!!.lastIndex + 1 ,
                                resources.getDimension(R.dimen.offset).toInt()
                            )
                        }

                    }
                }
            })
        avatarViewDetails.setOnClickListener { FragmentNavigate(
            context!!
        ).toProfileFromDetails(cardArguments?.username!!) }
    }
}
