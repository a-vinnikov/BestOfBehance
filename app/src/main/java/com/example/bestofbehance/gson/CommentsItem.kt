package com.example.bestofbehance.gson

import com.google.gson.annotations.SerializedName

data class CommentsItem(

	@field:SerializedName("created_on")
	val createdOn: Int? = null,

	@field:SerializedName("comment")
	val comment: String? = null,

	@field:SerializedName("user")
	val user: User? = null
)