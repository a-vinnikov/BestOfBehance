package com.example.bestofbehance.binding.mapper

import com.example.bestofbehance.binding.PeopleBinding
import com.example.bestofbehance.classesToSupport.MultiList

object MapperForMultiList {
    fun from(form: PeopleBinding) = MultiList.PeopleList(form)
}