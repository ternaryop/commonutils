package com.ternaryop.utils.date

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.ChronoUnit.DAYS
import java.time.temporal.ChronoUnit.YEARS
import java.util.Calendar

/**
 * Created by dave on 08/06/14.
 * Contains methods to compute days, years
 */

const val APPEND_DATE_FOR_PAST_AND_PRESENT = 1

private val DATE_FORMATTER_TOMORROW = DateTimeFormatter.ofPattern("'Tomorrow at' HH:mm")
private val DATE_FORMATTER_TODAY = DateTimeFormatter.ofPattern("'Today at' HH:mm")
private val DATE_FORMATTER_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy")
private val DATE_FORMATTER_IN_DAYS = DateTimeFormatter.ofPattern("'In %1\$d days at' HH:mm")

/**
 * Calculate the years between the current calendar and the passed one, if null 'now' is used
 */
fun Calendar.yearsBetweenDates(to: Calendar? = null): Int {
    val now = LocalDate.of(get(Calendar.YEAR), get(Calendar.MONTH) + 1, get(Calendar.DAY_OF_MONTH))

    val toLocal = to?.run {
        LocalDate.of(get(Calendar.YEAR), get(Calendar.MONTH) + 1, get(Calendar.DAY_OF_MONTH))
    } ?: LocalDate.now()

    return YEARS.between(now, toLocal).toInt()
}

/**
 * Determine days difference since this timestamp to current time
 * if timestamp is equal to Long.MAX_VALUE then return Long.MAX_VALUE
 *
 * @return numbers of days, if negative indicates days in the future beyond passed timestamp
 */
fun Long.daysSinceNow(): Long {
    return if (this == Long.MAX_VALUE) {
        Long.MAX_VALUE
    } else {
        DAYS.between(millisToLocalDate(), LocalDate.now())
    }
}

fun Long.formatPublishDaysAgo(flags: Int): String {
    val dayString = StringBuffer()

    if (this == Long.MAX_VALUE) {
        dayString.append("Never Published")
    } else {
        val localDateTime = millisToLocalDateTime()
        val days = ChronoUnit.DAYS.between(localDateTime.toLocalDate(), LocalDate.now())
        if (days < 0) {
            if (days == -1L) {
                DATE_FORMATTER_TOMORROW.formatTo(localDateTime, dayString)
            } else {
                val s = DATE_FORMATTER_IN_DAYS.format(localDateTime)
                dayString.append(String.format(s, -days))
            }
        } else {
            when (days) {
                0L -> {
                    DATE_FORMATTER_TODAY.formatTo(localDateTime, dayString)
                }
                1L -> dayString.append("Yesterday")
                else -> dayString.append(String.format("%1\$d days ago", days))
            }
            val appendDate = flags and APPEND_DATE_FOR_PAST_AND_PRESENT != 0
            if (days > 1 && appendDate) {
                dayString.append(" (")
                DATE_FORMATTER_DATE.formatTo(localDateTime, dayString)
                dayString.append(")")
            }
        }
    }
    return dayString.toString()
}
