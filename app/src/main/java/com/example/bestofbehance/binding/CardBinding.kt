package com.example.bestofbehance.binding

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "CardData")
data class CardBinding(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "bigImage") var bigImage: String? = null,
    @ColumnInfo(name = "avatar") var avatar: String? = null,
    @ColumnInfo(name = "name") var name: String? = "Name",
    @ColumnInfo(name = "post") var post: String? = "Post",
    @ColumnInfo(name = "views") var views: String? = "0",
    @ColumnInfo(name = "appreciations") var appreciations: String? = "0",
    @ColumnInfo(name = "comments") var comments: String? = "0"


) : Serializable

//.apply( RequestOptions().circleCrop())
