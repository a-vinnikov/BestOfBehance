package com.example.bestofbehance.layout

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import com.example.bestofbehance.R
import kotlinx.android.synthetic.main.list_item.view.*
import com.example.bestofbehance.gson.CardBinding
import com.example.bestofbehance.databinding.ListItemBinding
import com.example.bestofbehance.viewModels.InClick
import java.math.RoundingMode
import java.text.DecimalFormat


class AdapterViewHolder(private val list: MutableList<CardBinding>, val ViewMode: Int, val inClick: InClick) :
    RecyclerView.Adapter<AdapterViewHolder.ViewHolder>() {

    lateinit var context: Context

    fun addData(list: MutableList<CardBinding>) {
        val size = list.size
        list.addAll(list)
        val sizeNew = list.size
        notifyItemRangeChanged(size, sizeNew)
    }

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
            context.resources.getValue(R.dimen.height_of_grid, out, true)
            val floatResources = out.float
            holder.itemView.bigImageView.layoutParams.height = floatResources.toInt()
        }

        if(holder.adapterPosition == list.size-1){
            Toast.makeText(context, "End", Toast.LENGTH_SHORT).show()
        }

        val copyList = list[holder.adapterPosition].copy()
        if (ViewMode == 1) {
            copyList.views = holder.decimal(copyList.views.toString())
            copyList.appreciations = holder.decimal(copyList.appreciations.toString())
            copyList.comments = holder.decimal(copyList.comments.toString())

        }

        holder.bind(copyList)
    }

    override fun getItemCount(): Int = list.size


    class ViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(Api: CardBinding) {
            binding.cardView = Api
            CardBinding().setImageUrl(binding.bigImageView, Api.bigImage.toString(), "rounded")
            CardBinding().setImageUrl(binding.avatarView, Api.avatar.toString(), "rounded")
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


}
