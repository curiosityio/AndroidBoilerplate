package com.curiosityio.androidboilerplate.util

import android.R.attr.versionName
import android.content.pm.PackageManager
import android.R.attr.versionCode
import android.content.Context

open class BuildUtil {

    companion object {

        @Throws(PackageManager.NameNotFoundException::class)
        fun getVersionName(context: Context): String {
            return context.packageManager.getPackageInfo(context.packageName, 0).versionName
        }

        @Throws(PackageManager.NameNotFoundException::class)
        fun getVersionCode(context: Context): Int {
            return context.packageManager.getPackageInfo(context.packageName, 0).versionCode
        }

        fun getApplicationName(context: Context): String {
            val stringId = context.applicationInfo.labelRes

            return context.getString(stringId)
        }
    }

}
