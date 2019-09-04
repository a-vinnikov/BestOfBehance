package com.example.bestofbehance.gson

import com.google.gson.annotations.SerializedName

data class SocialLinksItem(

	@field:SerializedName("social_id")
	val socialId: Int? = null,

	@field:SerializedName("service_name")
	val serviceName: String? = null,

	@field:SerializedName("social_network_type")
	val socialNetworkType: String? = null,

	@field:SerializedName("value")
	val value: String? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("isInstagram")
	val isInstagram: Boolean? = null,

	@field:SerializedName("isTumblr")
	val isTumblr: Boolean? = null,

	@field:SerializedName("isTwitter")
	val isTwitter: Boolean? = null,

	@field:SerializedName("isFacebook")
	val isFacebook: Boolean? = null,

	@field:SerializedName("isFlickr")
	val isFlickr: Boolean? = null,

	@field:SerializedName("isPinterest")
	val isPinterest: Boolean? = null,

	@field:SerializedName("isBehance")
	val isBehance: Boolean? = null,

	@field:SerializedName("isDribbble")
	val isDribbble: Boolean? = null
)