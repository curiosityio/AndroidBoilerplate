package com.curiosityio.androidboilerplate.util

import android.util.Log

open class LogUtil {

    companion object {
        val LOG_TAG = "SUPER_AWESOME_APP"

        fun d(message: String) {
            Log.d(LOG_TAG, message)
        }
    }

}
