package com.example.bestofbehance.gson

import com.google.gson.annotations.SerializedName

data class FeaturesItem(

	@field:SerializedName("site")
	val site: Site? = null,

	@field:SerializedName("projects")
	val projects: List<ProjectsItem?>? = null,

	@field:SerializedName("num_of_projects")
	val numOfProjects: Int? = null
)