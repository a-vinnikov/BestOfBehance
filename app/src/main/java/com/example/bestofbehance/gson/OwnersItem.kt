package com.example.bestofbehance.gson

import com.google.gson.annotations.SerializedName

data class OwnersItem(

	@field:SerializedName("imagesGeneral")
	val imagesGeneral: ImagesGeneral? = null,

	@field:SerializedName("display_name")
	val displayName: String? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("stats")
	val stats: Stats? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("fields")
	val fields: List<String?>? = null
)