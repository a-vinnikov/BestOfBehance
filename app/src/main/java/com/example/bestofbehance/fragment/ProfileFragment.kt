package com.example.bestofbehance.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bestofbehance.BR
import com.example.bestofbehance.R
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.binding.PeopleBinding
import com.example.bestofbehance.binding.ProfileBinding
import com.example.bestofbehance.binding.ProjectsBinding
import com.example.bestofbehance.classesToSupport.*
import com.example.bestofbehance.classesToSupport.SharedPreferenceObject.editorSharedPreference
import com.example.bestofbehance.database.PeopleDataBase
import com.example.bestofbehance.database.ProjectsDataBase
import com.example.bestofbehance.databinding.FragmentProfileBinding
import com.example.bestofbehance.adapter.PagingAdapterProfile
import com.example.bestofbehance.dagger.NaviController
import com.example.bestofbehance.viewModel.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.profile_card.*

class ProfileFragment : Fragment() {

    private val args: ProfileFragmentArgs by navArgs()
    lateinit var jsonModel: VMForParse

    private var currentViewMode = VIEW_MODE_LISTVIEW
    lateinit var adapterProfile: PagingAdapterProfile
    lateinit var url: String
    var userId = 0

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.two_buttons_toolbar, menu)

        if (PeopleDataBase.getDatabase(context!!)?.getPeopleDao()?.getByUsername(args.cardBindingProfile) == null) {
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
                when {
                    nameProfile.text == null -> item.isCheckable = false
                    PeopleDataBase.getDatabase(context!!)?.getPeopleDao()?.getByUsername(args.cardBindingProfile) == null -> {

                        jsonModel.setUser(args.cardBindingProfile)
                        val observerGSON = Observer<MutableList<ProfileBinding>> { list ->
                            PeopleDataBase.getDatabase(context!!)?.getPeopleDao()?.insert(
                                PeopleBinding(
                                    list[0].id!!,
                                    args.cardBindingProfile,
                                    list[0].avatar,
                                    list[0].name,
                                    list[0].post,
                                    list[0].views,
                                    list[0].appreciations,
                                    list[0].followers,
                                    list[0].following,
                                    CurrentDate.getCurrentDateTime().toString()
                                )
                            )
                        }

                        jsonModel.listForUser.observe(viewLifecycleOwner, observerGSON)
                        item.setIcon(R.drawable.ic_bookmarks_pressed)
                    }
                    else -> {
                        PeopleDataBase.getDatabase(context!!)?.getPeopleDao()
                            ?.deleteByUsername(args.cardBindingProfile)
                        item.setIcon(R.drawable.ic_bookmarks_normal)
                    }
                }
            }

            R.id.menu_share -> {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(
                    Intent.EXTRA_TEXT, url
                )
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater)
        val fragmentDetailsView: View = binding.root

        jsonModel = ViewModelProviders.of(this, ViewModelFactory(context!!)).get(VMForParse::class.java)

        jsonModel.setUser(args.cardBindingProfile)

        val observerGSON = Observer<MutableList<ProfileBinding>> { list ->

            if (list[0].url != null ) url = list[0].url!!

            if (list[0].id != null) userId = list[0].id!!
            binding.cardViewProfile = list[0]
            binding.notifyPropertyChanged(BR._all)

            if (list[0].aboutMe != resources.getString(R.string.no_information) && list[0].aboutMe!!.length > resources.getInteger(R.integer.lengthOfAboutMe)) {
                more.visibility = VISIBLE
            }
        }

        jsonModel.listForUser.observe(viewLifecycleOwner, observerGSON)

        val observerMapGSON = Observer<MutableMap<String, String?>> { links ->
            for (i in links.keys) {
                if (links[i] != null) {
                    when (i) {
                        resources.getString(R.string.pinterest) -> {
                            pinterestImage.visibility = VISIBLE
                            pinterestImage.setOnClickListener { webPageOpen(links[resources.getString(R.string.pinterest)]!!) }
                        }
                        resources.getString(R.string.instagram) -> {
                            instagramImage.visibility = VISIBLE
                            instagramImage.setOnClickListener { webPageOpen(links[resources.getString(R.string.instagram)]!!) }
                        }
                        resources.getString(R.string.facebook) -> {
                            facebookImage.visibility = VISIBLE
                            facebookImage.setOnClickListener { webPageOpen(links[resources.getString(R.string.facebook)]!!) }
                        }
                        resources.getString(R.string.behance) -> {
                            beImage.visibility = VISIBLE
                            instagramImage.setOnClickListener { webPageOpen(links[resources.getString(R.string.behance)]!!) }
                        }
                        resources.getString(R.string.dribbble) -> {
                            dribbbleImage.visibility = VISIBLE
                            dribbbleImage.setOnClickListener { webPageOpen(links[resources.getString(R.string.dribbble)]!!) }
                        }
                        resources.getString(R.string.twitter) -> {
                            twitterImage.visibility = VISIBLE
                            twitterImage.setOnClickListener { webPageOpen(links[resources.getString(R.string.twitter)]!!) }
                        }
                    }
                }
            }
        }
        jsonModel.listForLinks.observe(viewLifecycleOwner, observerMapGSON)

        return fragmentDetailsView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentViewMode = SharedPreferenceObject.getSharedPreference(context!!, resources.getString(R.string.current_view_mode_profile), currentViewMode
        )

        when (currentViewMode) {
            VIEW_MODE_LISTVIEW -> viewModeProfile.isChecked = false
            VIEW_MODE_GRIDVIEW -> viewModeProfile.isChecked = true
        }

        viewModeProfile.setOnClickListener {
            when(viewModeProfile.isChecked){
                false -> {
                editorSharedPreference(context!!, resources.getString(R.string.current_view_mode_profile), VIEW_MODE_LISTVIEW)
                recyclerViewProfile.layoutManager = LinearLayoutManager(activity)
            }
                true -> {
                editorSharedPreference(context!!, resources.getString(R.string.current_view_mode_profile), VIEW_MODE_GRIDVIEW)
                recyclerViewProfile.layoutManager = GridLayoutManager(activity, 2)
            }
        }
            recyclerViewProfile.adapter?.notifyDataSetChanged()
        }

        if (recyclerViewProfile.adapter == null) {
            when(currentViewMode){
                VIEW_MODE_LISTVIEW -> {recyclerViewProfile.layoutManager = LinearLayoutManager(activity)}
                VIEW_MODE_GRIDVIEW -> {recyclerViewProfile.layoutManager = GridLayoutManager(activity, 2)}
            }
            createRecyclerView()
        }

        more.setOnClickListener {
            when {
                aboutMe.lineCount == 3 -> {
                    aboutMe.maxLines = Integer.MAX_VALUE
                    aboutMe.ellipsize = null
                    more.text = resources.getString(R.string.minimize)
                }
                aboutMe.lineCount > 3 -> {
                    aboutMe.maxLines = 3
                    aboutMe.ellipsize = TextUtils.TruncateAt.END
                    more.text = resources.getString(R.string.more)
                }
            }
        }

    }

    private fun createRecyclerView() {
        if (jsonModel.profilePagedList?.value == null) {
            jsonModel.setUserProjects(args.cardBindingProfile)
        }

        jsonModel.profilePagedList?.observe(viewLifecycleOwner,
            Observer<PagedList<CardBinding>> {
                adapterProfile = adapterFun(it) as PagingAdapterProfile
                adapterProfile.submitList(it)
                recyclerViewProfile.adapter = adapterProfile
            })

    }

    private fun adapterFun(list: MutableList<CardBinding>): PagedListAdapter<CardBinding, PagingAdapterProfile.ViewHolder> {

        return PagingAdapterProfile(object : InClick {
            override fun onItemClick(item: CardBinding, position: Int) {
                NaviController(context!!).toDetailsFromProfile(item)
            }
        }, object : BookmarkClick {
            override fun setPosition(position: Int) {
                if (ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.getById(list[position].id!!) == null) {
                    ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.insert(
                        ProjectsBinding(
                            list[position].id!!, list[position].bigImage, list[position].thumbnail,
                            list[position].avatar, list[position].artistName,
                            list[position].artName, list[position].views,
                            list[position].appreciations, list[position].comments,
                            list[position].username, list[position].published, list[position].url,
                            CurrentDate.getCurrentDateTime().toString()
                        )
                    )
                } else {
                    ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()
                        ?.deleteById(list[position].id!!)
                }
            }
        })
    }

    private fun webPageOpen(url: String) {
        val uris = Uri.parse(url)
        val intents = Intent(Intent.ACTION_VIEW, uris)
        startActivity(intents)
    }
}
