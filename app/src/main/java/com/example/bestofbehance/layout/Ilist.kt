package com.example.bestofbehance.layout

import com.example.bestofbehance.binding.CommentsBinding
import com.example.bestofbehance.binding.CountBinding
import com.example.bestofbehance.binding.ImageBinding
import com.example.bestofbehance.binding.TextBinding

interface Ilist{
    class ImageList(val imaList: ImageBinding) : Ilist
    class TextList(val textList: TextBinding): Ilist
    class CountList(val countList: CountBinding): Ilist
    class CommentsList(val comList: CommentsBinding) : Ilist
}

