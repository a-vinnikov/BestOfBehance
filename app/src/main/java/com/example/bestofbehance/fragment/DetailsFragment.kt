package com.example.bestofbehance.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bestofbehance.BR
import com.example.bestofbehance.R
import com.example.bestofbehance.classesToSupport.MultiList
import com.example.bestofbehance.dagger.module.FragmentNavigate
import com.example.bestofbehance.databinding.FragmentDetailsBinding
import com.example.bestofbehance.adapter.AdapterMulti
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.dagger.Injectable
import com.example.bestofbehance.extension.WebOpening
import com.example.bestofbehance.viewModel.ViewModelForParse
import com.example.bestofbehance.viewModel.ViewModelFactory
import kotlinx.android.synthetic.main.details_card.*
import kotlinx.android.synthetic.main.fragment_details.*
import javax.inject.Inject

class DetailsFragment : Fragment(), Injectable {

    lateinit var jsonModel: ViewModelForParse
    var cardArguments: CardBinding? = null
    var projectId: Int = 0
    var position = 0

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.two_buttons_toolbar, menu)

        if (jsonModel.checkInProjectsDB(projectId) == null) {
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
                jsonModel.bookmarksToolbar(cardArguments, item)
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

        val binding = FragmentDetailsBinding.inflate(inflater)
        val fragmentDetailsView: View = binding.root

        jsonModel = ViewModelProviders.of(this, viewModelFactory).get(ViewModelForParse::class.java)

        projectId = arguments?.getString("projectId")!!.toInt()

        val observerGSON = Observer<MutableList<CardBinding>>{
            cardArguments = it[0]
            binding.cardViewDetails = cardArguments
            binding.notifyPropertyChanged(BR._all)

            comments.text = this.resources.getQuantityString(
                R.plurals.big_comments_count,
                it[0].comments!!.toInt(),
                it[0].comments!!.toInt()
            )
        }

        jsonModel.listForProject.observe(viewLifecycleOwner, observerGSON)

        return fragmentDetailsView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jsonModel.setSingleProject(projectId)

        jsonModel.fetchData(projectId)

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
