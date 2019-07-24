package com.example.bestofbehance.gson

import com.google.gson.annotations.SerializedName

data class Responce(

	@field:SerializedName("projects")
	val projects: List<ProjectsItem?>? = null,

	@field:SerializedName("http_code")
	val httpCode: Int? = null
)