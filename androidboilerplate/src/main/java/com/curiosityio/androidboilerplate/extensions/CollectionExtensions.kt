package com.curiosityio.androidboilerplate.extensions

import java.util.*

fun <E> Collection<E>.containsWhere(predicate: (E) -> Boolean): Boolean {
    return find(predicate) != null
}