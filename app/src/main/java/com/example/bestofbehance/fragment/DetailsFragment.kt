package com.example.bestofbehance.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bestofbehance.BR
import com.example.bestofbehance.R
import com.example.bestofbehance.binding.ProjectsBinding
import com.example.bestofbehance.classesToSupport.CurrentDate
import com.example.bestofbehance.classesToSupport.MultiList
import com.example.bestofbehance.database.ProjectsDataBase
import com.example.bestofbehance.dagger.NaviController
import com.example.bestofbehance.databinding.FragmentDetailsBinding
import com.example.bestofbehance.adapter.AdapterMulti
import com.example.bestofbehance.viewModel.VMForParse
import com.example.bestofbehance.viewModel.ViewModelFactory
import kotlinx.android.synthetic.main.details_card.*
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {

    private val args: DetailsFragmentArgs by navArgs()
    lateinit var jsonModel: VMForParse
    var position = 0

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.two_buttons_toolbar, menu)

        if (ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.getById(args.cardBindingArg.id!!) == null) {
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
                if (ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.getById(args.cardBindingArg.id!!) == null) {
                    ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.insert(
                        ProjectsBinding(args.cardBindingArg.id!!, args.cardBindingArg.bigImage, args.cardBindingArg.thumbnail,
                            args.cardBindingArg.avatar, args.cardBindingArg.artistName,
                            args.cardBindingArg.artName, args.cardBindingArg.views,
                            args.cardBindingArg.appreciations, args.cardBindingArg.comments,
                            args.cardBindingArg.username, args.cardBindingArg.published, args.cardBindingArg.url,
                            CurrentDate.getCurrentDateTime().toString())
                    )
                    item.setIcon(R.drawable.ic_bookmarks_pressed)
                } else {
                    ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.deleteById(args.cardBindingArg.id!!)
                    item.setIcon(R.drawable.ic_bookmarks_normal)
                }
            }

            R.id.menu_share -> {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, args.cardBindingArg.url)
                intent.type = resources.getString(R.string.intent_type)

                startActivity(Intent.createChooser(intent, resources.getString(R.string.share)))
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

        jsonModel = ViewModelProviders.of(this, ViewModelFactory(context!!)).get(VMForParse::class.java)

        return fragmentDetailsView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        comments.text = this.resources.getQuantityString(
            R.plurals.big_comments_count,
            args.cardBindingArg.comments!!.toInt(),
            args.cardBindingArg.comments!!.toInt()
        )

        val liveData = fetchData()

        var imageItems: MutableList<MultiList>? = null
        var commentsItems: MutableList<MultiList>? = null

        liveData.observe(viewLifecycleOwner,
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

        avatarViewDetails.setOnClickListener { NaviController(
            context!!
        ).toProfileFromDetails(args.cardBindingArg.username!!) }
    }



    private fun fetchData(): MediatorLiveData<MutableList<MultiList>> {
        val liveDataMulti = MediatorLiveData<MutableList<MultiList>>()
        mediatorAdd(liveDataMulti, jsonModel.setContent(args.cardBindingArg.id!!))
        mediatorAdd(liveDataMulti, jsonModel.setComments(args.cardBindingArg.id!!))
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
