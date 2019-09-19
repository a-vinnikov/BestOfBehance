package com.example.bestofbehance.forAdapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.synthetic.main.list_item.view.*
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.databinding.ListItemBinding
import com.example.bestofbehance.classesToSupport.InClick
import java.math.RoundingMode
import java.text.DecimalFormat
import com.example.bestofbehance.BR
import com.example.bestofbehance.R
import com.example.bestofbehance.classesToSupport.SharedPreferenceObject.sharedCurrentViewMode
import com.example.bestofbehance.databases.ProjectsDataBase
import com.example.bestofbehance.classesToSupport.BookmarkClick
import com.example.bestofbehance.classesToSupport.MathObject
import com.example.bestofbehance.classesToSupport.NaviController
import com.example.bestofbehance.fragments.VIEW_MODE_GRIDVIEW
import com.example.bestofbehance.fragments.VIEW_MODE_LISTVIEW


class PagingAdapterViewHolder(val inClick: InClick, val bookmarkClick: BookmarkClick, val layout: String) :
    PagedListAdapter<CardBinding, PagingAdapterViewHolder.ViewHolder>(diffCallback) {

    lateinit var context: Context
    var position = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        this.position = holder.adapterPosition
        var currentViewMode = ""

        when(layout){
            context.resources.getString(R.string.best) -> { currentViewMode = sharedCurrentViewMode(context, context.resources.getString(R.string.currentViewMode), currentViewMode)}
            context.resources.getString(R.string.profile_title) -> {currentViewMode = sharedCurrentViewMode(context, context.resources.getString(R.string.currentViewModeProfile), currentViewMode)}
        }


        holder.itemView.avatarView.setOnClickListener {
            NaviController(context).toProfileFromBest(getItem(holder.adapterPosition)?.username!!)
        }

        holder.itemView.bookmark.isChecked = ProjectsDataBase.getDatabase(context)?.getProjectsDao()?.getById(getItem(position)!!.id!!) != null

        holder.itemView.bookmark.setOnClickListener {
            bookmarkClick.setPosition(position)
        }

        holder.itemView.constLayout.setOnClickListener {
            inClick.onItemClick(getItem(holder.adapterPosition)!!, holder.adapterPosition)
        }

        if (layout == context.resources.getString(R.string.profile_title)){
            holder.itemView.avatarView.visibility = GONE
            holder.itemView.artistName.visibility = GONE
        }

        var copyList = getItem(holder.adapterPosition)!!.copy()

        when (currentViewMode) {
            VIEW_MODE_GRIDVIEW -> {
                holder.itemView.avatarView.visibility = GONE

                copyList.views = MathObject.decimal(copyList.views.toString())
                copyList.appreciations = MathObject.decimal(copyList.appreciations.toString())
                copyList.comments = MathObject.decimal(copyList.comments.toString())
            }

            VIEW_MODE_LISTVIEW -> {
                if (layout == context.resources.getString(R.string.best)) holder.itemView.avatarView.visibility = VISIBLE
                copyList = getItem(holder.adapterPosition)!!.copy()
            }
        }

        holder.bind(copyList)
    }

    class ViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(Api: CardBinding) {
            binding.cardView = Api
            binding.notifyPropertyChanged(BR._all)
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
