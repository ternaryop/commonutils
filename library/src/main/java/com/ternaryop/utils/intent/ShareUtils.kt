package com.ternaryop.utils.intent

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore

data class ShareChooserParams(
    val destFileUri: Uri,
    val title: String,
    val subject: String,
    val mimeType: String = "image/jpeg")

object ShareUtils {
    fun showShareChooser(context: Context, shareChooserParams: ShareChooserParams) {
        val intent = Intent(Intent.ACTION_SEND)
            .setDataAndType(shareChooserParams.destFileUri, shareChooserParams.mimeType)
            .putExtra(Intent.EXTRA_SUBJECT, shareChooserParams.subject)
            .putExtra(Intent.EXTRA_STREAM, shareChooserParams.destFileUri)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        context.startActivity(Intent.createChooser(intent, shareChooserParams.title))
    }

    @Deprecated(
        message = "Do not properly work with Api level 29, use showShareChooser",
        replaceWith = ReplaceWith(
            expression = "showShareChooser",
            imports = ["com.ternaryop.utils.intent.ShareUtils.showShareChooser"]
        ),
        level = DeprecationLevel.WARNING)
    fun shareImage(context: Context, fullPath: String, mimeType: String, subject: String, chooserTitle: String) {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        values.put(MediaStore.Images.Media.DATA, fullPath)
        // sharing on google plus works only using MediaStore
        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = mimeType
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject)
        sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri)
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)

        context.startActivity(Intent.createChooser(sharingIntent, chooserTitle))
    }
}
