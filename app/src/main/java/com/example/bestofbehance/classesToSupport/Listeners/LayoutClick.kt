package com.example.bestofbehance.classesToSupport.listeners

import com.example.bestofbehance.binding.CardBinding

interface LayoutClick{
    fun onItemClick(item: CardBinding, position: Int)
}