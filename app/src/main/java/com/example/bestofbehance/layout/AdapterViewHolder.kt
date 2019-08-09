package com.example.bestofbehance.layout

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item.view.*
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.databinding.ListItemBinding
import com.example.bestofbehance.viewModels.InClick
import java.math.RoundingMode
import java.text.DecimalFormat
import androidx.recyclerview.widget.DiffUtil
import com.example.bestofbehance.room.DBMain
import com.example.bestofbehance.viewModels.BookmarkClick


class AdapterViewHolder(var list: MutableList<CardBinding>, val viewMode: String, val inClick: InClick, val bookmarkClick: BookmarkClick) :
    RecyclerView.Adapter<AdapterViewHolder.ViewHolder>() {

    lateinit var context: Context
    private var isLoading = false
    private val isLastPage = false
    var position = 0

    //PagedListAdapter<CardBinding, AdapterViewHolder.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        this.position = holder.adapterPosition
        Log.d("mLog", this.position.toString())

        holder.itemView.bookmark.isChecked = DBMain.find(context, list[holder.adapterPosition].id) != null

        holder.itemView.bookmark.setOnClickListener {
            bookmarkClick.setPosition(position)
        }


        holder.itemView.constLayout.setOnClickListener {
            inClick.onItemClick(list[holder.adapterPosition])
        }

        if (viewMode == "tile") {
            holder.itemView.avatarView.visibility = GONE
            val tValue = TypedValue()
            context.resources.getValue(com.example.bestofbehance.R.dimen.height_of_grid, tValue, true)
            val floatResources = tValue.float
            holder.itemView.bigImageView.layoutParams.height = floatResources.toInt()
        }

        val copyList = list[holder.adapterPosition].copy()
        if (viewMode == "tile") {
            copyList.views = holder.decimal(copyList.views.toString())
            copyList.appreciations = holder.decimal(copyList.appreciations.toString())
            copyList.comments = holder.decimal(copyList.comments.toString())

        }

        holder.bind(copyList)
    }

    fun addData(listData: MutableList<CardBinding>) {
        list.addAll(listData)
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size

    class ViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(Api: CardBinding) {
            binding.cardView = Api
            CardBinding().setRoundedImageUrl(binding.bigImageView, Api.bigImage.toString())
            CardBinding().setRoundedImageUrl(binding.avatarView, Api.avatar.toString())
        }

        fun decimal(numberString: String): String {
            var number = numberString
            if (number.toInt() > 1000) {
                var round = number.toInt()
                val df = DecimalFormat("#.#")
                round /= 1000
                df.roundingMode = RoundingMode.CEILING
                number = (df.format(round) + "k")
            }
            return number
        }

    }


    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<CardBinding>() {
            override fun areItemsTheSame(oldItem: CardBinding, newItem: CardBinding): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CardBinding, newItem: CardBinding): Boolean =
                oldItem == newItem
        }
    }

}
