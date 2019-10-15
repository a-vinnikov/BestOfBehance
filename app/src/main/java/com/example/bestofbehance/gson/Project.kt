package com.example.bestofbehance.gson

import com.google.gson.annotations.SerializedName

data class Project(

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("owners")
	val owners: List<OwnersItem?>? = null,

	@field:SerializedName("stats")
	val stats: Stats? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("slug")
	val slug: String? = null,

	@field:SerializedName("covers")
	val covers: Covers? = null,

	@field:SerializedName("published_on")
	val publishedOn: Int? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("modules")
	val modules: List<ModulesItem?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("fields")
	val fields: List<String?>? = null
)