package com.example.bestofbehance.binding

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PeopleData")
data class PeopleBinding(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "username") var username: String? = null,
    @ColumnInfo(name = "avatar") var avatar: String? = null,
    @ColumnInfo(name = "name") var name: String? = "Name",
    @ColumnInfo(name = "post") var post: String? = "Post",
    @ColumnInfo(name = "views") var views: String? = "0",
    @ColumnInfo(name = "appreciations") var appreciations: String? = "0",
    @ColumnInfo(name = "followers") var followers: String? = "0",
    @ColumnInfo(name = "following") var following: String? = "0",
    @ColumnInfo(name = "added") var added: String?
)