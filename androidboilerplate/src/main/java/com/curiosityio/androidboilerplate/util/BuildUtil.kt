package com.curiosityio.androidboilerplate.util

import android.R.attr.versionName
import android.content.pm.PackageManager
import android.R.attr.versionCode
import android.content.Context

open class BuildUtil {

    companion object {
        fun getVersionName(context: Context): String? {
            var versionName: String? = null

            try {
                versionName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
            }

            return versionName
        }

        fun getVersionCode(context: Context): Int? {
            var versionCode: Int? = null

            try {
                versionCode = context.packageManager.getPackageInfo(context.packageName, 0).versionCode
            } catch (e: PackageManager.NameNotFoundException) {
            }

            return versionCode
        }

        fun getApplicationName(context: Context): String {
            val stringId = context.applicationInfo.labelRes

            return context.getString(stringId)
        }
    }

}
