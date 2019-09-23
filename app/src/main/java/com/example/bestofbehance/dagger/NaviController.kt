package com.example.bestofbehance.dagger

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.example.bestofbehance.R
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.fragment.*
import dagger.Module
import javax.inject.Singleton

@Module
class NaviController(activity: Context) {

    lateinit var context: FragmentActivity

    val controller = Navigation.findNavController(activity as Activity, R.id.fr)

    @Singleton
    fun toDetailsFromBest(item: CardBinding) {
        controller.navigate(BestDirections.actionBestToDetails(item))
    }

    @Singleton
    fun toDetailsFromProjects(item: CardBinding) {
        controller.navigate(ProjectsFragmentDirections.actionProjectsToDetails(item))
    }

    @Singleton
    fun toProfileFromDetails(item: String) {
        controller.navigate(DetailsFragmentDirections.actionDetailsToProfile(item))
    }

    @Singleton
    fun toProfileFromBest(item: String) {
        controller.navigate(BestDirections.actionBestToProfile(item))
    }

    @Singleton
    fun toProfileFromProjects(item: String) {
        controller.navigate(ProjectsFragmentDirections.actionProjectsToProfile(item))
    }

    @Singleton
    fun toDetailsFromProfile(item: CardBinding){
        controller.navigate(ProfileFragmentDirections.actionProfileToDetails(item))
    }

    @Singleton
    fun toProfileFromPeople(item: String){
        controller.navigate(PeopleFragmentDirections.actionPeopleToProfile(item))
    }

}