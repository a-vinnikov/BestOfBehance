package com.example.bestofbehance.gson

import com.google.gson.annotations.SerializedName

data class Sizes(

	@field:SerializedName("max_1920")
	val max1920: String? = null,

	@field:SerializedName("original")
	val original: String? = null,

	@field:SerializedName("1400")
	val jsonMember1400: String? = null,

	@field:SerializedName("max_3840")
	val max3840: String? = null,

	@field:SerializedName("max_1240")
	val max1240: String? = null,

	@field:SerializedName("1400_opt_1")
	val jsonMember1400Opt1: String? = null,

	@field:SerializedName("disp")
	val disp: String? = null,

	@field:SerializedName("max_1200")
	val max1200: String? = null,

	@field:SerializedName("source")
	val source: String? = null,

	@field:SerializedName("hd")
	val hd: String? = null,

	@field:SerializedName("fs")
	val fs: String? = null
)