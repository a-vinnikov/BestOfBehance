package com.example.bestofbehance.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.bestofbehance.viewModels.GlideApp
import com.example.bestofbehance.viewModels.GlideOptions.circleCropTransform
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

object BindingAdapters{
    @JvmStatic
    @BindingAdapter("loadingImage")
    fun setImageUrl(view: ImageView, url: String?) {
        GlideApp.with(view.context).load(url).into(view)
    }

    @JvmStatic
    @BindingAdapter(value = ["loadingRoundedImage", "loadingThumbnail"])
    fun setRoundedImageUrl(view: ImageView, url: String?, turl: String?) {
        val options = RequestOptions().transform(MultiTransformation(CenterCrop(), RoundedCornersTransformation(15, 0)))
        GlideApp.with(view.context).load(url).thumbnail(Glide.with(view.context).load(turl).apply(options)).apply(options).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(view)
    }

    @JvmStatic
    @BindingAdapter("loadingCircleImage")
    fun setCircleImageUrl(view: ImageView, url: String?) {
        GlideApp.with(view.context).load(url).apply(circleCropTransform()).into(view)
    }

    @JvmStatic
    @BindingAdapter(value = ["loadingBigCircleImage", "loadingThumbnail"])
    fun setBigCircleImageUrl(view: ImageView, url: String?, turl: String?) {
        GlideApp.with(view.context).load(url).thumbnail(Glide.with(view.context).load(turl).apply(circleCropTransform())).apply(circleCropTransform()).into(view)
    }

}