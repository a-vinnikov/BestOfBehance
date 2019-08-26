package com.example.bestofbehance.binding

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "ProjectsData")
data class ProjectsBinding(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "bigImage") var bigImage: String? = null,
    @ColumnInfo(name = "avatar") var avatar: String? = null,
    @ColumnInfo(name = "artistName") var artistName: String? = "Name",
    @ColumnInfo(name = "artName") var artName: String? = "Art",
    @ColumnInfo(name = "views") var views: String? = "0",
    @ColumnInfo(name = "appreciations") var appreciations: String? = "0",
    @ColumnInfo(name = "comments") var comments: String? = "0",
    @ColumnInfo(name = "username") var username: String?,
    @ColumnInfo(name = "published") var published: Int?
) : Serializable

//.apply( RequestOptions().circleCrop())
