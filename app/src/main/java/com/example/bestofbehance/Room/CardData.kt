package com.example.bestofbehance.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "CardData")
data class CardData(
    @PrimaryKey
    @SerializedName("id") var id: Int = 0,
    @SerializedName("bigImage") var bigImage: String? = null,
    @SerializedName("avatar") var avatar: String? = null,
    @SerializedName("name") var name: String? = "Name",
    @SerializedName("post") var post: String? = "Post",
    @SerializedName("views") var views: Int = 0,
    @SerializedName("appreciations") var appreciations: Int = 0,
    @SerializedName("comments") var comments: Int = 0


){
    var indexInResponse: Int = -1
    //constructor():this(0,"","","","",0, 0,0)
}