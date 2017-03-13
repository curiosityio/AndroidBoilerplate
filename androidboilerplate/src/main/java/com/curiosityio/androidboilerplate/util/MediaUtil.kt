package com.curiosityio.androidboilerplate.util

import android.os.Environment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.provider.MediaStore
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.os.Build



class MediaUtil {

    companion object {
        // I recommend using IntentUtil to get Intent as you must use FileProvider to get the Intent after you obtain file here.
        @Throws(IOException::class)
        fun getPrivateTakePhotoFile(): File {
            val imageFileName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir = Environment.getExternalStorageDirectory()
            val imageFile: File = File(storageDir, imageFileName + ".jpg")

            if (imageFile.createNewFile()) {
                return imageFile
            } else {
                // this should never happen...
                throw RuntimeException("Error saving photo.")
            }
        }

        // I recommend using IntentUtil to get Intent as you must use FileProvider to get the Intent after you obtain file here.
        @Throws(IOException::class)
        fun getPublicGalleryTakePhotoFile(): File {
            val imageFileName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val imageFile: File = File(storageDir, imageFileName + ".jpg")

            if (imageFile.createNewFile()) {
                return imageFile
            } else {
                // this should never happen...
                throw RuntimeException("Error saving photo.")
            }
        }
    }

}