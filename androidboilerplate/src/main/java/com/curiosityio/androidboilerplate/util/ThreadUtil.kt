package com.curiosityio.androidboilerplate.util

import android.os.Looper

open class ThreadUtil() {

    companion object {
        fun isOnMainThread(): Boolean {
            return Looper.getMainLooper().thread == Thread.currentThread()
        }
    }

}
