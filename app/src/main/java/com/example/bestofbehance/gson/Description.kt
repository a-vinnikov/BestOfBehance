package com.example.bestofbehance.gson

import com.google.gson.annotations.SerializedName

data class Description(

	@field:SerializedName("http_code")
	val httpCode: Int? = null,

	@field:SerializedName("project")
	val project: Project? = null
)