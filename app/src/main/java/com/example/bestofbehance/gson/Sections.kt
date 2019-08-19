package com.example.bestofbehance.gson

import com.google.gson.annotations.SerializedName

data class Sections(

	@field:SerializedName("About Me")
	val aboutMe: String? = null
)