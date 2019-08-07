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
import com.example.bestofbehance.R
import com.example.bestofbehance.databinding.CommentItemBinding
import com.example.bestofbehance.binding.CommentsBinding
import com.example.bestofbehance.binding.ImageBinding
import com.example.bestofbehance.databinding.CountItemBinding
import com.example.bestofbehance.databinding.ListImageBinding
import com.example.bestofbehance.databinding.ListTextBinding
import kotlinx.android.synthetic.main.count_item.view.*
import java.text.SimpleDateFormat
import java.util.*


class AdapterGeneralComments(private val list: List<Ilist>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_IMAGE = 0
        private const val TYPE_TEXT = 1
        private const val TYPE_COUNT = 2
        private const val TYPE_COMMENTS = 3
    }

    lateinit var binding0: ListImageBinding
    lateinit var binding1: ListTextBinding
    lateinit var binding2: CountItemBinding
    lateinit var binding3: CommentItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_IMAGE -> {
            val inflater = LayoutInflater.from(parent.context)
            binding0 = ListImageBinding.inflate(inflater, parent, false)
            ImageViewHolder(binding0)
            //ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_image, parent, false))
        }
        TYPE_TEXT -> {
            val inflater = LayoutInflater.from(parent.context)
            binding1 = ListTextBinding.inflate(inflater, parent, false)
            TextViewHolder(binding1)
            //ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_image, parent, false))
        }
        TYPE_COUNT -> {
            val inflater = LayoutInflater.from(parent.context)
            binding2 = CountItemBinding.inflate(inflater, parent, false)
            CountViewHolder(binding2)
            //ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_image, parent, false))
        }
        TYPE_COMMENTS -> {
            val inflater = LayoutInflater.from(parent.context)
            binding3 = CommentItemBinding.inflate(inflater, parent, false)
            CommentsViewHolder(binding3)
        }
        else -> throw IllegalArgumentException()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (holder.itemViewType) {
            TYPE_IMAGE -> ImageViewHolder(binding0).onBindImage(list[position] as Ilist.ImageList)
            TYPE_TEXT -> TextViewHolder(binding1).onBindImage(list[position] as Ilist.TextList)
            TYPE_COUNT -> CountViewHolder(binding2).onBindImage(list[position] as Ilist.CountList)
            TYPE_COMMENTS -> CommentsViewHolder(binding3).onBindComments(list[position] as Ilist.CommentsList)
            else -> throw IllegalArgumentException()
        }

    override fun getItemCount() = list.count()

    override fun getItemViewType(position: Int): Int =
        when (list[position]) {
            is Ilist.ImageList -> TYPE_IMAGE
            is Ilist.TextList -> TYPE_TEXT
            is Ilist.CountList -> TYPE_COUNT
            is Ilist.CommentsList -> TYPE_COMMENTS
            else -> throw IllegalArgumentException()
        }

    class ImageViewHolder(private val binding: ListImageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBindImage(list: Ilist.ImageList) {
            binding.cardViewImage = list.imaList
            if (list.imaList.bigImageCard != null) {
                ImageBinding().setImageUrl1(binding.bigImageCard, list.imaList.bigImageCard.toString())
            }

        }
    }
    class TextViewHolder(private val binding: ListTextBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBindImage(list: Ilist.TextList) {
            binding.cardViewText = list.textList

        }
    }
    class CountViewHolder(private val binding: CountItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBindImage(list: Ilist.CountList) {
            binding.countView = list.countList
        }
    }

    class CommentsViewHolder(private val binding: CommentItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBindComments(list: Ilist.CommentsList) {
            binding.commentsView = list.comList
            CommentsBinding().setImageUrl2(binding.commentAvatarView, list.comList.commentsAvatarView.toString())
            binding.dateText.text = getDateTime(list.comList.date.toString())
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

    }

    @BindingAdapter("loadingImage0")
    fun setImageUrl0(view: ImageView, url: String) {
        Glide.with(view.context).load(url).into(view)
    }
}
