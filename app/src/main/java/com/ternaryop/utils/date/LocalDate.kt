package com.ternaryop.utils.date

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

fun Long.millisToLocalDateTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime {
    return Instant.ofEpochMilli(this).atZone(zoneId).toLocalDateTime()
}

fun Long.millisToLocalDate(zoneId: ZoneId = ZoneId.systemDefault()): LocalDate {
    return Instant.ofEpochMilli(this).atZone(zoneId).toLocalDate()
}

fun Long.secondsToLocalDateTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime {
    return Instant.ofEpochSecond(this).atZone(zoneId).toLocalDateTime()
}

