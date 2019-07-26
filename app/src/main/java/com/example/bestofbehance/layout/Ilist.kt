package com.example.bestofbehance.layout

import com.example.bestofbehance.gson.CardBinding
import com.example.bestofbehance.gson.CommentsBinding

interface Ilist{
    class GeneralList(val generalCard: CardBinding) : Ilist
    class CommentsList(val comList: CommentsBinding) : Ilist
}

