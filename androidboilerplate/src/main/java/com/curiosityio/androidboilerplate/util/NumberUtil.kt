package com.curiosityio.androidboilerplate.util

import android.content.Context
import android.util.DisplayMetrics
import java.text.DecimalFormat


open class NumberUtil {

    companion object {
        fun dpToPx(context: Context, dp: Int): Int {
            val displayMetrics = context.resources.displayMetrics
            return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
        }

        fun getFormattedCount(count: Int): String {
            return getFormattedCount(java.lang.Long.valueOf(count.toLong()))
        }

        fun getFormattedCount(count: String): String {
            return getFormattedCount(java.lang.Long.parseLong(count))
        }

        // Example: Input: 1200000, output: 1.2m
        fun getFormattedCount(count: Long): String {
            val unit: String
            val dbl: Double?
            val format = DecimalFormat("#.#")
            if (count < 1000) {
                return format.format(count)
            } else if (count < 1000000) {
                unit = "k"
                dbl = count / 1000.0
            } else if (count < 1000000000) {
                unit = "m"
                dbl = count / 1000000.0
            } else {
                unit = "b"
                dbl = count / 1000000000.0
            }
            return format.format(dbl) + unit
        }

        fun toAlphabetic(i: Int): String {
            if (i < 0) {
                return "-" + toAlphabetic(-i - 1)
            }

            val quot = i / 26
            val rem = i % 26
            val letter = ('A'.toInt() + rem).toChar()
            if (quot == 0) {
                return "" + letter
            } else {
                return toAlphabetic(quot - 1) + letter
            }
        }
    }

}
