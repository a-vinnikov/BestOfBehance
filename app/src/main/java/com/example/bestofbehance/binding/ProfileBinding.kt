package com.example.bestofbehance.binding

data class ProfileBinding(
    var id: Int? = 0,
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
    var url: String? = null
)