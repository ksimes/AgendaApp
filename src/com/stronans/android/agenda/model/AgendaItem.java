package com.stronans.android.agenda.model;

import java.util.List;

import com.stronans.android.agenda.support.DateInfo;

/**
 * Immutable pojo object set only by the constructor
 * @author SimonKing
 *
 */
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
    public List<Incident> eventsOnThisDay()
    {
        return eventsOnThisDay;
    }
    /**
     * @return the dateToday
     */
    public DateInfo date()
    {
        return dateToday;
    }
}
