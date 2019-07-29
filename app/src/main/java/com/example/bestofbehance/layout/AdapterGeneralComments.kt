package com.example.bestofbehance.layout

import android.annotation.SuppressLint
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bestofbehance.R
import com.example.bestofbehance.databinding.ListItemBinding
import com.example.bestofbehance.viewModels.ParseForVM
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.comment_item.view.*
import kotlinx.android.synthetic.main.list_details.*
import kotlinx.android.synthetic.main.list_details.view.*
import java.text.SimpleDateFormat
import java.util.*


class AdapterGeneralComments(private val list: MutableList<Ilist>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (holder.itemViewType) {
            //TYPE_GENERAL -> onBindGeneral(holder, list[position] as Ilist.GeneralList)
            TYPE_COMMENTS -> onBindComments(holder, list[position] as Ilist.CommentsList)
            else -> throw IllegalArgumentException()
        }

    override fun getItemCount() = list.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        /*TYPE_GENERAL -> {
            *//*val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemBinding.inflate(inflater)
            GeneralViewHolder(binding)*//*
            GeneralViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_details, parent, false))
        }*/
        TYPE_COMMENTS -> CommentsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false))
        else -> throw IllegalArgumentException()
    }

    companion object {
        //private const val TYPE_GENERAL = 0
        private const val TYPE_COMMENTS = 1
    }

    override fun getItemViewType(position: Int): Int =
        when (list[position]) {
            //is Ilist.GeneralList -> TYPE_GENERAL
            is Ilist.CommentsList -> TYPE_COMMENTS
            else -> throw IllegalArgumentException()
        }

    fun onBindGeneral(holder: RecyclerView.ViewHolder, list: Ilist.GeneralList) {
        /*holder.itemView.list_name1.text = list.generalCard.name?.trim()
        holder.itemView.list_post1.text = list.generalCard.post?.trim()
        setImageUrl(holder.itemView.bigImageView1, list.generalCard.bigImage.toString(), holder.itemView.context.resources.getString(R.string.not_rounded))
        setImageUrl(holder.itemView.avatarView1, list.generalCard.avatar.toString(), holder.itemView.context.resources.getString(R.string.not_rounded))

        ParseForVM().parseDescription(list.generalCard.id!!.toInt()) { result ->
            if (result == "") holder.itemView.description.text = holder.itemView.context.resources.getString(R.string.not_description)
            else holder.itemView.description.text = result.toString().trim()
        }*/

    }

    fun onBindComments(holder: RecyclerView.ViewHolder, list: Ilist.CommentsList) {
        holder.itemView.comment_name.text = list.comList.commentsName?.trim()
        holder.itemView.comment.text = list.comList.comment?.trim()
        holder.itemView.date_text.text = getDateTime(list.comList.date.toString())
        setImageUrl(holder.itemView.commentAvatarView, list.comList.commentsAvatarView.toString(), holder.itemView.context.resources.getString(R.string.not_rounded))
    }

    class GeneralViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    fun setImageUrl(view: ImageView, url: String, rounded: String) {
        if (rounded == "rounded") {
            Glide.with(view.context).load(url).apply(
                RequestOptions.bitmapTransform(
                    RoundedCornersTransformation(
                        15,
                        0
                    )
                )
            ).into(view)
        } else if (rounded == "not rounded") {
            Glide.with(view.context).load(url).into(view)
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateTime(s: String): String? {
        return try {
            val sdf = SimpleDateFormat("dd MMMM yyyy")
            val netDate = Date(s.toLong() * 1000)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }
}
