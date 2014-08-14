package com.stronans.android.agenda.model;

import android.text.format.Time;
import hirondelle.date4j.DateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Immutable DateTime object which wraps the hirondelle.date4j.DateTime and android.text.format objects.
 */
public class DateInfo {
    static final long DAYLENGTH = 86400000; // days (1000*60*60*24) mill * secs * mins * hours

    private Calendar calendar = null;
    private boolean notDefined = false;

    private DateInfo() {
        // Calendar object always defaults to today/Now
        this.calendar = Calendar.getInstance();
    }

    /**
     * Factory method which returns defined object set to current date and time.
     * @return defined DataInfo object set to now.
     */
    public static DateInfo getNow() {
        return new DateInfo();
    }

    /**
     * Factory method which returns undefined DateInfo object.
     * @return undefined DataInfo object.
     */
    public static DateInfo getUndefined() {
        DateInfo dateInfo = new DateInfo();
        dateInfo.notDefined = true;
        return dateInfo;
    }

    /**
     * Factory method which returns defined object set to date and time corresponding to long millisecond value.
     * @return defined DataInfo object set to corresponding millisecond value for Date/Time.
     */
    public static DateInfo fromLong(Long newDateInMilli) {
        DateInfo dateInfo = new DateInfo();
        if (newDateInMilli == Long.MIN_VALUE) {
            dateInfo.notDefined = true;
        } else {
            dateInfo.calendar.setTime(new Date(newDateInMilli));
        }
        return dateInfo;
    }

    public static DateInfo fromDateInfo(DateInfo newDate) {
        DateInfo dateInfo = new DateInfo();
        dateInfo.setToJustPastMidnight();

        if (newDate.notDefined) {
            dateInfo.notDefined = true;
        } else {
            if (newDate != null)
                dateInfo.calendar.setTimeInMillis(newDate.getMilliseconds());
        }
        return dateInfo;
    }

    public static DateInfo fromDate(int day, int month, int year) {
        DateInfo dateInfo = new DateInfo();
        dateInfo.calendar.set(Calendar.YEAR, year);
        dateInfo.calendar.set(Calendar.MONTH, month);
        dateInfo.calendar.set(Calendar.DATE, day);

        dateInfo.setToJustPastMidnight();

        return dateInfo;
    }

    public static DateInfo fromDateTime(int date, int month, int year, int hours, int minutes, int seconds) {
        DateInfo dateInfo = new DateInfo();
        dateInfo.calendar.set(Calendar.YEAR, year);
        dateInfo.calendar.set(Calendar.MONTH, month);
        dateInfo.calendar.set(Calendar.DATE, date);

        dateInfo.calendar.set(Calendar.HOUR, hours);
        dateInfo.calendar.set(Calendar.MINUTE, minutes);
        dateInfo.calendar.set(Calendar.SECOND, seconds);

        return dateInfo;
    }

    /**
     * @return number of days from today, positive for past, negative for future
     */
    public int intervalToToday() {
        DateTime today = DateTime.today(TimeZone.getDefault());

        DateTime current = DateTime.forDateOnly(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE));

        int diff = current.numDaysFrom(today);

        return diff;
    }

    public boolean isDefined()
    {
        return !notDefined;
    }

    /**
     * Formats the dateTime in this instance into a displayable string. If undefined then returns a blank string.
     * @param format Must conform to the formatting characters from
     * @return String holding formatted date/Time of blank if undefined.
     */
    public String format(String format) {
        String result = "";

        if (!notDefined) {
            DateTime current = DateTime.forInstant(calendar.getTimeInMillis(), TimeZone.getDefault());
            result = current.format(format, Locale.getDefault());
        }

        return result;
    }

    public void addToDate(int interval) {
        calendar.add(Calendar.DATE, interval);
    }

    public boolean after(DateInfo other) {
        return calendar.after(other.calendar);
    }

    public boolean before(DateInfo other) {
        return calendar.before(other.calendar);
    }

    /**
     * @return The first day of the current month (based on ordinal of Weekday enum)
     */
    public int getFirstDayOfMonth() {
        Calendar c = (Calendar) calendar.clone();
        c.set(Calendar.DATE, 1);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    /**
     * @return
     */
    public int getMonth() {
        return calendar.get(Calendar.MONTH);
    }

    public int getDateInMonth() {
        return calendar.get(Calendar.DATE);
    }

    /**
     * @return The current weekday of the current month
     */
    public int getCurrentDayOfMonth() {
        return (calendar.get(Calendar.DAY_OF_WEEK));
    }

    /**
     * @return The number of days in the current selected month
     */
    public int getDaysInMonth() {
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * Shorthand method for boolean for today
     *
     * @return true if date value is within today.
     */
    public boolean isToday() {
        return equals(new DateInfo());
    }

    /**
     * Method for boolean result if to DateInfo structures are equal in year, month and date.
     *
     * @return true if date value is equal to compare.
     */
    public boolean equals(DateInfo compare) {
        boolean result = false;
        if (calendar.get(Calendar.YEAR) == compare.calendar.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == compare.calendar.get(Calendar.DAY_OF_YEAR))
            result = true;

        return result;
    }

    public void setToJustPastMidnight() {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public void setToMidnight() {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    /**
     * @return The current selected date value in milliseconds
     */
    public Long getMilliseconds() {
        long x = Long.MIN_VALUE;

        if(!notDefined) {
            x = calendar.getTime().getTime();
        }
        return x;
    }

    /**
     *
     * @return The current selected date value (note ignore time value)
     */
    // public Date getDate()
    // {
    // return calendar.getTime();
    // }

    /**
     * @return The current selected date value (note ignore time value)
     */
    public void setMonth(int month) {
        calendar.set(Calendar.MONTH, month);
    }

    /**
     * @return The new selected date value one month forward from selected date (note ignore time value)
     */
    public void rollMonthForward() {
        calendar.roll(Calendar.MONTH, true);
    }

    /**
     * @return The new selected date value one month back from selected date (note ignore time value)
     */
    public void rollMonthBack() {
        calendar.roll(Calendar.MONTH, true);
    }

    /**
     * @param selected Date to be set
     */
    public void setSelected(Date selected) {
        this.calendar.setTime(selected);
    }

    /**
     * @param selected Date to be set
     */
    public void setSelected(DateInfo selected) {
        this.calendar.setTime(selected.calendar.getTime());
    }

    static public int getDayOfYear(DateInfo timeInfo) {
        Time selected = new Time();
        selected.set(timeInfo.getMilliseconds());
        return selected.yearDay + 1;
    }

    static public int getWeekOfYear(DateInfo timeInfo) {
        Time selected = new Time();
        selected.set(timeInfo.getMilliseconds());
        return selected.getWeekNumber();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        DateTime current = DateTime.forInstant(calendar.getTimeInMillis(), TimeZone.getDefault());

        return "DateInfo [notDefined=" + notDefined + ",  calendar=" + current.format("YYYY-MM-DD hh:mm:ss") + "]";
    }
}
