package com.example.bestofbehance.gson

import com.google.gson.annotations.SerializedName

data class CommentsMain(

	@field:SerializedName("comments")
	val comments: List<CommentsItem?>? = null,

	@field:SerializedName("http_code")
	val httpCode: Int? = null
)