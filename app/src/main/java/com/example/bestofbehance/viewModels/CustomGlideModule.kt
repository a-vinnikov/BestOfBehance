package com.example.bestofbehance.viewModels

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule


@GlideModule
class CustomeGlideModule : AppGlideModule(){
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}