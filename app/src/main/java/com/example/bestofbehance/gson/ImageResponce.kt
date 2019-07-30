package com.example.bestofbehance.gson

import com.google.gson.annotations.SerializedName


data class ImageResponce(

	@field:SerializedName("http_code")
	val httpCode: Int? = null,

	@field:SerializedName("project")
	val project: Project? = null
)