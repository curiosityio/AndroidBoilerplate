package com.curiosityio.androidboilerplate.util

import android.content.Context
import android.provider.Settings.System.ACCELEROMETER_ROTATION
import android.provider.Settings;

class DeviceUtil {

    companion object {
        fun isRotationEnabled(context: Context): Boolean {
            return android.provider.Settings.System.getInt(context.contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0) == 1
        }
    }

}