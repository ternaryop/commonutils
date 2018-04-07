package com.ternaryop.lazyimageloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.MenuItem
import android.widget.ImageView
import com.ternaryop.utils.bitmap.scaleForDefaultDisplay
import com.ternaryop.utils.network.openConnectionFollowingDifferentProtocols
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.Collections
import java.util.WeakHashMap
import java.util.concurrent.Executors

class ImageLoader(private val context: Context, prefix: String, private val stubResId: Int) {
    private val memoryCache = MemoryCache()
    private val fileCache = FileCache(context, IMAGE_PREFIX_DIRECTORY + prefix)
    private val imageViews = Collections.synchronizedMap(WeakHashMap<ImageView, String>())
    private val executorService = Executors.newFixedThreadPool(5)
    private val handler = Handler() // handler to display images in UI thread

    fun displayImage(url: String, imageView: ImageView, scaleForDefaultDisplay: Boolean = false) {
        imageViews[imageView] = url
        var bitmap = memoryCache[url]
        if (bitmap != null) {
            if (scaleForDefaultDisplay) {
                bitmap = bitmap.scaleForDefaultDisplay(context)
            }
            imageView.setImageBitmap(bitmap)
        } else {
            queuePhoto(url, imageView, scaleForDefaultDisplay)
            imageView.setImageResource(stubResId)
        }
    }

    fun displayDrawable(url: String, scaleForDefaultDisplay: Boolean, callback: ImageLoaderCallback) {
        executorService.submit {
            drawableFromUrl(url, scaleForDefaultDisplay)?.let { icon -> handler.post { callback.display(icon) } }
        }
    }

    fun displayIcon(url: String, menuItem: MenuItem) {
        displayDrawable(url, true, object : ImageLoaderCallback {
            override fun display(drawable: Drawable) {
                menuItem.icon = drawable
            }
        })
    }

    private fun queuePhoto(url: String, imageView: ImageView, scaleForDefaultDisplay: Boolean) {
        val p = PhotoToLoad(url, imageView)
        executorService.submit(PhotosLoader(p, scaleForDefaultDisplay))
    }

    private fun getBitmap(url: String): Bitmap? {
        val f = fileCache.getFile(url)
        //from SD cache
        val b = decodeFile(f)
        if (b != null) {
            return b
        }
        //from web
        try {
            val conn = URL(url).openConnectionFollowingDifferentProtocols()
            conn.connectTimeout = 30000
            conn.readTimeout = 30000
            conn.connect()
            val stream = conn.inputStream
            val os = FileOutputStream(f)
            Utils.copyStream(stream, os)
            os.close()
            conn.disconnect()
            return decodeFile(f)
        } catch (ex: Throwable) {
            ex.printStackTrace()
            if (ex is OutOfMemoryError)
                memoryCache.clear()
            return null
        }
    }

    //decodes image and scales it to reduce memory consumption
    private fun decodeFile(f: File): Bitmap? {
        try {
            //decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            val stream1 = FileInputStream(f)
            BitmapFactory.decodeStream(stream1, null, o)
            stream1.close()
            //Find the correct scale value. It should be the power of 2.
            val requiredSize = 70
            var tmpWidth = o.outWidth
            var tmpHeight = o.outHeight
            var scale = 1
            while (true) {
                if (tmpWidth / 2 < requiredSize || tmpHeight / 2 < requiredSize)
                    break
                tmpWidth /= 2
                tmpHeight /= 2
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

    //Task for the queue
    private inner class PhotoToLoad(val url: String, val imageView: ImageView)

    private inner class PhotosLoader(val photoToLoad: PhotoToLoad, private val scaleForDefaultDisplay: Boolean) : Runnable {
        override fun run() {
            try {
                if (imageViewReused(photoToLoad)) {
                    return
                }
                val bmp = getBitmap(photoToLoad.url)
                memoryCache.put(photoToLoad.url, bmp!!)
                if (imageViewReused(photoToLoad)) {
                    return
                }
                val bd = BitmapDisplayer(bmp, photoToLoad, scaleForDefaultDisplay)
                handler.post(bd)
            } catch (th: Throwable) {
                th.printStackTrace()
            }
        }
    }

    private fun imageViewReused(photoToLoad: PhotoToLoad): Boolean {
        val tag = imageViews[photoToLoad.imageView]
        return tag == null || tag != photoToLoad.url
    }

    //Used to display bitmap in the UI thread
    private inner class BitmapDisplayer(val bitmap: Bitmap?, val photoToLoad: PhotoToLoad, private val scaleForDefaultDisplay: Boolean) : Runnable {
        override fun run() {
            if (imageViewReused(photoToLoad)) {
                return
            }
            if (bitmap != null) {
                photoToLoad.imageView.setImageBitmap(if (scaleForDefaultDisplay)
                    bitmap.scaleForDefaultDisplay(context) else bitmap)
            } else {
                photoToLoad.imageView.setImageResource(stubResId)
            }
        }
    }

    fun clearCache() {
        memoryCache.clear()
        fileCache.clear()
    }

    private fun drawableFromUrl(url: String, scaleForDefaultDisplay: Boolean): Drawable? {
        var conn: HttpURLConnection? = null
        var os: OutputStream? = null
        try {
            val f = fileCache.getFile(url)
            var bitmap = Utils.decodeFile(f)

            if (bitmap == null) {
                conn = URL(url).openConnectionFollowingDifferentProtocols()
                conn.connectTimeout = 30000
                conn.readTimeout = 30000
                conn.connect()
                os = FileOutputStream(f)
                Utils.copyStream(conn.inputStream, os)

                bitmap = Utils.decodeFile(f)
            }
            if (scaleForDefaultDisplay) {
                bitmap = bitmap?.scaleForDefaultDisplay(context)
            }
            return BitmapDrawable(null, bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (os != null) os.close()
            } catch (ignored: Exception) {
            }

            try {
                if (conn != null) conn.disconnect()
            } catch (ignored: Exception) {
            }
        }
        return null
    }

    interface ImageLoaderCallback {
        fun display(drawable: Drawable)
    }

    companion object {
        val IMAGE_PREFIX_DIRECTORY = "images" + File.separator

        fun clearImageCache(context: Context) {
            FileCache.clearCache(context, IMAGE_PREFIX_DIRECTORY)
        }
    }
}
