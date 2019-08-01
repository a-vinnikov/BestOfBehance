package com.example.bestofbehance.layout

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagedListAdapter
import kotlinx.android.synthetic.main.list_item.view.*
import com.example.bestofbehance.gson.CardBinding
import com.example.bestofbehance.databinding.ListItemBinding
import com.example.bestofbehance.viewModels.InClick
import java.math.RoundingMode
import java.text.DecimalFormat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bestofbehance.paging.PaginationScrollListener
import kotlinx.android.synthetic.main.fragment_best.*
import kotlinx.android.synthetic.main.fragment_best.view.*


class AdapterViewHolder(val recyclerView: RecyclerView, val list: MutableList<CardBinding>, val ViewMode: Int, val inClick: InClick) :
    RecyclerView.Adapter<AdapterViewHolder.ViewHolder>() {

    lateinit var context: Context
    private var isLoading = false
    private val isLastPage = false

    //PagedListAdapter<CardBinding, AdapterViewHolder.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.constLayout.setOnClickListener {
            inClick.onItemClick(list[holder.adapterPosition])
            inClick.setPosition(holder.adapterPosition)
        }

        if (ViewMode == 1) {
            holder.itemView.avatarView.visibility = GONE
            val out = TypedValue()
            context.resources.getValue(com.example.bestofbehance.R.dimen.height_of_grid, out, true)
            val floatResources = out.float
            holder.itemView.bigImageView.layoutParams.height = floatResources.toInt()
        }

        if(holder.adapterPosition == list.size){
            Toast.makeText(context, "End", Toast.LENGTH_SHORT).show()
        }

        val copyList = list[holder.adapterPosition].copy()
        if (ViewMode == 1) {
            copyList.views = holder.decimal(copyList.views.toString())
            copyList.appreciations = holder.decimal(copyList.appreciations.toString())
            copyList.comments = holder.decimal(copyList.comments.toString())

        }

 /*       recyclerView.addOnScrollListener(object : PaginationScrollListener(LinearLayoutManager(context)) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                getMoreItems()
            }
        })*/

        holder.bind(copyList)
    }

/*    fun getMoreItems() {
        isLoading = false
        addData(list)
    }

    fun addData(list: MutableList<CardBinding>) {
        val size = list.size
        list.addAll(list)
        val sizeNew = list.size
        notifyItemRangeChanged(size, sizeNew)
        //adapterAbc.notifyDataSetChanged()
    }*/

    override fun getItemCount() = list.size

    class ViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(Api: CardBinding) {
            binding.cardView = Api
            CardBinding().setRoundedImageUrl(binding.bigImageView, Api.bigImage.toString())
            CardBinding().setRoundedImageUrl(binding.avatarView, Api.avatar.toString())
        }

        fun decimal(number: String): String {
            var numberK = number
            if (numberK.toInt() > 1000) {
                var round = numberK.toInt()
                val df = DecimalFormat("#.#")
                round /= 1000
                df.roundingMode = RoundingMode.CEILING
                numberK = (df.format(round) + "k")
            }
            return numberK
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
