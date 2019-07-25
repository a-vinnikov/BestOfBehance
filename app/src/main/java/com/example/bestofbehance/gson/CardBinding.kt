package com.example.bestofbehance.gson

import androidx.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.Serializable

import com.bumptech.glide.request.RequestOptions.bitmapTransform
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

data class CardBinding(
    var bigImage: String? = null,
    var avatar: String? = null,
    var name: String? = "Name",
    var post: String? = "Post",
    var views: String? = "0",
    var appreciations: String? = "0",
    var comments: String? = "0",
    var id: String? = null

) : Serializable {
    @BindingAdapter("loadingImage")
    fun setImageUrl(view: ImageView, url: String, rounded: String) {
        if (rounded == "rounded") {
            Glide.with(view.context).load(url).apply(bitmapTransform(RoundedCornersTransformation(15, 0))).into(view)
        } else if (rounded == "not rounded") {
            Glide.with(view.context).load(url).into(view)
        }

    }
}

//.apply( RequestOptions().circleCrop())
