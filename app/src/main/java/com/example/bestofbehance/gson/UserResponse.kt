package com.example.bestofbehance.gson

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("http_code")
	val httpCode: Int? = null,

	@field:SerializedName("user")
	val user: User? = null
)