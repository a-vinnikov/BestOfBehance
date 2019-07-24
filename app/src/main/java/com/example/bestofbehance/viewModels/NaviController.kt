package com.example.bestofbehance.viewModels

import android.support.v4.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.bestofbehance.R
import java.nio.file.Files.find


class NaviController(internal val activity: FragmentActivity?) {

    lateinit var context: FragmentActivity

    val controller = Navigation.findNavController(activity!!, R.id.fr)

    fun toDetails() {
        controller.navigate(R.id.details)
    }
}