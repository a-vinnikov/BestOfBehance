package com.example.bestofbehance.extensions

import android.content.Context
import android.content.Intent
import com.example.bestofbehance.R

object WebOpening {

    fun open(url: String, context: Context): Intent {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, url)
        intent.type = context.resources.getString(R.string.intent_type)
        return intent
    }
}