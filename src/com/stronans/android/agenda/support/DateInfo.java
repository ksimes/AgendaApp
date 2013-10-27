package com.stronans.android.agenda.support;

import java.util.Calendar;
import java.util.Date;

import android.text.format.Time;

public class DateInfo
{
    static final long DAYLENGTH = 86400000; // days (1000*60*60*24) mill * secs * mins * hours
    
    Calendar calendar = null;
    
    public DateInfo()
    {
        this.calendar = Calendar.getInstance();     // Default to today/Now
    }
    
    public DateInfo(Date newDate)
    {
        this.calendar = Calendar.getInstance();     // Default to today/Now

        if(newDate != null)
            this.calendar.setTime(newDate);
    }
    
    public DateInfo(Long newDateInMilli)
    {
        Date newDate = new Date(newDateInMilli);
        this.calendar = Calendar.getInstance();     // Default to today/Now
        this.calendar.setTime(newDate);
    }
    
    public DateInfo(DateInfo newDate)
    {
        this.calendar = Calendar.getInstance();     // Default to today/Now
        setToJustPastMidnight();

        if(newDate != null)
            this.calendar.setTime(newDate.getDate());
    }
    
    /**
     * Note that this routine suffers from rounding errors at the moment
     * @return number of days from today, positive for past, negative for future
     */
    public int intervalToToday()
    {
        //TODO: Fix rounding problems which make day before yesterday = yesterday
        long today = new Date().getTime() / DAYLENGTH; 
        long current = calendar.getTimeInMillis() / DAYLENGTH; 
        
        long diff = today - current;
        
        return (int)diff;     
    }

    /**
     * 
     * @return The first day of the current month (based on ordinal of Weekday enum) 
     */
    public int getFirstDayOfMonth()
    {
        Calendar c = (Calendar)calendar.clone();
        c.set(Calendar.DATE, 1);
        return c.get(Calendar.DAY_OF_WEEK);
    }
    
    /**
     * 
     * @return The current weekday of the current month
     */
    public int getCurrentDayOfMonth()
    {
        return (calendar.get(Calendar.DAY_OF_WEEK));
    }
    
    /**
     * 
     * @return The number of days in the current selected month
     */
    public int getDaysInMonth()
    {
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    
    /**
     * Shorthand method for boolean for today 
     * @return true if date value is within today.
     */
    public boolean isToday()
    {
        return equals(new DateInfo());
    }

    /**
     * Method for boolean result if to DateInfo structures are equal in year, month and date. 
     * @return true if date value is equal to compare.
     */
    public boolean equals(DateInfo compare)
    {
        boolean result = false;
        if(calendar.get(Calendar.YEAR) == compare.calendar.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == compare.calendar.get(Calendar.DAY_OF_YEAR))
            result = true;
            
        return result;
    }
    
    public void setToJustPastMidnight()
    {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND,0);
    }

    public void setToMidnight()
    {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND,0);
    }

    /**
     * 
     * @return The current selected date value (note ignore time value) 
     */
    public Date getDate()
    {
        return calendar.getTime();
    }
    
    /**
     * 
     * @return The current selected date value (note ignore time value) 
     */
    public void setMonth(int month)
    {
        calendar.set(Calendar.MONTH, month);
    }
    
    /**
     * 
     * @return The new selected date value one month forward from selected date (note ignore time value) 
     */
    public void rollMonthForward()
    {
        calendar.roll(Calendar.MONTH, true);
    }
    
    /**
     * 
     * @return The new selected date value one month back from selected date (note ignore time value) 
     */
    public void rollMonthBack()
    {
        calendar.roll(Calendar.MONTH, true);
    }
    
    /**
     * 
     * @return The current selected date value (note ignore time value) 
     */
    public Calendar getCalendar()
    {
        return calendar;
    }
    
    /**
     * 
     * @param selected Date to be set
     */
    public void setSelected(Date selected)
    {
        this.calendar.setTime(selected);
    }

    /**
     * 
     * @param selected Date to be set
     */
    public void setSelected(DateInfo selected)
    {
        this.calendar.setTime(selected.calendar.getTime());
    }

    static public int getDayOfYear(DateInfo timeInfo)
    {
        Time selected = new Time();
        selected.set(timeInfo.getDate().getTime());
        return selected.yearDay + 1;
    }
    
    static public int getWeekOfYear(DateInfo timeInfo)
    {
        Time selected = new Time();
        selected.set(timeInfo.getDate().getTime());
        return selected.getWeekNumber();
    }
}
