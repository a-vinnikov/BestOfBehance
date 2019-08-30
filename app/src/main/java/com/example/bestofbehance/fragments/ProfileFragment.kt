package com.example.bestofbehance.fragments

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
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
import com.example.bestofbehance.databases.SharedPreferenceObject
import com.example.bestofbehance.databases.SharedPreferenceObject.editorSharedPreference
import com.example.bestofbehance.databases.forRoom.PeopleDataBase
import com.example.bestofbehance.databases.forRoom.ProjectsDataBase
import com.example.bestofbehance.databinding.FragmentProfileBinding
import com.example.bestofbehance.viewModels.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.profile_card.*
import org.jetbrains.anko.lines
import java.text.SimpleDateFormat
import java.util.*


class ProfileFragment : Fragment() {

    private val args: ProfileFragmentArgs by navArgs()
    lateinit var jsonModel: VMForParse

    private var currentViewMode = "list"
    lateinit var adapterProfile: PagingAdapterViewHolder
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
                if (PeopleDataBase.getDatabase(context!!)?.getPeopleDao()?.getByUsername(args.cardBindingProfile) == null) {

                    jsonModel.setUser(args.cardBindingProfile)
                    val observerGSON = Observer<MutableList<ProfileBinding>> { list ->
                        PeopleDataBase.getDatabase(context!!)?.getPeopleDao()?.insert(
                            PeopleBinding(
                                list[0].id,
                                args.cardBindingProfile,
                                list[0].avatar,
                                list[0].name,
                                list[0].post,
                                list[0].views,
                                list[0].appreciations,
                                list[0].followers,
                                list[0].following,
                                getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss")
                            )
                        )
                    }

                    jsonModel.listForUser.observe(this, observerGSON)
                    item.setIcon(R.drawable.ic_bookmarks_pressed)
                } else {
                    PeopleDataBase.getDatabase(context!!)?.getPeopleDao()
                        ?.deleteByUsername(args.cardBindingProfile)
                    item.setIcon(R.drawable.ic_bookmarks_normal)
                }
            }

            R.id.menu_share -> {
                copyText("https://www.behance.net/${args.cardBindingProfile}")
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

        jsonModel = ViewModelProviders.of(this, ViewModelFactory()).get(VMForParse::class.java)

        jsonModel.setUser(args.cardBindingProfile)

        val observerGSON = Observer<MutableList<ProfileBinding>> { list ->

            userId = list[0].id
            binding.cardViewProfile = list[0]
            binding.notifyPropertyChanged(BR._all)

            if (list[0].aboutMe != "No information" || list[0].aboutMe!!.length > 100){
                more.visibility = VISIBLE
            }
        }

        jsonModel.listForUser.observe(this, observerGSON)

        return fragmentDetailsView
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentViewMode = SharedPreferenceObject.sharedCurrentViewMode(
            context!!,
            "currentViewModeProfile",
            currentViewMode
        )
        when (currentViewMode) {
            "list" -> viewModeProfile.isChecked = false
            "tile" -> viewModeProfile.isChecked = true
        }

        viewModeProfile.setOnClickListener {
            if (!viewModeProfile.isChecked) {
                createRecyclerView(VIEW_MODE_LISTVIEW)
                editorSharedPreference(context!!, "currentViewModeProfile", VIEW_MODE_LISTVIEW)
            } else if (viewModeProfile.isChecked) {
                createRecyclerView(VIEW_MODE_GRIDVIEW)
                editorSharedPreference(context!!, "currentViewModeProfile", VIEW_MODE_GRIDVIEW)
            }
        }

        if (recycler_view_profile.adapter == null) {
            createRecyclerView(currentViewMode)
        }

        more.setOnClickListener {
            when (about_me.lineCount) {
                3 -> {
                    about_me.maxLines = Integer.MAX_VALUE; about_me.ellipsize = null; more.text =
                        "Minimize"
                }
                in 3..Integer.MAX_VALUE -> {
                    about_me.maxLines = 3; about_me.ellipsize =
                        TextUtils.TruncateAt.END; more.text = "More"
                }
            }
        }

        /*recycler_view_profile.addOnScrollListener(object : PaginationScrollListener(LinearLayoutManager(context)) {
            override fun getTotalPageCount(): Int {
                //return TOTAL_PAGES
                return 0
            }

            override fun isLastPage(): Boolean {

                if (adapterProfile.position == (adapterProfile.list.size - 1) && adapterProfile.list.size > 40) {
                    isLoading = true
                    page += 1
                    jsonModel.setNextPageForUser(args.cardBindingProfile.username!!, page)
                    val observerGSONPaging = Observer<MutableList<CardBinding>> {
                        adapterProfile.addData(it)
                        isLoading = false
                        Timber.d("Loaded page $page")
                    }
                    jsonModel.pagingResponseList.observe(this@ProfileFragment, observerGSONPaging)
                }
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
            }
        })*/

    }

    private fun createRecyclerView(currentViewMode: String) {
        if (jsonModel.profilePagedList?.value == null) {
            jsonModel.setUserProjects(args.cardBindingProfile)
        }

        when (currentViewMode) {
            "list" -> {
                recycler_view_profile.layoutManager = LinearLayoutManager(activity)
                jsonModel.profilePagedList?.observe(this,
                    Observer<PagedList<CardBinding>> {
                        adapterProfile = adapterFun(it, "list") as PagingAdapterViewHolder
                        adapterProfile.submitList(it)
                        recycler_view_profile.adapter = adapterProfile
                    })
            }
            "tile" -> {
                recycler_view_profile.layoutManager = GridLayoutManager(activity, 2)
                jsonModel.profilePagedList?.observe(this,
                    Observer<PagedList<CardBinding>> {
                        adapterProfile = adapterFun(it, "tile") as PagingAdapterViewHolder
                        adapterProfile.submitList(it)
                        recycler_view_profile.adapter = adapterProfile
                    })
            }
        }

    }

    private fun adapterFun(
        list: MutableList<CardBinding>,
        viewMode: String
    ): PagedListAdapter<CardBinding, PagingAdapterViewHolder.ViewHolder> {

        return PagingAdapterViewHolder(viewMode, object : InClick {
            override fun onItemClick(item: CardBinding, position: Int) {
                NaviController(context!!).toDetailsFromProfile(item)
            }
        }, object : BookmarkClick {
            override fun setPosition(position: Int) {
                if (ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.getById(list[position].id) == null) {
                    ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()?.insert(
                        ProjectsBinding(
                            list[position].id, list[position].bigImage, list[position].thumbnail,
                            list[position].avatar, list[position].artistName,
                            list[position].artName, list[position].views,
                            list[position].appreciations, list[position].comments,
                            list[position].username, list[position].published,
                            getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss")
                        )
                    )
                } else {
                    ProjectsDataBase.getDatabase(context!!)?.getProjectsDao()
                        ?.deleteById(list[position].id)
                }
            }
        }, "Profile")
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun webPageOpen(url: String) {
        val uris = Uri.parse(url)
        val intents = Intent(Intent.ACTION_VIEW, uris)
        startActivity(intents)
    }

    fun copyText(text: String) {
        val myClip = ClipData.newPlainText("text", text)
        val myClipboard = context!!.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        myClipboard.primaryClip = myClip
        Toast.makeText(context!!, "Copied into clipboard", Toast.LENGTH_SHORT).show()
    }
}
