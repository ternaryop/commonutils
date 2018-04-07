package com.ternaryop.lazyimageloader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

private const val BUFFER_SIZE = 1024

object Utils {
    fun copyStream(stream: InputStream, os: OutputStream) {
        try {
            val bytes = ByteArray(BUFFER_SIZE)
            while (true) {
                val count = stream.read(bytes, 0, BUFFER_SIZE)
                if (count == -1)
                    break
                os.write(bytes, 0, count)
            }
        } catch (ignored: Exception) {
        }
    }

    //decodes image and scales it to reduce memory consumption
    fun decodeFile(f: File): Bitmap? {
        try {
            //decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            val stream1 = FileInputStream(f)
            BitmapFactory.decodeStream(stream1, null, o)
            stream1.close()
            //Find the correct scale value. It should be the power of 2.
            val REQUIRED_SIZE = 70
            var widthTmp = o.outWidth
            var heightTmp = o.outHeight
            var scale = 1
            while (true) {
                if (widthTmp / 2 < REQUIRED_SIZE || heightTmp / 2 < REQUIRED_SIZE)
                    break
                widthTmp /= 2
                heightTmp /= 2
                scale *= 2
            }
            //decode with inSampleSize
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            val stream2 = FileInputStream(f)
            val bitmap = BitmapFactory.decodeStream(stream2, null, o2)
            stream2.close()
            return bitmap
        } catch (ignored: FileNotFoundException) {
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }
}