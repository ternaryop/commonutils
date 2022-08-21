package com.ternaryop.utils.json

import com.ternaryop.utils.network.saveURL
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL

@Throws(JSONException::class, IOException::class)
fun InputStream.readJson(): JSONObject {
    val sb = StringBuilder()
    BufferedReader(InputStreamReader(this)).forEachLine { line ->
        sb.append(line)
    }
    return JSONTokener(sb.toString()).nextValue() as JSONObject
}

@Throws(IOException::class, JSONException::class)
fun URL.readJson(): JSONObject {
    ByteArrayOutputStream().use { os ->
        val bos = BufferedOutputStream(os)
        saveURL(bos)
        bos.flush()
        val str = String(os.toByteArray(), Charsets.UTF_8)
        return JSONTokener(str).nextValue() as JSONObject
    }
}
