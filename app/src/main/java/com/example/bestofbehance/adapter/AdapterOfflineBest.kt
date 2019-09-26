package com.example.bestofbehance.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item.view.*
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.databinding.ListItemBinding
import com.example.bestofbehance.BR
import com.example.bestofbehance.R
import com.example.bestofbehance.classesToSupport.*
import com.example.bestofbehance.module.FragmentNavigate
import com.example.bestofbehance.database.ProjectsDataBase
import com.example.bestofbehance.extension.MathObject
import com.example.bestofbehance.module.StorageModule


class AdapterOfflineBest(var list: MutableList<CardBinding>, val inClick: InClick, val bookmarkClick: BookmarkClick) : RecyclerView.Adapter<AdapterOfflineBest.ViewHolder>() {

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
            FragmentNavigate(context).toProfileFromBest(list[holder.adapterPosition].username!!)
        }

        holder.itemView.bookmark.isChecked = ProjectsDataBase.getDatabase(context)?.getProjectsDao()?.getById(list[position].id!!) != null

        holder.itemView.bookmark.setOnClickListener { bookmarkClick.setPosition(position) }

        holder.itemView.constLayout.setOnClickListener {
            inClick.onItemClick(list[holder.adapterPosition], holder.adapterPosition)
        }

        var copyList = list[holder.adapterPosition].copy()

        when (currentViewMode) {
            VIEW_MODE_GRIDVIEW -> {
                holder.itemView.avatarView.visibility = GONE

                copyList.views = MathObject.decimal(copyList.views.toString())
                copyList.appreciations = MathObject.decimal(copyList.appreciations.toString())
                copyList.comments = MathObject.decimal(copyList.comments.toString())
            }

            VIEW_MODE_LISTVIEW -> {
                holder.itemView.avatarView.visibility = View.VISIBLE
                copyList = list[holder.adapterPosition].copy()
            }
        }

        holder.bind(copyList)
    }

    override fun getItemCount() = list.size

    class ViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(Api: CardBinding) {
            binding.cardView = Api
            binding.notifyPropertyChanged(BR._all)
        }

    }
}
