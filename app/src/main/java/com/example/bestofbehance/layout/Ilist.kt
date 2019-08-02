package com.example.bestofbehance.layout

import com.example.bestofbehance.binding.CommentsBinding
import com.example.bestofbehance.binding.ImageBinding

interface Ilist{
    class ImageList(val imaList: ImageBinding) : Ilist
    class CommentsList(val comList: CommentsBinding) : Ilist
}

