package com.example.bestofbehance.binding

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import org.jetbrains.anko.matchParent

object BindingAdapters{
    @JvmStatic
    @BindingAdapter("loadingImage")
    fun setImageUrl(view: ImageView, url: String?) {
        Glide.with(view.context).load(url).into(view)
    }

    @JvmStatic
    @BindingAdapter(value = ["loadingRoundedImage", "loadingThumbnail"])
    fun setRoundedImageUrl(view: ImageView, url: String?, turl: String?) {
        val options = RequestOptions().transform(MultiTransformation(CenterCrop(), RoundedCornersTransformation(15, 0)))
        Glide.with(view.context).load(url).thumbnail(Glide.with(view.context).load(turl).diskCacheStrategy(DiskCacheStrategy.ALL).apply(RequestOptions.centerCropTransform())).apply(options).into(view)
    }

    @JvmStatic
    @BindingAdapter("loadingCircleImage")
    fun setCircleImageUrl(view: ImageView, url: String?) {
        Glide.with(view.context).load(url).apply(RequestOptions.circleCropTransform()).into(view)
    }

    @JvmStatic
    @BindingAdapter(value = ["loadingBigCircleImage", "loadingThumbnail"])
    fun setBigCircleImageUrl(view: ImageView, url: String?, turl: String?) {
        Glide.with(view.context).load(url).thumbnail(Glide.with(view.context).load(turl).apply(RequestOptions.circleCropTransform())).apply(RequestOptions.circleCropTransform()).into(view)
    }

}