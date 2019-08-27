package com.example.bestofbehance.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bestofbehance.BR
import com.example.bestofbehance.binding.*
import com.example.bestofbehance.databases.DBProjectsDao.getCurrentDateTime
import com.example.bestofbehance.databases.forRoom.PeopleDataBase
import com.example.bestofbehance.databases.forRoom.ProjectsDataBase
import com.example.bestofbehance.databinding.*
import com.example.bestofbehance.viewModels.NaviController
import kotlinx.android.synthetic.main.people_item.view.*


class AdapterMulti(private val list: List<MultiList>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context

    companion object {
        private const val TYPE_IMAGE = 0
        private const val TYPE_TEXT = 1
        private const val TYPE_COUNT = 2
        private const val TYPE_COMMENTS = 3
        private const val TYPE_PEOPLE = 4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_IMAGE -> {
            val inflater = LayoutInflater.from(parent.context)
            ImageViewHolder(ListImageBinding.inflate(inflater, parent, false))
        }
        TYPE_TEXT -> {
            val inflater = LayoutInflater.from(parent.context)
            TextViewHolder(ListTextBinding.inflate(inflater, parent, false))
        }
        TYPE_COUNT -> {
            val inflater = LayoutInflater.from(parent.context)
            CountViewHolder(CountItemBinding.inflate(inflater, parent, false))
        }
        TYPE_COMMENTS -> {
            val inflater = LayoutInflater.from(parent.context)
            CommentsViewHolder(CommentItemBinding.inflate(inflater, parent, false))
        }
        TYPE_PEOPLE -> {
            val inflater = LayoutInflater.from(parent.context)
            context = parent.context
            PeopleViewHolder(PeopleItemBinding.inflate(inflater, parent, false))
        }
        else -> throw IllegalArgumentException()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (holder) {
            is ImageViewHolder -> holder.onBindImage((list[position] as MultiList.ImageList).multiImage)
            is TextViewHolder -> holder.onBindImage((list[position] as MultiList.TextList).multiText)
            is CountViewHolder -> holder.onBindImage((list[position] as MultiList.CountList).multiCount)
            is CommentsViewHolder -> holder.onBindComments((list[position] as MultiList.CommentsList).multiComment)
            is PeopleViewHolder -> holder.onBindPeople((list[position] as MultiList.PeopleList).multiPeople, context)
            else -> throw IllegalArgumentException()
        }

    override fun getItemCount() = list.count()

    override fun getItemViewType(position: Int): Int =
        when (list[position]) {
            is MultiList.ImageList -> TYPE_IMAGE
            is MultiList.TextList -> TYPE_TEXT
            is MultiList.CountList -> TYPE_COUNT
            is MultiList.CommentsList -> TYPE_COMMENTS
            is MultiList.PeopleList -> TYPE_PEOPLE
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
        }

    }

    class PeopleViewHolder(private val binding: PeopleItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBindPeople(people: PeopleBinding, context: Context) {
            binding.peopleView = people
            binding.notifyPropertyChanged(BR._all)

            itemView.bookmark_people.isChecked = PeopleDataBase.getDatabase(context)?.getPeopleDao()?.getByUsername(people.username!!) != null
            itemView.peopleLayout.setOnClickListener { NaviController(context).toProfileFromPeople(people.id.toString()) }
            itemView.bookmark_people.setOnClickListener {
                if (PeopleDataBase.getDatabase(context)?.getPeopleDao()?.getByUsername(people.username!!) != null) {
                    PeopleDataBase.getDatabase(context)?.getPeopleDao()?.deleteByUsername(people.username!!)
            } }
        }

    }
}
