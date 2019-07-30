package com.example.bestofbehance.gson

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.io.Serializable

data class ImageBinding(
    var bigImageCard: String? = null,
    var description: String? = null
) : Serializable {
    @BindingAdapter("loadingImage1")
    fun setImageUrl1(view: ImageView, url: String) {
        Glide.with(view.context).load(url).into(view)
    }
}