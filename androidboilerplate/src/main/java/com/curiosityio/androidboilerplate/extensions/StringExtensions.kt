package com.curiosityio.androidboilerplate.extensions

import java.io.File

fun String.deleteFileAtPath() {
    val file = File(this)

    file.delete()
}