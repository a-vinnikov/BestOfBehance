package com.example.bestofbehance.classesToSupport

import com.example.bestofbehance.binding.CardBinding

interface InClick{
    fun onItemClick(item: CardBinding, position: Int)
}