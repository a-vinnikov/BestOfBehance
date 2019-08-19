package com.example.bestofbehance.gson

import com.google.gson.annotations.SerializedName

data class Site(

	@field:SerializedName("app_icon")
	val appIcon: String? = null,

	@field:SerializedName("parent_id")
	val parentId: Int? = null,

	@field:SerializedName("domain")
	val domain: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("icon")
	val icon: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("key")
	val key: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)