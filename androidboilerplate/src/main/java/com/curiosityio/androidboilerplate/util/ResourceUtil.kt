package com.curiosityio.androidboilerplate.util

import android.content.Context
import java.util.*

open class ResourceUtil {

    companion object {
        fun getArrayOfStringRes(context: Context, stringArrayRes: Int): ArrayList<String> {
            val myResArray = context.resources.getStringArray(stringArrayRes)
            return ArrayList(Arrays.asList(*myResArray))
        }
    }

}
