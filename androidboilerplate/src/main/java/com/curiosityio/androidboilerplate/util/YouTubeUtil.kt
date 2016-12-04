package com.curiosityio.androidboilerplate.util

open class YouTubeUtil {

    companion object {
        fun getVideoIdFromUrl(videoUrl: String): String? {
            val split = videoUrl.split(".+(v=)".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            if (split.size == 2) {
                return split[1]
            }

            return null
        }

        fun getThumbnailUrlForVideoUrl(videoUrl: String): String? {
            val vidId = getVideoIdFromUrl(videoUrl) ?: return null

            return "https://img.youtube.com/vi/$vidId/hqdefault.jpg"
        }
    }

}
