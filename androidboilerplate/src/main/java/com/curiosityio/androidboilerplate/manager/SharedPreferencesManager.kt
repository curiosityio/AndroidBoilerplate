package com.curiosityio.androidboilerplate.manager

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

open class SharedPreferencesManager() {

    companion object {
        fun getString(context: Context, key: String, defaultValue: String? = null): String? {
            return getSharedPreferences(context).getString(key, defaultValue)
        }

        fun getBoolean(context: Context, key: String, defaultValue: Boolean = false): Boolean {
            return getSharedPreferences(context).getBoolean(key, defaultValue)
        }

        fun getInt(context: Context, key: String, defaultValue: Int = Int.MIN_VALUE): Int {
            return getSharedPreferences(context).getInt(key, defaultValue)
        }

        fun getFloat(context: Context, key: String, defaultValue: Float = Float.MIN_VALUE): Float {
            return getSharedPreferences(context).getFloat(key, defaultValue)
        }

        fun getLong(context: Context, key: String, defaultValue: Long = Long.MIN_VALUE): Long {
            return getSharedPreferences(context).getLong(key, defaultValue)
        }

        fun edit(context: Context): Editor {
            return Editor(getSharedPreferences(context))
        }

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }
    }

    class Editor(val sharedPreferences: SharedPreferences) {

        private var editor: SharedPreferences.Editor

        init {
            editor = sharedPreferences.edit()
        }

        fun setString(key: String, value: String?): Editor {
            if (value != null) editor.putString(key, value)
            return this
        }

        fun setBoolean(key: String, value: Boolean): Editor {
            editor.putBoolean(key, value)
            return this
        }

        fun setInt(key: String, value: Int?): Editor {
            if (value != null) editor.putInt(key, value)
            return this
        }

        fun commit(): Boolean {
            return editor.commit()
        }

        fun apply() {
            editor.apply()
        }
    }

}