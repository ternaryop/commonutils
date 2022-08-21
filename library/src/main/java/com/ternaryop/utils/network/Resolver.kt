package com.ternaryop.utils.network

import java.io.IOException
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection

private const val BUFFER_SIZE = 100 * 1024

/* Resolve a possible shorten url to the original one */
fun URL.resolveShorten(): URL {
    val conn: URLConnection
    return try {
        conn = openConnection()
        conn.headerFields
        conn.url
    } catch (ignored: Exception) {
        this
    }
}

/**
 * Handle the case the url is redirected to a location with different protocol (eg from http to https or viceversa)
 * @return the open connection
 * @throws java.io.IOException
 */
@Throws(IOException::class)
fun URL.openConnectionFollowingDifferentProtocols(): HttpURLConnection {
    var url = this
    var conn: HttpURLConnection
    var location: String
    var continueFollow = true


    do {
        conn = url.openConnection() as HttpURLConnection
        conn.instanceFollowRedirects = false

        when (conn.responseCode) {
            HttpURLConnection.HTTP_MOVED_PERM, HttpURLConnection.HTTP_MOVED_TEMP -> {
                location = conn.getHeaderField("Location")
                // Deal with relative URLs
                url = URL(url, location)
            }
            else -> continueFollow = false
        }
    } while (continueFollow)
    return conn
}

@Throws(IOException::class)
fun URL.saveURL(os: OutputStream) {
    var connection: HttpURLConnection? = null

    try {
        connection = openConnection() as HttpURLConnection
        connection.inputStream?.use { it.copyTo(os, BUFFER_SIZE) }
    } finally {
        try {
            connection?.disconnect()
        } catch (ignored: Exception) {
        }
    }
}

