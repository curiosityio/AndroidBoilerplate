package com.curiosityio.androidboilerplate.manager

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

open class SharedPreferencesManager() {

    companion object {
        fun getString(context: Context, key: String): String? {
            return getSharedPreferences(context).getString(key, null)
        }

        fun getBoolean(context: Context, key: String): Boolean {
            return getSharedPreferences(context).getBoolean(key, false)
        }

        fun getInt(context: Context, key: String): Int {
            return getSharedPreferences(context).getInt(key, Integer.MIN_VALUE)
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

        fun commit() {
            editor.commit()
        }
    }

}