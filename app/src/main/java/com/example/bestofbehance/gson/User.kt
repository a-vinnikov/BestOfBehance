package com.example.bestofbehance.gson

import com.google.gson.annotations.SerializedName

data class User(

	@field:SerializedName("imagesGeneral")
	val images: Images? = null,

	@field:SerializedName("display_name")
	val displayName: String? = null
)