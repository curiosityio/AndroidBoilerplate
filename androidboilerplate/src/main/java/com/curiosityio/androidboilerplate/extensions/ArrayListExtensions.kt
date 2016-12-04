package com.curiosityio.androidboilerplate.extensions

import java.util.*

fun <E : Comparable<E>> ArrayList<E>.sortAlphabeticalOrder() {
    Collections.sort(this, { obj, t1 -> obj.compareTo(t1) })
}
