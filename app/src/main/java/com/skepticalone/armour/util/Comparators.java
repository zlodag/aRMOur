package com.skepticalone.armour.util;

import android.support.annotation.Nullable;

import org.threeten.bp.LocalDate;
import org.threeten.bp.ZonedDateTime;

public final class Comparators {

    public static boolean equalStrings(@Nullable final String string1, @Nullable final String string2) {
        return string1 == null ? string2 == null : string1.equals(string2);
    }

    public static boolean equalDateTimes(@Nullable final ZonedDateTime dateTime1, @Nullable final ZonedDateTime dateTime2) {
        return dateTime1 == null ? dateTime2 == null : (dateTime2 != null && dateTime1.isEqual(dateTime2));
    }

    public static boolean equalDates(@Nullable final LocalDate date1, @Nullable final LocalDate date2) {
        return date1 == null ? date2 == null : (date2 != null && date1.isEqual(date2));
    }

}
