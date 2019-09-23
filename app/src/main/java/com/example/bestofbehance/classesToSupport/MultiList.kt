package com.example.bestofbehance.classesToSupport

import com.example.bestofbehance.binding.*

interface MultiList{
    class ImageList(val multiImage: ImageBinding) : MultiList
    class TextList(val multiText: TextBinding): MultiList
    class CountList(val multiCount: CountBinding): MultiList
    class CommentsList(val multiComment: CommentsBinding) : MultiList
    class PeopleList(val multiPeople: PeopleBinding) : MultiList {
        object ModelMapper {
            fun from(form: PeopleBinding) = PeopleList(form)
        }
    }

}

