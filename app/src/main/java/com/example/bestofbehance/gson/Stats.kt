package com.example.bestofbehance.gson

import com.google.gson.annotations.SerializedName

data class Stats(

	@field:SerializedName("comments")
	val comments: Int? = null,

	@field:SerializedName("appreciations")
	val appreciations: Int? = null,

	@field:SerializedName("views")
	val views: Int? = null,

	@field:SerializedName("followers")
	val followers: Int? = null,

	@field:SerializedName("following")
	val following: Int? = null
)