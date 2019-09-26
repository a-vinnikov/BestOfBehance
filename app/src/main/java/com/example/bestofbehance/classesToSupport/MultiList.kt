package com.example.bestofbehance.classesToSupport

import com.example.bestofbehance.binding.*
import com.example.bestofbehance.binding.detailsBinding.CommentsBinding
import com.example.bestofbehance.binding.detailsBinding.CountBinding
import com.example.bestofbehance.binding.detailsBinding.ImageBinding
import com.example.bestofbehance.binding.detailsBinding.TextBinding

interface MultiList{
    class ImageList(val multiImage: ImageBinding) : MultiList
    class TextList(val multiText: TextBinding): MultiList
    class CountList(val multiCount: CountBinding): MultiList
    class CommentsList(val multiComment: CommentsBinding) : MultiList
    class PeopleList(val multiPeople: PeopleBinding) : MultiList

}

