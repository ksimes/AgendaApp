package com.stronans.android.agenda.model;

import java.util.List;

import com.stronans.android.agenda.support.DateInfo;

public class AgendaItem
{
    List<Incident> eventsOnThisDay;
    DateInfo dateToday;
    
    public AgendaItem(DateInfo dateToday, List<Incident> eventsOnThisDay)
    {
        super();
        this.eventsOnThisDay = eventsOnThisDay;
        this.dateToday = dateToday;
    }
    
    /**
     * @return the eventsOnThisDay
     */
    public List<Incident> getEventsOnThisDay()
    {
        return eventsOnThisDay;
    }
    /**
     * @param eventsOnThisDay the eventsOnThisDay to set
     */
    public void setEventsOnThisDay(List<Incident> eventsOnThisDay)
    {
        this.eventsOnThisDay = eventsOnThisDay;
    }
    /**
     * @return the dateToday
     */
    public DateInfo getDate()
    {
        return dateToday;
    }
    /**
     * @param dateToday the dateToday to set
     */
    public void setDateToday(DateInfo dateToday)
    {
        this.dateToday = dateToday;
    }
}
