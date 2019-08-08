package com.example.bestofbehance.layout

import com.example.bestofbehance.binding.CommentsBinding
import com.example.bestofbehance.binding.CountBinding
import com.example.bestofbehance.binding.ImageBinding
import com.example.bestofbehance.binding.TextBinding

interface MultiList{
    class ImageList(val imaList: ImageBinding) : MultiList
    class TextList(val textList: TextBinding): MultiList
    class CountList(val countList: CountBinding): MultiList
    class CommentsList(val comList: CommentsBinding) : MultiList
}

