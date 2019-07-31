package com.example.bestofbehance.layout

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bestofbehance.R
import com.example.bestofbehance.databinding.CommentItemBinding
import com.example.bestofbehance.databinding.FragmentDetailsBinding
import com.example.bestofbehance.databinding.ListDetailsBinding
import com.example.bestofbehance.databinding.ListItemBinding
import com.example.bestofbehance.gson.CommentsBinding
import com.example.bestofbehance.gson.ImageBinding
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.comment_item.view.*
import java.text.SimpleDateFormat
import java.util.*


class AdapterGeneralComments(private val list: List<Ilist>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_IMAGE = 0
        private const val TYPE_COMMENTS = 1
    }

    lateinit var binding0: ListDetailsBinding
    lateinit var binding1: CommentItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_IMAGE -> {
            val inflater = LayoutInflater.from(parent.context)
            binding0 = ListDetailsBinding.inflate(inflater, parent, false)
            ImageViewHolder(binding0)
            //ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_details, parent, false))
        }
        TYPE_COMMENTS -> {
            val inflater = LayoutInflater.from(parent.context)
            binding1 = CommentItemBinding.inflate(inflater, parent, false)
            CommentsViewHolder(binding1)
        }
        else -> throw IllegalArgumentException()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (holder.itemViewType) {
            TYPE_IMAGE -> ImageViewHolder(binding0).onBindImage(list[position] as Ilist.ImageList)
            TYPE_COMMENTS -> CommentsViewHolder(binding1).onBindComments(list[position] as Ilist.CommentsList)
            else -> throw IllegalArgumentException()
        }

    override fun getItemCount() = list.count()

    override fun getItemViewType(position: Int): Int =
        when (list[position]) {
            is Ilist.ImageList -> TYPE_IMAGE
            is Ilist.CommentsList -> TYPE_COMMENTS
            else -> throw IllegalArgumentException()
        }

    class ImageViewHolder(private val binding: ListDetailsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBindImage(list: Ilist.ImageList) {
            binding.cardViewImage = list.imaList
            visibilityOfDescription(list)
            if (list.imaList.bigImageCard != null){
                ImageBinding().setImageUrl1(binding.bigImageCard, list.imaList.bigImageCard.toString())
            }
            /*holder.itemView.list_name1.text = list.generalCard.name?.trim()
            holder.itemView.list_post1.text = list.generalCard.post?.trim()
            setImageUrl(holder.itemView.bigImageView1, list.generalCard.bigImage.toString(), holder.itemView.context.resources.getString(R.string.not_rounded))
            setImageUrl(holder.itemView.avatarView1, list.generalCard.avatar.toString(), holder.itemView.context.resources.getString(R.string.not_rounded))

            ParseForVM().parseProject(list.generalCard.id!!.toInt()) { result ->
                if (result == "") holder.itemView.description.text = holder.itemView.context.resources.getString(R.string.not_description)
                else holder.itemView.description.text = result.toString().trim()
            }*/

        }

        fun visibilityOfDescription(list: Ilist.ImageList){
            if(list.imaList.description == null){
                binding.description.visibility = GONE
            } else{
                binding.description.visibility = View.VISIBLE
            }
        }
    }

    class CommentsViewHolder(private val binding: CommentItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private var i = 0

        fun onBindComments(list: Ilist.CommentsList) {
            binding.commentsView = list.comList
            numberOfComments(list)
            CommentsBinding().setImageUrl2(binding.commentAvatarView, list.comList.commentsAvatarView.toString())
            binding.dateText.text = getDateTime(list.comList.date.toString())
            /*holder.itemView.comment_name.text = list.comList.commentsName?.trim()
            holder.itemView.comment.text = list.comList.comment?.trim()
            */
            //setImageUrl(holder.itemView.commentAvatarView, list.comList.commentsAvatarView.toString(), holder.itemView.context.resources.getString(R.string.not_rounded))
        }

        @SuppressLint("SimpleDateFormat")
        private fun getDateTime(s: String): String? {
            return try {
                val sdf = SimpleDateFormat("dd MMMM yyyy")
                val netDate = Date(s.toLong() * 1000)
                sdf.format(netDate)
            } catch (e: Exception) {
                ""
            }
        }

        fun numberOfComments(list: Ilist.CommentsList){
            if(list.comList.commentsName == null){
                binding.commentAvatarView.visibility = GONE
                binding.dateText.visibility = GONE
                binding.commentLine.visibility = GONE
                i+1
            }else{
                binding.commentAvatarView.visibility = View.VISIBLE
                binding.dateText.visibility = View.VISIBLE
                binding.commentLine.visibility = View.VISIBLE
            }
        }

    }

    @BindingAdapter("loadingImage0")
    fun setImageUrl0(view: ImageView, url: String) {
        Glide.with(view.context).load(url).into(view)
    }
}
