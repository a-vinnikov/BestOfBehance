package com.example.bestofbehance.viewModels

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.example.bestofbehance.R
import com.example.bestofbehance.gson.CardBinding
import com.example.bestofbehance.layout.BestDirections


class NaviController(internal val activity: FragmentActivity?) {

    lateinit var context: FragmentActivity

    val controller = Navigation.findNavController(activity!!, R.id.fr)

    fun toDetails(item: CardBinding) {
        controller.navigate(BestDirections.actionBestToDetails(item))
    }
}