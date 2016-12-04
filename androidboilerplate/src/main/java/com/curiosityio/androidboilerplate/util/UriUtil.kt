package com.curiosityio.androidboilerplate.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.*
import java.util.*
import android.provider.MediaStore
import java.text.SimpleDateFormat

open class UriUtil {

    companion object {
        fun getBitmapFromUri(uri: Uri): Bitmap {
            val image = File(uri.path)
            return BitmapFactory.decodeFile(image.absolutePath)
        }

        @Throws(IOException::class)
        fun createFileFromInputStream(inputStream: InputStream?): File {
            if (android.os.Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED) {
                var outputStream: OutputStream? = null
                try {
                    val filePath = File(Environment.getExternalStorageDirectory().absolutePath + "/images")
                    if (!filePath.exists()) {
                        filePath.mkdirs()
                    }

                    val file = File(filePath, UUID.randomUUID().toString() + ".png")
                    file.createNewFile()

                    outputStream = FileOutputStream(file)

                    val bytes = ByteArray(1024)
                    var read = inputStream!!.read(bytes)
                    while (read != -1) {
                        outputStream.write(bytes, 0, read)
                        read = inputStream.read(bytes)
                    }

                    return file
                } catch (e: IOException) {
                    throw e
                } finally {
                    try {
                        inputStream?.close()
                        // outputStream?.flush();
                        outputStream?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } else {
                throw IOException("Cannot save image. Your SD card is not available.")
            }
        }

        @Throws(IOException::class)
        fun createFileFromBitmap(image: Bitmap): File {
            if (android.os.Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED) {
                val filePath = File(Environment.getExternalStorageDirectory().absolutePath + "/images")
                if (!filePath.exists()) {
                    filePath.mkdirs()
                }

                val file = File(filePath, UUID.randomUUID().toString() + ".png")
                file.createNewFile()

                val fileOutputStream = FileOutputStream(file)

                image.compress(Bitmap.CompressFormat.PNG, 85, fileOutputStream)

                fileOutputStream.flush()
                fileOutputStream.close()

                return file
            } else {
                throw IOException("Cannot save image. Your SD card is not available.")
            }
        }

        @Throws(IOException::class)
        fun resizeImageAndSaveToFile(image: Uri): Uri {
            val resizedImageFile = resizeImageFileAndSaveToFile(getFileFromUri(image))

            return Uri.parse(resizedImageFile.toString())
        }

        @Throws(IOException::class)
        fun resizeImageFileAndSaveToFile(image: File): File {
            val fileUri = Uri.fromFile(image)
            val bitmap = getBitmapFromUri(fileUri)
            val resizedBitmap = resize(bitmap)

            return createFileFromBitmap(resizedBitmap)
        }

        fun resize(bitmap: Bitmap): Bitmap {
            val scaleSize = 1690

            val originalWidth = bitmap.width
            val originalHeight = bitmap.height
            var newWidth = -1
            var newHeight = -1
            var multFactor = -1.0f
            if (originalHeight > originalWidth) {
                newHeight = scaleSize
                multFactor = originalWidth.toFloat() / originalHeight.toFloat()
                newWidth = (newHeight * multFactor).toInt()
            } else if (originalWidth > originalHeight) {
                newWidth = scaleSize
                multFactor = originalHeight.toFloat() / originalWidth.toFloat()
                newHeight = (newWidth * multFactor).toInt()
            } else if (originalHeight == originalWidth) {
                newHeight = scaleSize
                newWidth = scaleSize
            }

            return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false)
        }

        fun getFileFromUri(uri: Uri): File {
            return File(uri.path)
        }

        // DONT FORGET to turn String into a Content Provider to work with Nougat.
        // val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", getFileFromUri(photoTakenUri))
        @Throws(IOException::class)
        fun getPathToTakePictureWith(): Uri {
            val imageFileName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir = Environment.getExternalStorageDirectory()
            val image = File.createTempFile(imageFileName, ".jpg", storageDir)

            return Uri.parse(image.toString())
        }


        // When choosing photos from gallery, the URI returned back to you may not be the correct one. This function finds you the full path to retrieve the image.
        fun getFullPathForUri(context: Context, uri: Uri): Uri {
            // try to retrieve the image from the media store first
            // this will only work for images selected from gallery
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(uri, projection, null, null, null)
            if (cursor != null) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()

                val result = Uri.parse(cursor.getString(columnIndex))

                cursor.close()

                return result
            }
            return uri
        }
    }

}