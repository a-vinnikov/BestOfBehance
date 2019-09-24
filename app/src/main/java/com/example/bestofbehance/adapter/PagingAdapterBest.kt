package com.example.bestofbehance.adapter

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
import com.example.bestofbehance.BR
import com.example.bestofbehance.R
import com.example.bestofbehance.database.ProjectsDataBase
import com.example.bestofbehance.classesToSupport.BookmarkClick
import com.example.bestofbehance.extensions.MathObject
import com.example.bestofbehance.dagger.NavigateModule
import com.example.bestofbehance.dagger.StorageModule
import com.example.bestofbehance.fragment.VIEW_MODE_GRIDVIEW
import com.example.bestofbehance.fragment.VIEW_MODE_LISTVIEW


class PagingAdapterBest(val inClick: InClick, val bookmarkClick: BookmarkClick) :
    PagedListAdapter<CardBinding, PagingAdapterBest.ViewHolder>(diffCallback) {

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
        currentViewMode = StorageModule.getPreferences(context, context.resources.getString(R.string.current_view_mode), currentViewMode)



        holder.itemView.avatarView.setOnClickListener {
            NavigateModule(context).toProfileFromBest(getItem(holder.adapterPosition)?.username!!)
        }

        holder.itemView.bookmark.isChecked = ProjectsDataBase.getDatabase(context)?.getProjectsDao()?.getById(getItem(position)!!.id!!) != null

        holder.itemView.bookmark.setOnClickListener {
            bookmarkClick.setPosition(position)
        }

        holder.itemView.constLayout.setOnClickListener {
            inClick.onItemClick(getItem(holder.adapterPosition)!!, holder.adapterPosition)
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
                holder.itemView.avatarView.visibility = VISIBLE
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
