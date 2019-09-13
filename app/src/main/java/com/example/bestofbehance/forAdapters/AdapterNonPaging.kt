package com.example.bestofbehance.forAdapters

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
import com.example.bestofbehance.classesToSupport.*
import com.example.bestofbehance.databases.ProjectsDataBase


class AdapterNonPaging(var list: MutableList<CardBinding>, val inClick: InClick, val bookmarkClick: BookmarkClick, val layout: String) : RecyclerView.Adapter<AdapterNonPaging.ViewHolder>() {

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
            "Best" -> { currentViewMode = SharedPreferenceObject.sharedCurrentViewMode(context, "currentViewMode", currentViewMode)
            }
            "Projects" -> {currentViewMode = SharedPreferenceObject.sharedCurrentViewMode(context, "currentViewModeProjects", currentViewMode)
            }
        }


        holder.itemView.avatarView.setOnClickListener {
            when(layout){
                "Best" -> {
                    NaviController(context).toProfileFromBest(list[holder.adapterPosition].username!!) }
                "Projects" -> {
                    NaviController(context).toProfileFromProjects(list[holder.adapterPosition].username!!) }
            }
        }

        holder.itemView.bookmark.isChecked = ProjectsDataBase.getDatabase(context)?.getProjectsDao()?.getById(list[position].id!!) != null

        holder.itemView.bookmark.setOnClickListener {
            bookmarkClick.setPosition(position)
        }

        holder.itemView.constLayout.setOnClickListener {
            inClick.onItemClick(list[holder.adapterPosition], holder.adapterPosition)
        }

        var copyList = list[holder.adapterPosition].copy()

        when (currentViewMode) {
            "tile" -> {
                holder.itemView.avatarView.visibility = GONE

                copyList.views = MathObject.decimal(copyList.views.toString())
                copyList.appreciations = MathObject.decimal(copyList.appreciations.toString())
                copyList.comments = MathObject.decimal(copyList.comments.toString())
            }

            "list" -> {
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
