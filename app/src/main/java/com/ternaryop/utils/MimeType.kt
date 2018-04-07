package com.ternaryop.utils

import android.webkit.MimeTypeMap

/**
 * Created by dave on 27/04/14.
 * Helper class to work with Mime types
 */
object MimeType {
    private val map = mapOf(
        "flv" to "video/x-flv",
        "mp4" to "video/mp4",
        "m3u8" to "application/x-mpegURL",
        "ts" to "video/MP2T",
        "3gp" to "video/3gpp",
        "mov" to "video/quicktime",
        "avi" to "video/x-msvideo",
        "wmv" to "video/x-ms-wmv"
    )

    fun getTypeFromMimeType(mimeType: String): String {
        val comps = mimeType.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return if (comps.size == 2) {
            comps[0]
        } else mimeType
    }

    fun getMimeTypeFromExtension(extension: String): String {
        return map[extension] ?: return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }

    fun isVideoMimeType(mimeType: String): Boolean = mimeType.startsWith("video/")
    fun isImageMimeType(mimeType: String): Boolean = mimeType.startsWith("image/")
}
