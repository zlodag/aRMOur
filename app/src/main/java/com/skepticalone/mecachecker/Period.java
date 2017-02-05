package com.skepticalone.mecachecker;

import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Period {

    private static final int MILLIS_PER_SECOND = 1000;
    private static final DateFormat sFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
    final Calendar start;
    final Calendar end;


    Period(Calendar start, Calendar end) {
        this.start = start;
        this.end = end;
    }

    Period(long startSeconds, long endSeconds) {
        start = new GregorianCalendar();
        start.setTimeInMillis(startSeconds * MILLIS_PER_SECOND);
        end = new GregorianCalendar();
        end.setTimeInMillis(endSeconds * MILLIS_PER_SECOND);
    }

    static int getDaysFromDayUntilNextDay(int fromDay, int untilNextDay) {
        return fromDay == untilNextDay ? 7 : (untilNextDay - fromDay + 7) % 7;
    }

    private static Period getNextWeekend(Calendar date) {
        Calendar start = new GregorianCalendar(
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)
        );
        start.add(Calendar.DAY_OF_WEEK, getDaysFromDayUntilNextDay(date.get(Calendar.DAY_OF_WEEK), Calendar.SATURDAY));
        Calendar end = new GregorianCalendar();
        end.setTime(start.getTime());
        end.add(Calendar.DAY_OF_WEEK, 2);
        return new Period(start, end);
    }

    private static boolean isOnWeekend(Calendar date) {
        int day = date.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.SATURDAY || day == Calendar.SUNDAY;
    }

    protected final Date getStart() {
        return start.getTime();
    }

    protected final Date getEnd() {
        return end.getTime();
    }

    public final int getYear() {
        return start.get(Calendar.YEAR);
    }

    public final int getMonth() {
        return start.get(Calendar.MONTH);
    }

    public final int getDayOfMonth() {
        return start.get(Calendar.DAY_OF_MONTH);
    }

    public final int getHour(boolean isStart) {
        return (isStart ? start : end).get(Calendar.HOUR_OF_DAY);
    }

    public final int getMinute(boolean isStart) {
        return (isStart ? start : end).get(Calendar.MINUTE);
    }

    @Override
    public String toString() {
        return sFormat.format(start.getTime()) + " - " + sFormat.format(end.getTime());
    }

    final long timeSince(Period lastPeriod) {
        return start.getTimeInMillis() - lastPeriod.end.getTimeInMillis();
    }

    public long getDuration() {
        return end.getTimeInMillis() - start.getTimeInMillis();
    }

    public final long getTimeInSeconds(boolean isStart) {
        return (isStart ? start : end).getTimeInMillis() / MILLIS_PER_SECOND;
    }

    final boolean overlapsWith(Period period) {
        return end.after(period.start) && period.end.after(start);
    }

    final boolean involvesWeekend() {
        return isOnWeekend(start) || overlapsWith(getNextWeekend(start));
    }

    @Nullable
    final Period getForbiddenWeekend() {
        Period nextWeekend = getNextWeekend(start);
        return (isOnWeekend(start) || overlapsWith(nextWeekend)) ? nextWeekend : null;
    }

    protected final boolean isSameDay() {
        return start.get(Calendar.DAY_OF_MONTH) == end.get(Calendar.DAY_OF_MONTH);
    }

    final void advance(int field, int amount) {
        start.add(field, amount);
        end.add(field, amount);
    }

}
