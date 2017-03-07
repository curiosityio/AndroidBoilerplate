package com.curiosityio.androidboilerplate.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import java.util.*
import android.provider.MediaStore



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

        //    @Override
        //    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //        super.onActivityResult(requestCode, resultCode, data);
        //
        //        switch(requestCode) {
        //            case TAKE_PIC_CODE:
        //                Bitmap photo = (Bitmap) data.getExtras().get("data");
        //
        //                break;
        //        }
        //    }
        fun getTakePictureIntent(): Intent {
            return Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        }

//        override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
//            if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
//                val videoUri = intent!!.data
//                mVideoView.setVideoURI(videoUri)
//            }
//        }
        fun getTakeVideoIntent(): Intent {
            return Intent(MediaStore.ACTION_VIDEO_CAPTURE)
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
