package com.example.bestofbehance.gson

import com.google.gson.annotations.SerializedName

data class ProjectsItem(

	@field:SerializedName("owners")
	val owners: List<OwnersItem?>? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("stats")
	val stats: Stats? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("fields")
	val fields: List<String?>? = null,

	@field:SerializedName("covers")
	val covers: Covers? = null

)