package com.curiosityio.androidboilerplate.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.ImageView

open class ViewUtil {

    companion object {
        fun dpToPx(dp: Int): Int {
            return (dp * Resources.getSystem().displayMetrics.density).toInt()
        }

        fun pxToDp(px: Int): Int {
            return (px / Resources.getSystem().displayMetrics.density).toInt()
        }

        fun closeKeyboard(context: Context, field: View) {
            try {
                val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

                inputMethodManager.hideSoftInputFromWindow(field.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
            } catch (ex: Exception) {
                LogUtil.d("Hide keyboard failed.")
            }
        }

        fun showKeyboard(context: Context, field: View) {
            try {
                field.requestFocus()

                val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

                inputMethodManager.showSoftInput(field, InputMethodManager.SHOW_IMPLICIT)
            } catch (ex: Exception) {
                LogUtil.d("Show keyboard failed.")
            }
        }

        // Create a bitmap from a view. Usually used for animations to make them go smoother by using a bitmap instead of animating the view itself.
        fun viewToImage(viewToBeConverted: WebView): Bitmap {
            val extraSpace = 2000 //because getContentHeight doesn't always return the full screen height.
            val height = viewToBeConverted.contentHeight + extraSpace

            var viewBitmap = Bitmap.createBitmap(viewToBeConverted.width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(viewBitmap)
            viewToBeConverted.draw(canvas)

            //If the view is scrolled, cut off the top part that is off the screen.
            try {
                val scrollY = viewToBeConverted.scrollY

                if (scrollY > 0) {
                    viewBitmap = Bitmap.createBitmap(viewBitmap, 0, scrollY, viewToBeConverted.width, height - scrollY)
                }
            } catch (ex: Exception) {
                LogUtil.d("Could not remove top part of the webview image. ex: " + ex)
            }

            return viewBitmap
        }
    }

}
