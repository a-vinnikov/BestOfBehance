package com.example.bestofbehance.viewModels

import com.example.bestofbehance.gson.CardBinding

interface InClick{
    fun onItemClick(item: CardBinding)
    fun setPosition(position: Int)

}