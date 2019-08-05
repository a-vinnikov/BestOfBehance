package com.example.bestofbehance.gson

import androidx.databinding.BindingAdapter
import android.widget.ImageView
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide
import java.io.Serializable

import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.google.gson.annotations.SerializedName
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

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


) : Serializable {
    @BindingAdapter("loadingImage")
    fun setImageUrl(view: ImageView, url: String) {
            Glide.with(view.context).load(url).into(view)
    }
    @BindingAdapter("loadingRoundedImage")
    fun setRoundedImageUrl(view: ImageView, url: String) {
            Glide.with(view.context).load(url).apply(bitmapTransform(RoundedCornersTransformation(50, 0))).into(view)
    }

}

//.apply( RequestOptions().circleCrop())
