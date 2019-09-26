package com.example.bestofbehance.extension

import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.binding.PeopleBinding
import com.example.bestofbehance.binding.ProjectsBinding
import com.example.bestofbehance.binding.mapper.MapperForCardBinding
import com.example.bestofbehance.binding.mapper.MapperForMultiList
import com.example.bestofbehance.classesToSupport.MultiList

object Converter {
    fun convertProjectsToMulti(list: MutableList<PeopleBinding>?, myCallBack: (result: MutableList<MultiList>) -> Unit) {
        val listMulti: MutableList<MultiList> = mutableListOf()
        for (i in 0 until list!!.size) {
            listMulti.add(
                MapperForMultiList.from(list[i])
            )
        }
        myCallBack.invoke(listMulti)
    }

    fun convertProjectsToCard(list: MutableList<ProjectsBinding>, myCallBack: (result: MutableList<CardBinding>) -> Unit){
        val listCard: MutableList<CardBinding> = mutableListOf()
        for (i in 0 until list.size){
            listCard.add(MapperForCardBinding.from(list[i]))
        }
        myCallBack.invoke(listCard)
    }
}