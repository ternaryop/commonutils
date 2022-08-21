package com.ternaryop.utils.json

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

val JSONObject.isEmptyObject: Boolean
    get() = names() == null

@Throws(JSONException::class)
@Suppress("ReturnCount")
fun Any.toJSON(): Any {
    when {
        this is Map<*, *> -> {
            val json = JSONObject()
            for (key in keys) {
                json.put(key.toString(), this.get(key)!!.toJSON())
            }
            return json
        }
        this is Iterable<*> -> {
            val json = JSONArray()
            for (value in this) {
                json.put(value)
            }
            return json
        }
        else -> return this
    }
}

@Throws(JSONException::class)
fun JSONObject.toMap(): Map<String, Any> {
    val map = HashMap<String, Any>()
    val keys = keys()
    while (keys.hasNext()) {
        val key = keys.next()
        get(key).fromJson()?.let { map[key] = it }
    }
    return map
}

@Throws(JSONException::class)
fun Any.fromJson(): Any? {
    return when {
        this === JSONObject.NULL -> null
        this is JSONObject -> toMap()
        this is JSONArray -> toList()
        else -> this
    }
}

@Throws(JSONException::class)
fun JSONArray.toList(): List<Any> {
    val list = ArrayList<Any>()
    for (i in 0 until length()) {
        get(i).fromJson()?.let { list.add(it) }
    }
    return list
}

@Throws(JSONException::class)
fun JSONObject.getMap(key: String): Map<String, Any> = getJSONObject(key).toMap()

