package com.ternaryop.utils.log

import java.io.File
import java.io.FileOutputStream
import java.io.PrintStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by dave on 23/06/16.
 * helper class to log errors to file
 */
object Log {
    private val DATE_TIME_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)

    fun error(destFile: File, vararg msg: String) {
        error(null, destFile, *msg)
    }

    @Suppress("PrintStackTrace")
    fun error(t: Throwable?, destFile: File, vararg msg: String) {
        val date = DATE_TIME_FORMAT.format(Date())
        // Android Q uses sandbox so we must ensure directories exist
        destFile.parentFile?.mkdirs()
        try {
            FileOutputStream(destFile, true).use { fos ->
                val ps = PrintStream(fos)
                for (m in msg) {
                    ps.println("$date - $m")
                }
                t?.printStackTrace(ps)
                ps.flush()
                ps.close()
            }
        } catch (fosEx: Exception) {
            fosEx.printStackTrace()
        }
    }
}
