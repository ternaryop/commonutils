package com.ternaryop.lazyimageloader

import android.content.Context
import java.io.File

class FileCache constructor(context: Context, prefix: String? = null) {
    private val cacheDir = File(context.cacheDir, prefix ?: "").apply { parentFile?.apply { if (!exists()) mkdirs() } }
    private val prefix: String? = prefix?.let { File(it).name }

    fun getFile(url: String): File {
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        var filename = Integer.toHexString(url.hashCode())
        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);
        if (prefix != null) {
            filename = prefix + filename
        }
        return File(cacheDir, filename)
    }

    fun clear() {
        if (prefix == null) {
            cacheDir.listFiles()
        } else {
            cacheDir.listFiles { _, filename -> filename.startsWith(prefix) }
        }?.also { files ->
            for (f in files) {
                f.delete()
            }
        }
    }

    companion object {
        fun clearCache(context: Context, prefixDir: String) {
            val files = File(context.cacheDir, prefixDir).listFiles() ?: return
            for (f in files) {
                f.delete()
            }
        }
    }
}