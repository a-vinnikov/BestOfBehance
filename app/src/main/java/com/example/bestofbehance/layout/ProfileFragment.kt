package com.example.bestofbehance.layout

import android.os.Bundle
import android.view.*
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
import com.example.bestofbehance.binding.ProfileBinding
import com.example.bestofbehance.databases.DBProjectsDao
import com.example.bestofbehance.databases.SharedPreferenceObject
import com.example.bestofbehance.databases.SharedPreferenceObject.editorSharedPreference
import com.example.bestofbehance.databases.forRoom.CardDataBase
import com.example.bestofbehance.databinding.FragmentProfileBinding
import com.example.bestofbehance.paging.ItemSourceFactory
import com.example.bestofbehance.viewModels.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class ProfileFragment : Fragment() {

    private val args: ProfileFragmentArgs by navArgs()
    lateinit var jsonModel: VMForParse

    private var currentViewMode = "list"
    var page = 1
    lateinit var pagingAdapterProfile: PagingAdapterViewHolder
    var userId = 0
    private var isLoading = false
    private val isLastPage = false

    init {
        Timber.plant(Timber.DebugTree())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.two_buttons_toolbar, menu)

        /*if (DBMain.find(context!!, userId) == null) {
            menu.findItem(R.id.menu_bookmark)
                ?.setIcon(R.drawable.ic_bookmarks_normal)
        } else {
            menu.findItem(R.id.menu_bookmark)
                ?.setIcon(R.drawable.ic_bookmarks_pressed)
        }*/

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.menu_bookmark -> {
                Toast.makeText(activity, "Work in progress", Toast.LENGTH_SHORT).show()
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
        val binding = FragmentProfileBinding.inflate(inflater)
        val fragmentDetailsView: View = binding.root

        jsonModel = ViewModelProviders.of(this, ViewModelFactory()).get(VMForParse::class.java)

        jsonModel.setUser(args.cardBindingProfile.username!!)

        val observerGSON = Observer<MutableList<ProfileBinding>> { list ->

            userId = list[0].id
            binding.cardViewProfile = list[0]
            binding.notifyPropertyChanged(BR._all)
        }

        jsonModel.listForUser.observe(this, observerGSON)

        return fragmentDetailsView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentViewMode = SharedPreferenceObject.sharedCurrentViewMode(context!!, "currentViewModeProfile", currentViewMode)
        when (currentViewMode){
            "list" -> viewModeProfile.isChecked = false
            "tile" -> viewModeProfile.isChecked = true
        }

        viewModeProfile.setOnClickListener {
            if (!viewModeProfile.isChecked) {
                createRecyclerView(VIEW_MODE_LISTVIEW)
                editorSharedPreference(context!!, "currentViewModeProfile", VIEW_MODE_LISTVIEW)
            } else if (viewModeProfile.isChecked) {
                createRecyclerView(VIEW_MODE_GRIDVIEW)
                editorSharedPreference(context!!,"currentViewModeProfile", VIEW_MODE_GRIDVIEW)
            }
        }

        if (recycler_view_profile.adapter == null) {
            createRecyclerView(currentViewMode)
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

    override fun onDestroy() {
        super.onDestroy()
        DBProjectsDao.close(context!!)
    }

    private fun createRecyclerView(currentViewMode: String) {
        if (jsonModel.listForUserProjects.value == null) {
            jsonModel.setUserProjects(args.cardBindingProfile.username!!, page)
        }

        val observerGSON = Observer<MutableList<CardBinding>> { list ->

            when (currentViewMode) {
                "list" -> { runBlocking { abc(list, "list") } }
                "tile" -> { runBlocking { abc(list, "tile") } }
            }
            recycler_view_profile.adapter = pagingAdapterProfile

            for(i in 0 until list.size){
                if(DBProjectsDao.find(context!!, list[i].id) != null){
                    DBProjectsDao.update(context!!, list[i])
                }
            }
        }
        jsonModel.listForUserProjects.observe(this, observerGSON)

        when (currentViewMode) {
            "list" -> { recycler_view_profile.layoutManager = LinearLayoutManager(activity) }
            "tile" -> { recycler_view_profile.layoutManager = GridLayoutManager(activity, 2) }
        }

    }

    private fun adapterFun(list: MutableList<CardBinding>, viewMode: String): PagedListAdapter<CardBinding, PagingAdapterViewHolder.ViewHolder> {

        return PagingAdapterViewHolder(viewMode, object : InClick {
            override fun onItemClick(item: CardBinding, position: Int) {
                NaviController(context!!).toDetailsFromBest(item)
            }
        }, object : BookmarkClick {
            override fun setPosition(position: Int) {
                if (DBProjectsDao.find(context!!, list[position].id) == null) {
                    DBProjectsDao.add(context!!, list[position])
                } else {
                    DBProjectsDao.delete(context!!, list[position].id)
                }
            }
        }, "Best")
    }

    private fun updateAdapter(
        asyncList: Deferred<MutableList<CardBinding>>,
        config: PagedList.Config,
        pagedListPagingAdapter: PagedListAdapter<CardBinding, PagingAdapterViewHolder.ViewHolder>
    ) {
        val turnoverDataSourceFactory = ItemSourceFactory(asyncList) {}

        val items = PagedList.Builder<Int, CardBinding>(turnoverDataSourceFactory.create(), config)
            .setNotifyExecutor(Executor { view!!.post(it) })
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .build()

        pagedListPagingAdapter.submitList(items)
    }

    suspend fun abc(list: MutableList<CardBinding>, viewMode: String) = coroutineScope {
        val cba = async { CardDataBase.getDatabase(context!!)?.getCardDao()?.all!! }
        updateAdapter(cba, ItemSourceFactory.providePagingConfig(), adapterFun(list, viewMode))}

}
