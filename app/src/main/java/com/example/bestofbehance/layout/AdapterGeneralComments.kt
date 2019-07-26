package com.example.bestofbehance.layout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bestofbehance.R
import kotlinx.android.synthetic.main.comment_item.view.*
import kotlinx.android.synthetic.main.list_details.view.*



class AdapterGeneralComments(private val list: MutableList<Ilist>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (holder.itemViewType) {
            TYPE_GENERAL -> onBindGeneral(holder, list[position] as Ilist.GeneralList)
            TYPE_COMMENTS -> onBindComments(holder, list[position] as Ilist.CommentsList)
            else -> throw IllegalArgumentException()
        }

    override fun getItemCount() = list.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_GENERAL -> GeneralViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_details,
                parent,
                false
            )
        )
        TYPE_COMMENTS -> CommentsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.comment_item,
                parent,
                false
            )
        )
        else -> throw IllegalArgumentException()
    }

    companion object {
        private const val TYPE_GENERAL = 0
        private const val TYPE_COMMENTS = 1
    }

    override fun getItemViewType(position: Int): Int =
        when (list[position]) {
            is Ilist.GeneralList -> TYPE_GENERAL
            is Ilist.CommentsList -> TYPE_COMMENTS
            else -> throw IllegalArgumentException()
        }

    fun onBindGeneral(holder: RecyclerView.ViewHolder, list: Ilist.GeneralList) {
        holder.itemView.list_name1.text = "jpga"
    }

    fun onBindComments(holder: RecyclerView.ViewHolder, list: Ilist.CommentsList) {
        holder.itemView.comment_name.text = "pepega"
    }

    class GeneralViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}
