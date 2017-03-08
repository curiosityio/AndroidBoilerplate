package com.curiosityio.androidboilerplate.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.CalendarContract
import java.util.*
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.curiosityio.androidboilerplate.BuildConfig
import com.curiosityio.androidboilerplate.util.UriUtil.Companion.getPrivateTakePhotoFile
import com.curiosityio.androidboilerplate.util.UriUtil.Companion.getPublicGalleryTakePhotoFile


open class IntentUtil {

    companion object {
        fun getOpenWebpageIntent(url: String): Intent {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            return intent
        }

        fun startActivityIntent(context: Context, activityClass: Class<*>): Intent {
            return Intent(context, activityClass)
        }

        fun startServiceIntent(context: Context, serviceClass: Class<*>): Intent {
            return Intent(context, serviceClass)
        }

        fun getShareIntent(text: String): Intent {
            val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            return sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text)
        }

        // example on how to get result.
        //    @Override
        //    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //        super.onActivityResult(requestCode, resultCode, data);
        //
        //        switch(requestCode) {
        //            case CHOOSE_PIC_CODE:
        //                if (resultCode == Activity.RESULT_OK) {
        //                    Uri selectedImage = data.getData();
        //
        //                    InputStream imageStream = null;
        //                    try {
        //                        imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
        //
        //                        Bitmap yourImage = BitmapFactory.decodeStream(imageStream);
        //                    } catch (FileNotFoundException e) {
        //                        e.printStackTrace();
        //                    }
        //                }
        //                break;
        //        }
        //    }
        fun getPictureFromGalleryIntent(): Intent {
            val photoPickerIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            photoPickerIntent.type = "image/*"
            return photoPickerIntent
        }

        // Returns an Intent to use with startActivityForResult to get a small Bitmap from. Do not use if you want to capture the full size image.
        //
        // in onActivityResult, call intent.getExtras().get("data") to get the Bitmap.
        fun getTakeSmallBitmapPhotoIntent(context: Context): Intent? {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(context.packageManager) != null) return takePictureIntent else return null
        }

        open class FullSizePhotoIntent(val intent: Intent, val uri: Uri)
        // Note: You must provide a FileProvider in your manifest because we are using a FileProvider here.
        //
//        <application>
//          ...
//          <provider android:name="android.support.v4.content.FileProvider"
//            android:authorities="${applicationId}.fileprovider"
//            android:exported="false"
//            android:grantUriPermissions="true">
//          <meta-data android:name="android.support.FILE_PROVIDER_PATHS"
//            android:resource="@xml/provider_paths" />
//          ...
//        </application>
        // `applicationId` argument, give: BuildConfig.APPLICATION_ID
        // Create a new file: res/xml/file_paths.xml and put inside:
//        <?xml version="1.0" encoding="utf-8"?>
//        <paths>
//          <external-path name="external_files" path="." />
//        </paths>
        // Make sure to replace `com.example.package.name` with your actual package name of your app.
        @Throws(RuntimeException::class)
        fun getTakeFullSizePhotoSaveToPublicGalleryIntent(context: Context, applicationId: String): FullSizePhotoIntent? {
            val intent = getTakeSmallBitmapPhotoIntent(context) ?: return null
            if (!PermissionUtil.isWriteExternalStoragePermissionGranted(context)) throw RuntimeException("You do not have permission to take a photo.")

            val uri = FileProvider.getUriForFile(context, "$applicationId.fileprovider", getPublicGalleryTakePhotoFile())
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)

            return FullSizePhotoIntent(intent, uri)
        }

        // Note: Files saved in this directory are deleted when the app is uninstalled.
        // Note: We check if write external storage permission granted ONLY IF SDK is below 18. Declare this in your manifest if you target below 18.
        // <manifest ...>
        //   <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        //                                                    android:maxSdkVersion="18" />
        //   ...
        // </manifest>
        //
        // Note: You must provide a FileProvider in your manifest because we are using a FileProvider here.
        //
//        <application>
//          ...
//          <provider android:name="android.support.v4.content.FileProvider"
//            android:authorities="${applicationId}.fileprovider"
//            android:exported="false"
//            android:grantUriPermissions="true">
//          <meta-data android:name="android.support.FILE_PROVIDER_PATHS"
//            android:resource="@xml/provider_paths" />
//          ...
//        </application>
        // `applicationId` argument, give: BuildConfig.APPLICATION_ID
        // Create a new file: res/xml/file_paths.xml and put inside:
//        <?xml version="1.0" encoding="utf-8"?>
//        <paths>
//          <external-path name="external_files" path="." />
//        </paths>
        @Throws(RuntimeException::class)
        fun getTakeFullSizePhotoSaveToPrivateAppDataIntent(context: Context, applicationId: String): FullSizePhotoIntent? {
            val intent = getTakeSmallBitmapPhotoIntent(context) ?: return null
            if (Build.VERSION.SDK_INT < 18 && !PermissionUtil.isWriteExternalStoragePermissionGranted(context)) throw RuntimeException("You do not have permission to take a photo.")

            val uri = FileProvider.getUriForFile(context, "$applicationId.fileprovider", getPrivateTakePhotoFile())
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)

            return FullSizePhotoIntent(intent, uri)
        }

        //        override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        //            if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
        //                val videoUri = intent!!.data
        //                mVideoView.setVideoURI(videoUri)
        //            }
        //        }
        fun getTakeVideoIntent(context: Context): Intent? {
            val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            if (takeVideoIntent.resolveActivity(context.packageManager) != null) return takeVideoIntent else return null
        }

        // example on how to get result.
        //    @Override
        //    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //        super.onActivityResult(requestCode, resultCode, data);
        //
        //        switch(requestCode) {
        //            case CHOOSE_VIDEO_CODE:
        //                if (resultCode == Activity.RESULT_OK) {
        //                    Uri selectedVideo = data.getData();
        //                }
        //                break;
        //        }
        //    }
        fun getVideoFromGalleryIntent(): Intent {
            val videoPickerIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            videoPickerIntent.type = "video/*"
            return videoPickerIntent
        }

        fun getAddEventCalendarIntent(title: String, description: String? = null, location: String? = null, email: String? = null): Intent {
            val beginTime = Calendar.getInstance()

            beginTime.set(Calendar.HOUR_OF_DAY, 19)
            beginTime.set(Calendar.MINUTE, 0)
            beginTime.roll(Calendar.DAY_OF_MONTH, 1)

            val endTime = beginTime.clone() as Calendar

            endTime.roll(Calendar.HOUR_OF_DAY, 1)

            val intent = Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI).putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.timeInMillis)
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.timeInMillis)
                    .putExtra(CalendarContract.Events.TITLE, title)
                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)

            if (description != null) intent.putExtra(CalendarContract.Events.DESCRIPTION, description)
            if (location != null) intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location)
            if (email != null) intent.putExtra(Intent.EXTRA_EMAIL, email)

            return intent
        }
    }

}
