package com.ternaryop.utils.bitmap

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

@Throws(IOException::class)
fun File.readBitmap(): Bitmap {
    val stream = BufferedInputStream(FileInputStream(this))
    val bitmap = BitmapFactory.decodeStream(stream)
    stream.close()

    return bitmap
}

@Throws(IOException::class)
fun URL.readBitmap(): Bitmap {
    val connection = openConnection() as HttpURLConnection
    val stream = connection.inputStream
    val bitmap = BitmapFactory.decodeStream(stream)
    stream.close()
    connection.disconnect()

    return bitmap
}

fun File.savePng(image: Bitmap) {
    FileOutputStream(this).use { fos ->
        image.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.flush()
    }
}
