package com.example.bestofbehance.viewModels

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.example.bestofbehance.R
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.layout.BestDirections
import com.example.bestofbehance.layout.DetailsFragmentDirections
import com.example.bestofbehance.layout.ProjectsFragmentDirections


class NaviController(internal val activity: Context) {

    lateinit var context: FragmentActivity

    val controller = Navigation.findNavController(activity as Activity, R.id.fr)

    fun toDetailsFromBest(item: CardBinding) {
        controller.navigate(BestDirections.actionBestToDetails(item))
    }

    fun toDetailsFromProjects(item: CardBinding) {
        controller.navigate(ProjectsFragmentDirections.actionProjectsToDetails(item))
    }

    fun toProfileFromDetails(item: CardBinding) {
        controller.navigate(DetailsFragmentDirections.actionDetailsToProfile(item))
    }

    fun toProfileFromBest(item: CardBinding) {
        controller.navigate(BestDirections.actionBestToProfile(item))
    }

}