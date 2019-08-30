package com.example.bestofbehance.gson

import com.google.gson.annotations.SerializedName

data class User(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("about_me")
	val occupation: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("social_links")
	val socialLinks: List<SocialLinksItem?>? = null,

	@field:SerializedName("features")
	val features: List<FeaturesItem?>? = null,

	@field:SerializedName("twitter")
	val twitter: String? = null,

	@field:SerializedName("stats")
	val stats: Stats? = null,

	@field:SerializedName("banner_image_url")
	val bannerImageUrl: String? = null,

	@field:SerializedName("company")
	val company: String? = null,

	@field:SerializedName("links")
	val links: List<Any?>? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("images")
	val images: Images? = null,

	@field:SerializedName("website")
	val website: String? = null,

	@field:SerializedName("has_social_links")
	val hasSocialLinks: Boolean? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("display_name")
	val displayName: String? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("sections")
	val sections: Sections? = null,

	@field:SerializedName("created_on")
	val createdOn: Int? = null,

	@field:SerializedName("has_default_image")
	val hasDefaultImage: Int? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("fields")
	val fields: List<String?>? = null,

	@field:SerializedName("username")
	val username: String? = null
)