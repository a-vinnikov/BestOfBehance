package com.example.bestofbehance.binding

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bestofbehance.extensions.CurrentDate

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
) {
    object ModelMapper {
        fun from(form: ProfileBinding, username: String?) =
            PeopleBinding(form.id!!, username, form.avatar, form.name, form.post, form.views, form.appreciations, form.followers, form.following, CurrentDate.getCurrentDateTime().toString())
    }
}