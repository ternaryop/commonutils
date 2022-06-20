package com.ternaryop.utils.network

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Pair
import java.io.UnsupportedEncodingException
import java.net.URI
import java.net.URISyntaxException
import java.net.URLEncoder

/**
 * Created by dave on 25/04/14.
 * Helper class to work with Uri
 */

const val RESOLVE_URL_RETRY_COUNT = 20

/**
 * Method for return file path of Gallery image
 *
 * @param context
 * @param downloadId the id appended when the Uri authority is DownloadsProvider,
 * if null DocumentsContract.getDocumentId() is used as default value
 * @return path of the selected image file from gallery
 */
fun Uri.getPath(context: Context, downloadId: Long? = null): String? {
    // check here to KITKAT or new version
    val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, this)) {
        // ExternalStorageProvider
        if (isExternalStorageDocument) {
            val docId = DocumentsContract.getDocumentId(this)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }
        } else if (isDownloadsDocument) {
            // DownloadsProvider
            val id = downloadId ?: DocumentsContract.getDocumentId(this).toLong()
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),
                id
            )

            return contentUri.getDataColumn(context, null, null)
        } else if (isMediaDocument) {
            // MediaProvider
            val docId = DocumentsContract.getDocumentId(this)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]
            val contentUri: Uri? = when (type) {
                "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                else -> null
            }
            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])

            return contentUri.getDataColumn(context, selection, selectionArgs)
        }
    } else if ("content".equals(scheme, ignoreCase = true)) {
        // MediaStore (and general)
        // Return the remote address
        return if (isGooglePhotosUri) lastPathSegment else getDataColumn(context, null, null)
    } else if ("file".equals(scheme, ignoreCase = true)) {
        // File
        return path
    }
    return null
}

/**
 * Get the value of the data column for this Uri. This is useful for
 * MediaStore Uris, and other file-based ContentProviders.
 *
 * @param context The context.
 * @param selection (Optional) Filter used in the query.
 * @param selectionArgs (Optional) Selection arguments used in the query.
 * @return The value of the _data column, which is typically a file path.
 */
fun Uri?.getDataColumn(context: Context, selection: String?, selectionArgs: Array<String>?): String? {
    if (this == null) return null

    val projection = arrayOf(MediaStore.Images.Media.DATA)

    return context.contentResolver.query(this, projection, selection, selectionArgs, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
        } else {
            null
        }
    }
}

/**
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
inline val Uri.isExternalStorageDocument: Boolean
    get() = "com.android.externalstorage.documents" == authority

/**
 * @return Whether the Uri authority is DownloadsProvider.
 */
inline val Uri.isDownloadsDocument: Boolean
    get() = "com.android.providers.downloads.documents" == authority

/**
 * @return Whether the Uri authority is MediaProvider.
 */
inline val Uri.isMediaDocument: Boolean
    get() = "com.android.providers.media.documents" == authority

/**
 * @return Whether the Uri authority is Google Photos.
 */
inline val Uri.isGooglePhotosUri: Boolean
    get() = "com.google.android.apps.photos.content" == authority

fun Uri.getRealPathAndMimeFromUri(context: Context): Pair<String, String?>? {
    return context.contentResolver.query(this, null, null, null, null)?.use { cursor ->
        val bucketIdIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)

        cursor.moveToFirst()

        val bucketId = if (bucketIdIndex == -1) -1 else cursor.getString(bucketIdIndex).toLong()

        val mimeTypeIndex = cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)
        val mimeType = if (mimeTypeIndex == -1) null else cursor.getString(mimeTypeIndex)
        Pair.create(getPath(context, bucketId), mimeType)
    }
}

object UriUtils {
    /**
     * URI doesn't encode invalid characters, so we do it
     * For example the url http://my.com/hello\u00a0-world.jpg is fixed into
     * http://my.com/hello%C2%A0-world.jpg
     * @param uriString the url to convert to URI
     * @param enc the encoding to use
     * @param retryCount how many times must retry before throw exception
     * @return the fixed url
     * @throws URISyntaxException unable to encode the illegal characters
     * @throws UnsupportedEncodingException unable to encode the url
     */
    fun encodeIllegalChar(uriString: String, enc: String, retryCount: Int = RESOLVE_URL_RETRY_COUNT): URI {
        var count = retryCount
        var uriStr = uriString

        while (true) {
            try {
                return URI(uriStr)
            } catch (e: URISyntaxException) {
                val reason = e.reason
                if (reason == null || !(reason.contains("in path") ||
                        reason.contains("in query") ||
                        reason.contains("in fragment"))) {
                    throw e
                }
                if (0 > count--) {
                    throw e
                }
                val input = e.input
                val idx = e.index
                val illChar = input[idx].toString()
                uriStr = input.replace(illChar, URLEncoder.encode(illChar, enc))
            }
        }
    }

    fun resolveRelativeURL(baseURL: String?, link: String): String {
        val uri = encodeIllegalChar(link, "UTF-8")
        return when {
            uri.isAbsolute -> uri.toString()
            baseURL != null -> encodeIllegalChar(baseURL, "UTF-8").resolve(uri).toString()
            else -> throw IllegalArgumentException("baseUrl is null")
        }
    }
}
