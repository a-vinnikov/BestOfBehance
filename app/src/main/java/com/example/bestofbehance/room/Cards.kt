package com.example.bestofbehance.room

import com.orm.SugarRecord

class Cards(
    val cardsId: Int,
    val cardsBigImage: String?,
    val cardsAvatar: String?,
    val cardsName: String?,
    val cardsPost: String?,
    val cardsViews: String?,
    val cardsAppreciations: String?,
    val cardsComments: String?
) : SugarRecord() {
    /*var id: Int? = null
    var bigImage: String? = null
    var avatar: String? = null
    var name: String? = null
    var post: String? = null
    var views: String? = null
    var appreciations: String? = null
    var comments: String? = null*/


    fun Cards(){}

/*    fun Cards(id: Int?, bigImage: String?, avatar: String?, name: String?, post: String?, views: String?, appreciations: String?, comments: String?){
        this.id = id
        this.bigImage = bigImage
        this.avatar = avatar
        this.name = name
        this.post = post
        this.views = views
        this.appreciations = appreciations
        this.comments = comments
    }*/
}