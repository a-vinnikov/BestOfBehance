package com.example.bestofbehance.gson

import com.google.gson.annotations.SerializedName

data class ModulesItem(

	@field:SerializedName("sizes")
	val sizes: Sizes? = null,

	@field:SerializedName("src")
	val src: String? = null
)