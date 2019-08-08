package com.example.bestofbehance.layout

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bestofbehance.databinding.CommentItemBinding
import com.example.bestofbehance.binding.CommentsBinding
import com.example.bestofbehance.binding.ImageBinding
import com.example.bestofbehance.databinding.CountItemBinding
import com.example.bestofbehance.databinding.ListImageBinding
import com.example.bestofbehance.databinding.ListTextBinding
import java.text.SimpleDateFormat
import java.util.*


class AdapterGeneralComments(private val list: List<MultiList>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_IMAGE = 0
        private const val TYPE_TEXT = 1
        private const val TYPE_COUNT = 2
        private const val TYPE_COMMENTS = 3
    }

    lateinit var bindingImage: ListImageBinding
    lateinit var bindingText: ListTextBinding
    lateinit var bindingCount: CountItemBinding
    lateinit var bindingComments: CommentItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_IMAGE -> {
            val inflater = LayoutInflater.from(parent.context)
            bindingImage = ListImageBinding.inflate(inflater, parent, false)
            ImageViewHolder(bindingImage)
            //ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_image, parent, false))
        }
        TYPE_TEXT -> {
            val inflater = LayoutInflater.from(parent.context)
            bindingText = ListTextBinding.inflate(inflater, parent, false)
            TextViewHolder(bindingText)
            //ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_image, parent, false))
        }
        TYPE_COUNT -> {
            val inflater = LayoutInflater.from(parent.context)
            bindingCount = CountItemBinding.inflate(inflater, parent, false)
            CountViewHolder(bindingCount)
            //ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_image, parent, false))
        }
        TYPE_COMMENTS -> {
            val inflater = LayoutInflater.from(parent.context)
            bindingComments = CommentItemBinding.inflate(inflater, parent, false)
            CommentsViewHolder(bindingComments)
        }
        else -> throw IllegalArgumentException()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (holder.itemViewType) {
            TYPE_IMAGE -> ImageViewHolder(bindingImage).onBindImage(list[position] as MultiList.ImageList)
            TYPE_TEXT -> TextViewHolder(bindingText).onBindImage(list[position] as MultiList.TextList)
            TYPE_COUNT -> CountViewHolder(bindingCount).onBindImage(list[position] as MultiList.CountList)
            TYPE_COMMENTS -> CommentsViewHolder(bindingComments).onBindComments(list[position] as MultiList.CommentsList)
            else -> throw IllegalArgumentException()
        }

    override fun getItemCount() = list.count()

    override fun getItemViewType(position: Int): Int =
        when (list[position]) {
            is MultiList.ImageList -> TYPE_IMAGE
            is MultiList.TextList -> TYPE_TEXT
            is MultiList.CountList -> TYPE_COUNT
            is MultiList.CommentsList -> TYPE_COMMENTS
            else -> throw IllegalArgumentException()
        }

    class ImageViewHolder(private val binding: ListImageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBindImage(list: MultiList.ImageList) {
            binding.cardViewImage = list.imaList
            if (list.imaList.bigImageCard != null) {
                ImageBinding().setBigImageUrl(binding.bigImageCard, list.imaList.bigImageCard.toString())
            }

        }
    }
    class TextViewHolder(private val binding: ListTextBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBindImage(list: MultiList.TextList) {
            binding.cardViewText = list.textList

        }
    }
    class CountViewHolder(private val binding: CountItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBindImage(list: MultiList.CountList) {
            binding.countView = list.countList
        }
    }

    class CommentsViewHolder(private val binding: CommentItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBindComments(list: MultiList.CommentsList) {
            binding.commentsView = list.comList
            CommentsBinding().setImageUrlComment(binding.commentAvatarView, list.comList.commentAvatarView.toString())
            binding.dateText.text = getDateTime(list.comList.date.toString())
        }

        @SuppressLint("SimpleDateFormat")
        private fun getDateTime(timestamp: String): String? {
            return try {
                val sdf = SimpleDateFormat("dd MMMM yyyy")
                val netDate = Date(timestamp.toLong() * 1000)
                sdf.format(netDate)
            } catch (e: Exception) {
                ""
            }
        }

    }
}
