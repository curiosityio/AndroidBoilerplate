package com.curiosityio.androidboilerplate.util

import android.content.Context
import android.net.ConnectivityManager
import android.provider.Settings

open class InternetConnectionUtil() {

    companion object {
        fun isAnyInternetConnected(context: Context): Boolean {
            val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo

            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

        fun isRotationEnabled(context: Context): Boolean {
            return Settings.System.getInt(context.contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0) == 1
        }

        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo

            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

        fun isConnectedWifi(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = connectivityManager.activeNetworkInfo

            return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_WIFI
        }

        fun isConnectedMobile(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = connectivityManager.activeNetworkInfo

            return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_MOBILE
        }
    }

}
