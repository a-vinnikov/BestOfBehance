package com.example.bestofbehance.fragments

import android.content.Context
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.synthetic.main.list_item.view.*
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.databinding.ListItemBinding
import com.example.bestofbehance.viewModels.InClick
import java.math.RoundingMode
import java.text.DecimalFormat
import com.example.bestofbehance.BR
import com.example.bestofbehance.R
import com.example.bestofbehance.databases.SharedPreferenceObject
import com.example.bestofbehance.databases.forRoom.ProjectsDataBase
import com.example.bestofbehance.viewModels.BookmarkClick
import com.example.bestofbehance.viewModels.NaviController
import timber.log.Timber


class PagingAdapterViewHolder(val viewMode: String, val inClick: InClick, val bookmarkClick: BookmarkClick, val layout: String) :
    PagedListAdapter<CardBinding, PagingAdapterViewHolder.ViewHolder>(diffCallback) {

    lateinit var context: Context
    var position = 0

    //RecyclerView.Adapter<AdapterViewHolder.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        this.position = holder.adapterPosition

        holder.itemView.avatarView.setOnClickListener {
            when (layout){
                "Best" -> {NaviController(context).toProfileFromBest(getItem(holder.adapterPosition)?.username!!)}
                "Projects" -> {NaviController(context).toProfileFromProjects(getItem(holder.adapterPosition)?.username!!)}
            }
            SharedPreferenceObject.editorSharedPreference(context, "position", holder.adapterPosition.toString())
        }

        holder.itemView.bookmark.isChecked = ProjectsDataBase.getDatabase(context)?.getProjectsDao()?.getById(getItem(position)!!.id) != null

        holder.itemView.bookmark.setOnClickListener {
            bookmarkClick.setPosition(position)
        }

        holder.itemView.constLayout.setOnClickListener {
            inClick.onItemClick(getItem(holder.adapterPosition)!!, holder.adapterPosition)
        }

        if (layout == "Profile"){
            holder.itemView.avatarView.visibility = GONE
            holder.itemView.artistName.visibility = GONE
        }

        if (viewMode == "tile") {
            holder.itemView.avatarView.visibility = GONE
            val tValue = TypedValue()
            context.resources.getValue(R.dimen.height_of_grid, tValue, true)
            val floatResources = tValue.float
            holder.itemView.bigImageView.layoutParams.height = floatResources.toInt()
        }

        val copyList = getItem(holder.adapterPosition)!!.copy()
        if (viewMode == "tile") {
            copyList.views = holder.decimal(copyList.views.toString())
            copyList.appreciations = holder.decimal(copyList.appreciations.toString())
            copyList.comments = holder.decimal(copyList.comments.toString())

        }

        holder.bind(copyList)
    }

    class ViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(Api: CardBinding) {
            binding.cardView = Api
            binding.notifyPropertyChanged(BR._all)
        }

        fun decimal(numberString: String): String {
            var number = numberString
            if (number.toInt() > 1000) {
                var round = number.toInt()
                val df = DecimalFormat("#.#")
                round /= 1000
                df.roundingMode = RoundingMode.CEILING
                number = (df.format(round) + "k")
            }
            return number
        }

    }


    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<CardBinding>() {
            override fun areItemsTheSame(oldItem: CardBinding, newItem: CardBinding): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CardBinding, newItem: CardBinding): Boolean =
                oldItem == newItem
        }
    }

}
