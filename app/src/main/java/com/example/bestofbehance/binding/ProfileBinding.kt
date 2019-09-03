package com.example.bestofbehance.binding

data class ProfileBinding(
    var id: Int = 0,
    var avatar: String? = null,
    var thumbnail: String? = null,
    var name: String? = "Name",
    var cityCountry: String? = null,
    var views: String? = "0",
    var appreciations: String? = "0",
    var followers: String? = "0",
    var following: String? = "0",
    var aboutMe: String? = null,
    var post: String? = "Post",
    var pinterest: String? = null,
    var instagram: String? = null,
    var facebook: String? = null,
    var behance: String? = null,
    var dribbble: String? = null,
    var twitter: String? = null
)