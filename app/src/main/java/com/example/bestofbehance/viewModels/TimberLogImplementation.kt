package com.example.bestofbehance.viewModels

import android.util.Log
import com.example.bestofbehance.BuildConfig
import timber.log.Timber

class TimberLogImplementation {
    companion object {
        fun initLogging() {
            if(Timber.treeCount() != 0) return
            if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
            else Timber.plant(ReleaseTree())
        }
    }
}

class ReleaseTree: Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return}
    }
}