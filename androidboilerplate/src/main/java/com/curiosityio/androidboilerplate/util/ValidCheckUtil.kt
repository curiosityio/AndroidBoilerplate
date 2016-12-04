package com.curiosityio.androidboilerplate.util

import android.telephony.PhoneNumberUtils
import android.util.Patterns

open class ValidCheckUtil {

    companion object {
        fun isValidEmail(email: String?): Boolean {
            if (email == null) return false else return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isValidPhoneNumber(number: String?): Boolean {
            if (number == null) return false else return PhoneNumberUtils.isGlobalPhoneNumber(number)
        }

        fun isValidURL(url: String?): Boolean {
            if (url == null) return false else return Patterns.WEB_URL.matcher(url).matches()
        }

        fun isValidFirstAndLastName(fullname: String): Boolean {
            // regex accepts any [1 or more none whitespace character(s)], [one space], [1 or more none whitespace character(s)]
            // (allows hyphens in names for example why allowing any non-whitespace char.)
            val splitFullName = fullname.trim { it <= ' ' }.split("\\S+[ ]\\S+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            return splitFullName.size == 2
        }

        fun getSplitFullName(fullname: String): Array<String>? {
            if (isValidFirstAndLastName(fullname)) {
                return fullname.trim { it <= ' ' }.split("\\S+[ ]\\S+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            }

            return null
        }
    }

}
