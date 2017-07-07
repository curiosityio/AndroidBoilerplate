package com.curiosityio.androidboilerplate.util

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.*
import java.util.*
import android.provider.MediaStore
import android.database.Cursor
import android.os.Build
import android.provider.DocumentsContract

open class UriUtil {

    companion object {

        fun getBitmapFromUri(uri: Uri): Bitmap {
            val image = File(uri.path)
            return BitmapFactory.decodeFile(image.absolutePath)
        }

        @Throws(IOException::class)
        fun createDocumentFileFromInputStream(inputStream: InputStream?, fileExtension: String): File {
            return createFileFromInputStream(inputStream, fileExtension, "documents")
        }

        @Throws(IOException::class)
        fun createImageFileFromInputStream(inputStream: InputStream?): File {
            return createFileFromInputStream(inputStream, ".png", "images")
        }

        @Throws(IOException::class)
        private fun createFileFromInputStream(inputStream: InputStream?, fileExtension: String, directoryName: String): File {
            if (android.os.Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED) {
                var outputStream: OutputStream? = null
                try {
                    val filePath = File(Environment.getExternalStorageDirectory().absolutePath + "/" + directoryName)
                    if (!filePath.exists()) {
                        filePath.mkdirs()
                    }

                    val file = File(filePath, UUID.randomUUID().toString() + fileExtension)
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
                throw IOException("Cannot save file. Your SD card is not available.")
            }
        }

        @Throws(IOException::class)
        fun createImageFileFromBitmap(image: Bitmap): File {
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
        fun saveCapturedImageToFileInAppExternalStorage(context: Context, imageUri: Uri): Uri {
            val absoluteFilePathStringUri = fileAbsolutePathToUri(uriToString(imageUri)) // imageUri might be of form: `/path/to/image` or `file:///path/to/image` where we need to assert it is always `file:///path/to/image`

            val inputStreamOfImage: InputStream = context.contentResolver.openInputStream(absoluteFilePathStringUri)
            val imageFileUri = fileToUri(createImageFileFromInputStream(inputStreamOfImage))
            inputStreamOfImage.close()

            return imageFileUri
        }

        fun resize(bitmap: Bitmap): Bitmap {
            return resize(bitmap, bitmap.width, bitmap.height)
        }

        fun resize(bitmap: Bitmap, originalWidth: Int, originalHeight: Int): Bitmap {
            val scaleSize = 1690

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

        fun inputStreamFromUri(context: Context, uri: Uri): InputStream? {
            return context.contentResolver.openInputStream(uri)
        }

        // Take a Uri and get a File (with path, `file:///path/to/file`)
        fun uriToFile(uri: Uri): File {
            return File(uri.path)
        }

        fun uriToString(uri: Uri): String {
            return uri.path
        }

        fun stringPathToFile(path: String): File {
            return File(path)
        }

        fun fileAbsolutePathToUri(absolutePath: String): Uri {
            if (absolutePath.length >= 8 && absolutePath.startsWith("file:///")) {
                return Uri.parse(absolutePath)
            } else {
                if (absolutePath.substring(1) == "/") {
                    return Uri.parse("file://$absolutePath")
                } else {
                    return Uri.parse("file:///$absolutePath")
                }
            }
        }

        fun stringPathToUri(path: String): Uri {
            return Uri.parse(path)
        }

        fun remotePathToUri(path: String): Uri {
            return Uri.parse(path)
        }

        fun fileToUri(file: File): Uri {
            return Uri.fromFile(file)
        }

        fun fileToString(file: File): String {
            return file.toString()
        }

        // When you (1) Pick an image from the gallery (2) record a video via video intent (3) pick a video from the gallery BUT NOT when you take a photo via photo intent because you tell the intent where to save the photo. You will get back a content URI (`content://blah/blah/blah.mp4`) back in onActivityResult(). You want to convert this content URI into a file URI (`file:///full/path/to/blah/blah.mp4`) so that you can actually read the file, show it to the user, or upload it to a server.
        // This code was found in https://github.com/hoolrory/AndroidVideoSamples. This is the most complete example I have seen that does all file types. Thank you!
        fun contentUriToFilePath(context: Context, uri: Uri): String? {
            val EXTERNAL_STORAGE_DOCUMENTS_PROVIDER = "com.android.externalstorage.documents"
            val DOWNLOAD_DOCUMENTS_PROVIDER = "com.android.providers.downloads.documents"
            val MEDIA_DOCUMENTS_PROVIDER = "com.android.providers.media.documents"

            val PUBLIC_DOWNLOADS_CONTEXT_URI = "content://downloads/public_downloads"

            val DATA_COLUMN = "_data"

            val DOCUMENT_TYPE_AUDIO = "audio"
            val DOCUMENT_TYPE_IMAGE = "image"
            val DOCUMENT_TYPE_VIDEO = "video"
            val DOCUMENT_TYPE_PRIMARY = "primary"

            val URI_SCHEME_CONTENT = "content"
            val URI_SCHEME_FILE = "file"

            val authority = uri.authority
            val scheme = uri.scheme

            fun getColumn(context: Context, uri: Uri, column: String, selection: String?, selectionArgs: Array<String>?): String {
                var path: String? = null
                var cursor: Cursor? = null
                try {
                    cursor = context.contentResolver.query(uri, arrayOf(column), selection, selectionArgs, null)
                    if (cursor != null && cursor.moveToFirst()) {
                        val column_index = cursor.getColumnIndexOrThrow(column)
                        path = cursor.getString(column_index)
                    }
                } finally {
                    cursor?.let(Cursor::close)
                }

                return path!!
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
                val documentId = DocumentsContract.getDocumentId(uri)
                val documentIdPieces = documentId.split(":".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()
                val documentType = documentIdPieces[0]
                val documentPath = documentIdPieces[1]

                if (authority.equals(EXTERNAL_STORAGE_DOCUMENTS_PROVIDER, ignoreCase = true)) {
                    if (documentType.equals(DOCUMENT_TYPE_PRIMARY, ignoreCase = true)) {
                        return String.format(Locale.US, "%s/%s", Environment.getExternalStorageDirectory(), documentPath)
                    }
                } else if (authority.equals(DOWNLOAD_DOCUMENTS_PROVIDER, ignoreCase = true)) {
                    val contentUri = ContentUris.withAppendedId(Uri.parse(PUBLIC_DOWNLOADS_CONTEXT_URI), java.lang.Long.valueOf(documentId)!!)
                    return getColumn(context, contentUri, DATA_COLUMN, null, null)
                } else if (authority.equals(MEDIA_DOCUMENTS_PROVIDER, ignoreCase = true)) {
                    var contentUri: Uri? = null
                    if (documentType.equals(DOCUMENT_TYPE_AUDIO, ignoreCase = true)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    } else if (documentType.equals(DOCUMENT_TYPE_IMAGE, ignoreCase = true)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if (documentType.equals(DOCUMENT_TYPE_VIDEO, ignoreCase = true)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }
                    return getColumn(context, contentUri!!, DATA_COLUMN, "_id=?", arrayOf(documentPath))
                }
            } else if (scheme.equals(URI_SCHEME_CONTENT, ignoreCase = true)) {
                return getColumn(context, uri, DATA_COLUMN, null, null)
            } else if (scheme.equals(URI_SCHEME_FILE, ignoreCase = true)) {
                return uri.path
            }
            return null
        }
    }

}