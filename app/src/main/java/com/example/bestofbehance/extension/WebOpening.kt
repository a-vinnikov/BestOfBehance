package com.example.bestofbehance.extension

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.bestofbehance.R

object WebOpening {

    fun share(url: String, context: Context): Intent {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, url)
        intent.type = context.resources.getString(R.string.intent_type)
        return intent
    }

    fun immediately(url: String, context: Context){
            val uris = Uri.parse(url)
            val intents = Intent(Intent.ACTION_VIEW, uris)
            context.startActivity(intents)
    }

}