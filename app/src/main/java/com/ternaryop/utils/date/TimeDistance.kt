package com.ternaryop.utils.date

import org.joda.time.Days
import org.joda.time.LocalDate
import org.joda.time.Years
import org.joda.time.format.DateTimeFormat
import java.util.Calendar

/**
 * Created by dave on 08/06/14.
 * Contains methods to compute days, years
 */

const val APPEND_DATE_FOR_PAST_AND_PRESENT = 1

fun Calendar.yearsBetweenDates(to: Calendar): Int {
    return Years.yearsBetween(LocalDate(this), LocalDate(to)).years
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
    } else Days.daysBetween(LocalDate(this), LocalDate()).days.toLong()
}


fun Long.formatPublishDaysAgo(flags: Int): String {
    val dayString = StringBuffer()

    if (this == Long.MAX_VALUE) {
        dayString.append("Never Published")
    } else {
        val days = daysSinceNow()
        if (days < 0) {
            if (days == -1L) {
                DateTimeFormat.forPattern("'Tomorrow at' HH:mm").printTo(dayString, this)
            } else {
                val s = DateTimeFormat.forPattern("'In %1\$d days at' HH:mm").print(this)
                dayString.append(String.format(s, -days))
            }
        } else {
            when (days) {
                0L -> DateTimeFormat.forPattern("'Today at' HH:mm").printTo(dayString, this)
                1L -> dayString.append("Yesterday")
                else -> dayString.append(String.format("%1\$d days ago", days))
            }
            val appendDate = flags and APPEND_DATE_FOR_PAST_AND_PRESENT != 0
            if (days > 1 && appendDate) {
                dayString.append(" (")
                DateTimeFormat.forPattern("dd/MM/yyyy").printTo(dayString, this)
                dayString.append(")")
            }
        }
    }
    return dayString.toString()
}
