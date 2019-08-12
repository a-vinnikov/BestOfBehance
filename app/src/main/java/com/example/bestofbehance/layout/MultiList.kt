package com.example.bestofbehance.layout

import com.example.bestofbehance.binding.CommentsBinding
import com.example.bestofbehance.binding.CountBinding
import com.example.bestofbehance.binding.ImageBinding
import com.example.bestofbehance.binding.TextBinding

interface MultiList{
    class ImageList(val multiImage: ImageBinding) : MultiList
    class TextList(val multiText: TextBinding): MultiList
    class CountList(val multiCount: CountBinding): MultiList
    class CommentsList(val multiComment: CommentsBinding) : MultiList
}

