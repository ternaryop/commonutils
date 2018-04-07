package com.ternaryop.utils.intent

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.provider.MediaStore

object ShareUtils {
    fun shareImage(context: Context, fullPath: String, mimeType: String, subject: String, chooserTitle: String) {
        val values = ContentValues(4)
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
