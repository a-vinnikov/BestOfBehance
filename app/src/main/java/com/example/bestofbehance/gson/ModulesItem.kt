package com.example.bestofbehance.gson

import com.google.gson.annotations.SerializedName

data class ModulesItem(

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("components")
	val components: List<ComponentsItem?>? = null,

	@field:SerializedName("src")
	val src: String? = null
)