package com.example.bestofbehance.classesToSupport

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule


@GlideModule
class CustomGlideModule : AppGlideModule(){
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}