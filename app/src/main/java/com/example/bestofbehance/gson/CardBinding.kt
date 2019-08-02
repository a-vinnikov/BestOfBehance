package com.example.bestofbehance.gson

import androidx.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.Serializable

import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.google.gson.annotations.SerializedName
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

data class CardBinding(
    @SerializedName("original") var bigImage: String? = null,
    @SerializedName("138") var avatar: String? = null,
    @SerializedName("display_name") var name: String? = "Name",
    @SerializedName("fields") var post: String? = "Post",
    @SerializedName("views") var views: String? = "0",
    @SerializedName("appreciations") var appreciations: String? = "0",
    @SerializedName("comments") var comments: String? = "0",
    @SerializedName("id") var id: String? = null

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
