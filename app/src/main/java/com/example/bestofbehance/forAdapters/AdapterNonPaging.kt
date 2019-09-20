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
import com.example.bestofbehance.R
import com.example.bestofbehance.classesToSupport.*
import com.example.bestofbehance.databases.ProjectsDataBase
import com.example.bestofbehance.fragments.VIEW_MODE_GRIDVIEW
import com.example.bestofbehance.fragments.VIEW_MODE_LISTVIEW


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
            context.resources.getString(R.string.best) -> { currentViewMode = SharedPreferenceObject.getSharedPreference(context, context.resources.getString(R.string.currentViewMode), currentViewMode)
            }
            context.resources.getString(R.string.projects_title) -> {currentViewMode = SharedPreferenceObject.getSharedPreference(context, context.resources.getString(R.string.currentViewModeProjects), currentViewMode)
            }
        }


        holder.itemView.avatarView.setOnClickListener {
            when(layout){
                context.resources.getString(R.string.best) -> {
                    NaviController(context).toProfileFromBest(list[holder.adapterPosition].username!!) }
                context.resources.getString(R.string.projects_title) -> {
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
