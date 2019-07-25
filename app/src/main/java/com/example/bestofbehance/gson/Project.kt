package com.example.bestofbehance.gson

import com.google.gson.annotations.SerializedName


data class Project(

	@field:SerializedName("description")
	val description: String? = null

)