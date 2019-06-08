package com.ternaryop.utils.date

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val ISO_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.US)

fun Calendar.fromIsoFormat(isoDateString: String): Calendar {
    val c = Calendar.getInstance()
    c.time = ISO_DATE_FORMAT.parse(isoDateString) ?: return c
    return c
}

fun Calendar.toIsoFormat(): String = ISO_DATE_FORMAT.format(time)
