package com.curiosityio.androidboilerplate.util

import android.content.Context
import android.content.pm.PackageManager

class PermissionUtil {

    companion object {
        fun isWriteExternalStoragePermissionGranted(context: Context): Boolean {
            return context.packageManager.checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, context.packageName) == PackageManager.PERMISSION_GRANTED
        }
    }

}
