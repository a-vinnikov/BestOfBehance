package com.example.bestofbehance.classesToSupport

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.module.AppGlideModule


@GlideModule
class CustomGlideModule : AppGlideModule(){
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val diskCacheSizeBytes = 1024 * 1024 * 150
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, "BestOfBehance", diskCacheSizeBytes.toLong()))
    }
}