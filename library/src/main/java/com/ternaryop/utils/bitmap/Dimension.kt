package com.ternaryop.utils.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.DisplayMetrics

fun Bitmap.resize(newWidth: Float, newHeight: Float): Bitmap {
    val scaleWidth = newWidth / width
    val scaleHeight = newHeight / height
    // create a matrix for the manipulation
    val matrix = Matrix()
    // resize the bit map
    matrix.postScale(scaleWidth, scaleHeight)
    // recreate the new Bitmap
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
}

@Suppress("UnusedPrivateMember")
fun Bitmap.scaleForDefaultDisplay(context: Context): Bitmap {
    val metrics = DisplayMetrics()
    val scaleWidth = metrics.scaledDensity
    val scaleHeight = metrics.scaledDensity
    // create a matrix for the manipulation
    val matrix = Matrix()
    // resize the bit map
    matrix.postScale(scaleWidth, scaleHeight)
    // recreate the new Bitmap
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.scale(scaledWidth: Int, scaledHeight: Int, portrait: Boolean): Bitmap {
    if (portrait) {
        if (height > scaledHeight) {
            return resize(scaledWidth.toFloat(), scaledHeight.toFloat())
        }
    }
    return this
}
