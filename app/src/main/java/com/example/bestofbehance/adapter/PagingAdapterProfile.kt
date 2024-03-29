package com.example.bestofbehance.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.synthetic.main.list_item.view.*
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.databinding.ListItemBinding
import com.example.bestofbehance.classesToSupport.listeners.LayoutClick
import com.example.bestofbehance.BR
import com.example.bestofbehance.database.ProjectsDataBase
import com.example.bestofbehance.classesToSupport.listeners.BookmarkClick
import com.example.bestofbehance.classesToSupport.VIEW_MODE_GRIDVIEW
import com.example.bestofbehance.classesToSupport.VIEW_MODE_LISTVIEW
import com.example.bestofbehance.extension.MathObject


class PagingAdapterProfile(private val currentViewMode: String, val inClick: LayoutClick, val bookmarkClick: BookmarkClick) :
    PagedListAdapter<CardBinding, PagingAdapterProfile.ViewHolder>(diffCallback){

    lateinit var context: Context
    var position = 0
    var viewMode = currentViewMode

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        this.position = holder.adapterPosition
        //currentViewMode = preferences.stringGet(activity.resources.getString(R.string.current_view_mode_profile), currentViewMode)

        /*holder.itemView.avatarView.setOnClickListener {
            userClick.onUserClick(getItem(holder.adapterPosition)?.username!!)
        }*/

        holder.itemView.bookmark.isChecked = ProjectsDataBase.getDatabase(context)?.getProjectsDao()?.getById(getItem(position)!!.id!!) != null

        holder.itemView.bookmark.setOnClickListener {
            bookmarkClick.setPosition(position)
        }

        holder.itemView.constLayout.setOnClickListener {
            inClick.onItemClick(getItem(holder.adapterPosition)!!, holder.adapterPosition)
        }

            holder.itemView.avatarView.visibility = GONE
            holder.itemView.artistName.visibility = GONE

        var copyList = getItem(holder.adapterPosition)!!.copy()

        when (viewMode) {
            VIEW_MODE_GRIDVIEW -> {
                holder.itemView.avatarView.visibility = GONE

                copyList.views = MathObject.decimal(copyList.views.toString())
                copyList.appreciations = MathObject.decimal(copyList.appreciations.toString())
                copyList.comments = MathObject.decimal(copyList.comments.toString())
            }

            VIEW_MODE_LISTVIEW -> {
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
