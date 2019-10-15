package com.example.bestofbehance.dagger

import androidx.navigation.NavController
import com.example.bestofbehance.fragment.*

class FragmentNavigate(private val nav: NavController) {

    fun toDetailsFromBest(item: String) = nav.navigate(BestFragmentDirections.actionBestToDetails(item))

    fun toDetailsFromProjects(item: String) = nav.navigate(ProjectsFragmentDirections.actionProjectsToDetails(item))

    fun toProfileFromDetails(item: String) = nav.navigate(DetailsFragmentDirections.actionDetailsToProfile(item))

    fun toProfileFromBest(item: String) = nav.navigate(BestFragmentDirections.actionBestToProfile(item))

    fun toProfileFromProjects(item: String) = nav.navigate(ProjectsFragmentDirections.actionProjectsToProfile(item))

    fun toDetailsFromProfile(item: String) = nav.navigate(ProfileFragmentDirections.actionProfileToDetails(item))

    fun toProfileFromPeople(item: String) = nav.navigate(PeopleFragmentDirections.actionPeopleToProfile(item))
}