package com.example.bestofbehance.fragments

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
import com.example.bestofbehance.classesToSupport.MultiList
import com.example.bestofbehance.databases.ProjectsDataBase
import com.example.bestofbehance.classesToSupport.NaviController
import com.example.bestofbehance.databinding.FragmentDetailsBinding
import com.example.bestofbehance.forAdapters.AdapterMulti
import com.example.bestofbehance.viewModels.VMForParse
import com.example.bestofbehance.viewModels.ViewModelFactory
import kotlinx.android.synthetic.main.details_card.*
import kotlinx.android.synthetic.main.fragment_details.*
import java.text.SimpleDateFormat
import java.util.*


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
                            getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss"))
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
                intent.type = "text/plain"

                startActivity(Intent.createChooser(intent, "Share to : "))
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

        liveData.observe(viewLifecycleOwner,
            Observer<MutableList<MultiList>> {
                if(it.size != 0) when (it[position]) {
                    is MultiList.ImageList -> imageItems = it
                    is MultiList.TextList -> imageItems = it
                    is MultiList.CountList -> commentsItems = it
                }

                if (imageItems != null && commentsItems != null) {

                    val temp: List<MultiList> = imageItems.orEmpty() + commentsItems.orEmpty()
                    recycler_view_details.adapter = AdapterMulti(temp as MutableList<MultiList>, null)
                    recycler_view_details.layoutManager = LinearLayoutManager(activity)

                    comments.setOnClickListener {
                        if (commentsItems!!.size < 7){
                            details_card.setExpanded(false, true)
                            (recycler_view_details.layoutManager as LinearLayoutManager).scrollToPosition(temp.size - 1)
                        } else {
                            details_card.setExpanded(false, true)
                            (recycler_view_details.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(imageItems!!.lastIndex + 1 , resources.getInteger(R.integer.offsetComments)
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
        mediatorAdd(liveDataMulti, jsonModel.setContent(args.cardBindingArg.id!!, context!!))
        mediatorAdd(liveDataMulti, jsonModel.setComments(args.cardBindingArg.id!!, context!!))
        return liveDataMulti
    }

    private fun mediatorAdd(mediator: MediatorLiveData<MutableList<MultiList>>, list: LiveData<MutableList<MultiList>>){
        mediator.addSource(list) {
            if (it != null) {
                mediator.value = it
            }
        }
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}
