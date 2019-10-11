package com.example.bestofbehance.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bestofbehance.BR
import com.example.bestofbehance.R
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.binding.ProfileBinding
import com.example.bestofbehance.classesToSupport.*
import com.example.bestofbehance.databinding.FragmentProfileBinding
import com.example.bestofbehance.adapter.PagingAdapterProfile
import com.example.bestofbehance.binding.mapper.MapperForPeopleBinding
import com.example.bestofbehance.dagger.AllAboutSharedPreferences
import com.example.bestofbehance.dagger.Injectable
import com.example.bestofbehance.extension.WebOpening
import com.example.bestofbehance.dagger.module.FragmentNavigate
import com.example.bestofbehance.dagger.module.StorageModule
import com.example.bestofbehance.viewModel.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.profile_card.*
import javax.inject.Inject

class ProfileFragment : Fragment(), Injectable {

    @Inject
    lateinit var preferences: AllAboutSharedPreferences

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var username: String = ""
    lateinit var jsonModel: ViewModelForParse

    private var currentViewMode = VIEW_MODE_LISTVIEW
    lateinit var adapterProfile: PagingAdapterProfile
    lateinit var url: String
    var userId = 0

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.two_buttons_toolbar, menu)

        if (jsonModel.getByUsernameFromPeopleDB(username) == null) {
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
                    jsonModel.getByUsernameFromPeopleDB(username) == null -> {

                        jsonModel.setUser(username)
                        val observerGSON = Observer<MutableList<ProfileBinding>> { list ->
                            jsonModel.insertInPeopleDB(
                                MapperForPeopleBinding.from(list[0], username)
                            )
                        }

                        jsonModel.listForUser.observe(viewLifecycleOwner, observerGSON)
                        item.setIcon(R.drawable.ic_bookmarks_pressed)
                    }
                    else -> {
                        jsonModel.deleteByUsernameFromPeopleDB(username)
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

        jsonModel =
            ViewModelProviders.of(this, viewModelFactory).get(ViewModelForParse::class.java)

        username = arguments?.getString("username")!!

        jsonModel.setUser(username)

        val observerGSON = Observer<MutableList<ProfileBinding>> { list ->

            if (list[0].url != null) url = list[0].url!!
            if (list[0].id != null) userId = list[0].id!!
            binding.cardViewProfile = list[0]
            binding.notifyPropertyChanged(BR._all)

            if (list[0].aboutMe != resources.getString(R.string.no_information) && list[0].aboutMe!!.length > resources.getInteger(
                    R.integer.lengthOfAboutMe
                )
            ) {
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
                            pinterestImage.setOnClickListener {
                                WebOpening.immediately(
                                    links[resources.getString(
                                        R.string.pinterest
                                    )]!!, context!!
                                )
                            }
                        }
                        resources.getString(R.string.instagram) -> {
                            instagramImage.visibility = VISIBLE
                            instagramImage.setOnClickListener {
                                WebOpening.immediately(
                                    links[resources.getString(
                                        R.string.instagram
                                    )]!!, context!!
                                )
                            }
                        }
                        resources.getString(R.string.facebook) -> {
                            facebookImage.visibility = VISIBLE
                            facebookImage.setOnClickListener {
                                WebOpening.immediately(
                                    links[resources.getString(
                                        R.string.facebook
                                    )]!!, context!!
                                )
                            }
                        }
                        resources.getString(R.string.behance) -> {
                            beImage.visibility = VISIBLE
                            instagramImage.setOnClickListener {
                                WebOpening.immediately(
                                    links[resources.getString(
                                        R.string.behance
                                    )]!!, context!!
                                )
                            }
                        }
                        resources.getString(R.string.dribbble) -> {
                            dribbbleImage.visibility = VISIBLE
                            dribbbleImage.setOnClickListener {
                                WebOpening.immediately(
                                    links[resources.getString(
                                        R.string.dribbble
                                    )]!!, context!!
                                )
                            }
                        }
                        resources.getString(R.string.twitter) -> {
                            twitterImage.visibility = VISIBLE
                            twitterImage.setOnClickListener {
                                WebOpening.immediately(
                                    links[resources.getString(
                                        R.string.twitter
                                    )]!!, context!!
                                )
                            }
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

        currentViewMode = preferences.stringGet(resources.getString(R.string.current_view_mode_profile), currentViewMode
        )

        when (currentViewMode) {
            VIEW_MODE_LISTVIEW -> viewModeProfile.isChecked = false
            VIEW_MODE_GRIDVIEW -> viewModeProfile.isChecked = true
        }

        viewModeProfile.setOnClickListener {
            when (viewModeProfile.isChecked) {
                false -> {
                    preferences.stringEdit(resources.getString(R.string.current_view_mode_profile),
                        VIEW_MODE_LISTVIEW
                    )
                    recyclerViewProfile.layoutManager = LinearLayoutManager(activity)
                }
                true -> {
                    preferences.stringEdit(resources.getString(R.string.current_view_mode_profile),
                        VIEW_MODE_GRIDVIEW
                    )
                    recyclerViewProfile.layoutManager = GridLayoutManager(activity, 2)
                }
            }
            recyclerViewProfile.adapter?.notifyDataSetChanged()
        }

        if (recyclerViewProfile.adapter == null) {
            when (currentViewMode) {
                VIEW_MODE_LISTVIEW -> {
                    recyclerViewProfile.layoutManager = LinearLayoutManager(activity)
                }
                VIEW_MODE_GRIDVIEW -> {
                    recyclerViewProfile.layoutManager = GridLayoutManager(activity, 2)
                }
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
            jsonModel.setUserProjects(username)
        }
        adapterProfile = adapterFun() as PagingAdapterProfile
        recyclerViewProfile.adapter = adapterProfile
        jsonModel.profilePagedList?.observe(viewLifecycleOwner,
            Observer<PagedList<CardBinding>> {
                adapterProfile.submitList(it)
            })

    }

    private fun adapterFun(): PagedListAdapter<CardBinding, PagingAdapterProfile.ViewHolder> {

        return PagingAdapterProfile(object : InClick {
            override fun onItemClick(item: CardBinding, position: Int) {
                FragmentNavigate(context!!).toDetailsFromProfile(item.id.toString())
            }
        }, object : BookmarkClick {
            override fun setPosition(position: Int) {
                jsonModel.bookmarksProjects(jsonModel.profilePagedList?.value?.get(position)!!)
            }
        })
    }
}
