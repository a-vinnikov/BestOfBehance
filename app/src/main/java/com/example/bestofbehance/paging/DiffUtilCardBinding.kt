package com.example.bestofbehance.paging

import androidx.recyclerview.widget.DiffUtil
import com.example.bestofbehance.gson.CardBinding


class DiffUtilCardBinding(private val oldList: MutableList<CardBinding>, private val newList: MutableList<CardBinding>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData = oldList[oldItemPosition]
        val newData = newList[newItemPosition]
        return oldData.id === newData.id
}

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData = oldList[oldItemPosition]
        val newData = newList[newItemPosition]
        return oldData.views == newData.views && oldData.appreciations == newData.appreciations && oldData.comments == newData.comments && oldData === newData
    }
}