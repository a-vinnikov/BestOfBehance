package com.example.bestofbehance.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.io.Serializable

data class CommentsBinding(
    var commentAvatarView: String? = null,
    var commentName: String? = null,
    var comment: String? = null,
    var date: String? = null
) : Serializable {
    @BindingAdapter("loadingImageComment")
    fun setImageUrlComment(view: ImageView, url: String) {
        Glide.with(view.context).load(url).into(view)
    }
}

//.apply( RequestOptions().circleCrop())
