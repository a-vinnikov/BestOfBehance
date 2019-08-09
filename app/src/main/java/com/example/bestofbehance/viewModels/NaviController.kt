package com.example.bestofbehance.viewModels

import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.example.bestofbehance.R
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.layout.BestDirections
import com.example.bestofbehance.layout.ProjectsFragmentDirections


class NaviController(internal val activity: FragmentActivity?) {

    lateinit var context: FragmentActivity

    val controller = Navigation.findNavController(activity!!, R.id.fr)

    fun toDetailsFromBest(item: CardBinding) {
        controller.navigate(BestDirections.actionBestToDetails(item))
    }

    fun toDetailsFromProjects(item: CardBinding) {
        controller.navigate(ProjectsFragmentDirections.actionProjectsToDetails(item))
    }

}