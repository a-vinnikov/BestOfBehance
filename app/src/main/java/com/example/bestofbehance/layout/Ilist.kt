package com.example.bestofbehance.layout

import com.example.bestofbehance.gson.CommentsBinding
import com.example.bestofbehance.gson.ImageBinding

interface Ilist{
    class ImageList(val imaList: ImageBinding) : Ilist
    class CommentsList(val comList: CommentsBinding) : Ilist
}

