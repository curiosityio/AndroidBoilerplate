package com.curiosityio.androidboilerplate.extensions

import android.content.Context
import com.curiosityio.androidboilerplate.manager.SharedPreferencesManager

fun SharedPreferencesManager.Companion.getStringOrNull(context: Context, key: String): String? {
    return SharedPreferencesManager.getString(context, key, null)
}

fun SharedPreferencesManager.Companion.getIntOrNull(context: Context, key: String): Int? {
    val value = SharedPreferencesManager.getInt(context, key, Int.MIN_VALUE)

    if (value == Int.MIN_VALUE) return null else return value
}

fun SharedPreferencesManager.Companion.getFloatOrNull(context: Context, key: String): Float? {
    val value = SharedPreferencesManager.getFloat(context, key, Float.MIN_VALUE)

    if (value == Float.MIN_VALUE) return null else return value
}

fun SharedPreferencesManager.Companion.getLongOrNull(context: Context, key: String): Long? {
    val value = SharedPreferencesManager.getLong(context, key, Long.MIN_VALUE)

    if (value == Long.MIN_VALUE) return null else return value
}