package com.ternaryop.utils;

import java.util.Calendar;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by dave on 08/06/14.
 * Contains methods to compute days, years
 */
public class DateTimeUtils {
    public final static int APPEND_DATE_FOR_PAST_AND_PRESENT = 1;

    public static int yearsBetweenDates(Calendar from, Calendar to) {
        return Years.yearsBetween(new LocalDate(from), new LocalDate(to)).getYears();
    }

    /**
     * Determine days difference since timestamp to current time
     * if timestamp is equal to Long.MAX_VALUE then return Long.MAX_VALUE
     *
     * @param timestamp the start milliseconds from which start to count days
     * @return numbers of days, if negative indicates days in the future beyond
     * passed timestamp
     */
    public static long daysSinceTimestamp(long timestamp) {
        if (timestamp == Long.MAX_VALUE) {
            return Long.MAX_VALUE;
        }
        return Days.daysBetween(new LocalDate(timestamp), new LocalDate()).getDays();
    }

    public static String formatPublishDaysAgo(long timestamp, int flags) {
        StringBuffer dayString = new StringBuffer();

        if (timestamp == Long.MAX_VALUE) {
            dayString.append("Never Published");
        } else {
            long days = daysSinceTimestamp(timestamp);
            if (days < 0) {
                if (days == -1) {
                    DateTimeFormat.forPattern("'Tomorrow at' HH:mm").printTo(dayString, timestamp);
                } else {
                    String s = DateTimeFormat.forPattern("'In %1$d days at' HH:mm").print(timestamp);
                    dayString.append(String.format(s, -days));
                }
            } else {
                if (days == 0) {
                    DateTimeFormat.forPattern("'Today at' HH:mm").printTo(dayString, timestamp);
                } else if (days == 1) {
                    dayString.append("Yesterday");
                } else {
                    dayString.append(String.format("%1$d days ago", days));
                }
                boolean appendDate = (flags & APPEND_DATE_FOR_PAST_AND_PRESENT) != 0;
                if (days > 1 && appendDate) {
                    dayString.append(" (");
                    DateTimeFormat.forPattern("dd/MM/yyyy").printTo(dayString, timestamp);
                    dayString.append(")");
                }
            }
        }
        return dayString.toString();
    }
}
