package com.example.bestofbehance.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.io.Serializable

data class ImageBinding(
    var bigImageCard: String? = null
) : Serializable {
    @BindingAdapter("loadingBigImage")
    fun setBigImageUrl(view: ImageView, url: String) {
        Glide.with(view.context).load(url).into(view)
    }
}