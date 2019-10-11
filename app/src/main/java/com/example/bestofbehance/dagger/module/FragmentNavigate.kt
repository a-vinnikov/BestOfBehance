package com.example.bestofbehance.dagger.module

import android.app.Activity
import android.content.Context
import androidx.navigation.Navigation
import com.example.bestofbehance.R
import com.example.bestofbehance.fragment.*

class FragmentNavigate(activity: Context) {

    val controller = Navigation.findNavController(activity as Activity, R.id.fr)

    fun toDetailsFromBest(item: String) {
        controller.navigate(BestDirections.actionBestToDetails(item))
    }

    fun toDetailsFromProjects(item: String) {
        controller.navigate(ProjectsFragmentDirections.actionProjectsToDetails(item))
    }

    fun toProfileFromDetails(item: String) {
        controller.navigate(DetailsFragmentDirections.actionDetailsToProfile(item))
    }

    fun toProfileFromBest(item: String) {
        controller.navigate(BestDirections.actionBestToProfile(item))
    }

    fun toProfileFromProjects(item: String) {
        controller.navigate(ProjectsFragmentDirections.actionProjectsToProfile(item))
    }

    fun toDetailsFromProfile(item: String){
        controller.navigate(ProfileFragmentDirections.actionProfileToDetails(item))
    }

    fun toProfileFromPeople(item: String){
        controller.navigate(PeopleFragmentDirections.actionPeopleToProfile(item))
    }

}