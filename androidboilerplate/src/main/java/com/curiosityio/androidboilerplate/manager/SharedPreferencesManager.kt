package com.curiosityio.androidboilerplate.manager

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

open class SharedPreferencesManager {

    companion object {
        @JvmStatic fun getString(context: Context, key: String, defaultValue: String? = null): String? {
            return getSharedPreferences(context).getString(key, defaultValue)
        }

        @JvmStatic fun getBoolean(context: Context, key: String, defaultValue: Boolean = false): Boolean {
            return getSharedPreferences(context).getBoolean(key, defaultValue)
        }

        @JvmStatic fun getInt(context: Context, key: String, defaultValue: Int = Int.MIN_VALUE): Int {
            return getSharedPreferences(context).getInt(key, defaultValue)
        }

        @JvmStatic fun getFloat(context: Context, key: String, defaultValue: Float = Float.MIN_VALUE): Float {
            return getSharedPreferences(context).getFloat(key, defaultValue)
        }

        @JvmStatic fun getLong(context: Context, key: String, defaultValue: Long = Long.MIN_VALUE): Long {
            return getSharedPreferences(context).getLong(key, defaultValue)
        }

        @JvmStatic fun edit(context: Context): Editor {
            return Editor(getSharedPreferences(context))
        }

        @JvmStatic fun getSharedPreferences(context: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }
    }

    class Editor(val sharedPreferences: SharedPreferences) {

        var editor: SharedPreferences.Editor = sharedPreferences.edit()

        fun setString(key: String, value: String?): Editor {
            editor.putString(key, value)
            return this
        }

        fun setBoolean(key: String, value: Boolean): Editor {
            editor.putBoolean(key, value)
            return this
        }

        fun setInt(key: String, value: Int): Editor {
            editor.putInt(key, value)
            return this
        }

        fun setFloat(key: String, value: Float): Editor {
            editor.putFloat(key, value)
            return this
        }

        fun setLong(key: String, value: Long): Editor {
            editor.putLong(key, value)
            return this
        }

        fun commitChangesNow(): Boolean {
            return editor.commit()
        }

        fun applyChangesSometime() {
            editor.apply()
        }
    }

}