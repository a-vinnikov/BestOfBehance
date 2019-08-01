package com.example.bestofbehance.paging

import androidx.recyclerview.widget.DiffUtil
import com.example.bestofbehance.gson.CardBinding


class DiffUtilForAdapter(private val oldList: MutableList<CardBinding>, private val newList: MutableList<CardBinding>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct = oldList[oldItemPosition]
        val newProduct = newList[newItemPosition]
        return oldProduct.id === newProduct.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct = oldList[oldItemPosition]
        val newProduct = newList[newItemPosition]
        return oldProduct.name.equals(newProduct.name) && oldProduct.post === newProduct.post
    }
}