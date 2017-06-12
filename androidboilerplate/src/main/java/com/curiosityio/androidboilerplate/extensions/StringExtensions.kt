package com.curiosityio.androidboilerplate.extensions

import java.io.File

fun String.deleteFileAtPath() {
    val file = File(this)

    file.delete()
}

fun String.getFileExtension(): String? { // returns ".png" for "yo.png"
    var extension: String? = null

    val i = lastIndexOf('.')
    if (i > 0) {
        extension = substring(i)
    }

    return extension
}

fun String.getFileExtensionWithoutDot(): String? { // returns "png" for "yo.png"
    return getFileExtension()?.substring(1)
}