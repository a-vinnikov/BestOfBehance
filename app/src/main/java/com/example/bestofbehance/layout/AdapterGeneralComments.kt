package com.example.bestofbehance.layout

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bestofbehance.BR
import com.example.bestofbehance.databinding.CommentItemBinding
import com.example.bestofbehance.binding.CommentsBinding
import com.example.bestofbehance.binding.CountBinding
import com.example.bestofbehance.binding.ImageBinding
import com.example.bestofbehance.binding.TextBinding
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_IMAGE -> {
            val inflater = LayoutInflater.from(parent.context)
            ImageViewHolder(ListImageBinding.inflate(inflater, parent, false))
            //ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_image, parent, false))
        }
        TYPE_TEXT -> {
            val inflater = LayoutInflater.from(parent.context)
            TextViewHolder(ListTextBinding.inflate(inflater, parent, false))
            //ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_image, parent, false))
        }
        TYPE_COUNT -> {
            val inflater = LayoutInflater.from(parent.context)
            CountViewHolder(CountItemBinding.inflate(inflater, parent, false))
            //ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_image, parent, false))
        }
        TYPE_COMMENTS -> {
            val inflater = LayoutInflater.from(parent.context)
            CommentsViewHolder(CommentItemBinding.inflate(inflater, parent, false))
        }
        else -> throw IllegalArgumentException()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (holder) {
            is ImageViewHolder -> holder.onBindImage((list[position] as MultiList.ImageList).multiImage)
            is TextViewHolder -> holder.onBindImage((list[position] as MultiList.TextList).multiText)
            is CountViewHolder -> holder.onBindImage((list[position] as MultiList.CountList).multiCount)
            is CommentsViewHolder -> holder.onBindComments((list[position] as MultiList.CommentsList).multiComment)
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

        fun onBindImage(image: ImageBinding) {
            binding.cardViewImage = image
            binding.notifyPropertyChanged(BR._all)
        }
    }

    class TextViewHolder(private val binding: ListTextBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBindImage(text: TextBinding) {
            binding.cardViewText = text
            binding.notifyPropertyChanged(BR._all)
        }
    }

    class CountViewHolder(private val binding: CountItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBindImage(count: CountBinding) {
            binding.countsView = count
            binding.notifyPropertyChanged(BR._all)
        }
    }

    class CommentsViewHolder(private val binding: CommentItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBindComments(comment: CommentsBinding) {
            binding.commentsView = comment
            binding.notifyPropertyChanged(BR._all)
            //binding.dateText.text = getDateTime(comment.date.toString())
        }

    }
}
