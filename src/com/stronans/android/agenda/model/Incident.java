package com.stronans.android.agenda.model;

import java.util.ArrayList;
import java.util.List;

import com.stronans.android.agenda.support.DateInfo;

public class Incident
{
    public static final String Title = "title";
    public static final String Description = "description";
    public static final String Location = "location";
    public static final String Period = "period";
    
    String title;                   // title of an event
    String description;             // description
    String location;           // eventLocation
    DateInfo start;
    DateInfo end;
    boolean allDay;
    int eventStatus;                // eventStatus
    boolean hasStatus;
    int calendarColour;
        
    int calendarId;             // calendar_id
    EventCategory category;
    
    public Incident()
    {
        super();
        category = new EventCategory();
        eventStatus = -1;
    }
    /**
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }
    /**
     * @param title the title to set
     */
    public void setTitle(String title)
    {
        this.title = title;
    }
    /**
     * Checks the title to see if it is non-null and has some text in it
     * @return boolean if title is non-null and non-zero length
     */
    public boolean hasTitle()
    {
        boolean result = false;
        if(title != null)
            if(title.length() > 0)
                result = true;
        
        return result;
    }
    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
    /**
     * Checks the description to see if it is non-null and has some text in it
     * @return boolean if description is non-null and non-zero length
     */
    public boolean hasDescription()
    {
        boolean result = false;
        if(description != null)
            if(description.length() > 0)
                result = true;
        
        return result;
    }
    /**
     * @return the eventLocation
     */
    public String getEventLocation()
    {
        return location;
    }
    /**
     * @param eventLocation the eventLocation to set
     */
    public void setEventLocation(String eventLocation)
    {
        this.location = eventLocation;
    }
    /**
     * Checks the eventLocation to see if it is non-null and has some text in it
     * @return boolean if eventLocation is non-null and non-zero length
     */
    public boolean hasLocation()
    {
        boolean result = false;
        if(location != null)
            if(location.length() > 0)
                result = true;
        
        return result;
    }
    /**
     * @return the start date/time of the event
     */
    public DateInfo getStart()
    {
        return start;
    }
    /**
     * @param start the start date/time to set
     */
    public void setStart(DateInfo start)
    {
        this.start = start;
    }
    /**
     * @return the end date/time
     */
    public DateInfo getEnd()
    {
        return end;
    }
    /**
     * @param end the end date/time to set
     */
    public void setEnd(DateInfo end)
    {
        this.end = end;
    }
    /**
     * @return true if the event is an allDay event
     */
    public boolean isAllDay()
    {
        return allDay;
    }
    /**
     * @param allDay the allDay status to set
     */
    public void setAllDay(boolean allDay)
    {
        this.allDay = allDay;
    }
    /**
     * @return the eventStatus
     */
    public int getEventStatus()
    {
        return eventStatus;
    }
    /**
     * @param eventStatus the eventStatus to set
     */
    public void setEventStatus(int eventStatus)
    {
        this.eventStatus = eventStatus;
    }
    /**
     * @return the hasStatus
     */
    public boolean hasStatus()
    {
        return hasStatus;
    }
    /**
     * @param hasStatus the hasStatus to set
     */
    public void setHasStatus(boolean hasStatus)
    {
        this.hasStatus = hasStatus;
    }
    /**
     * @return the calendarId
     */
    public int getCalendarId()
    {
        return calendarId;
    }
    /**
     * @param calendarId the calendarId to set
     */
    public void setCalendarId(int calendarId)
    {
        this.calendarId = calendarId;
    }
    /**
     * @return the category
     */
    public EventCategory getCategory()
    {
        return category;
    }
    /**
     * @param category the category to set
     */
    public void setCategory(EventCategory category)
    {
        this.category = category;
    }
    /**
     * @return the calendarColour
     */
    public int getCalendarColour()
    {
        return calendarColour;
    }
    /**
     * @param calendarColour the calendarColour to set
     */
    public void setCalendarColour(int calendarColour)
    {
        this.calendarColour = calendarColour;
    }

    /**
     * Extracts a subset of data for a specific date assuming that the list passed in contains
     * a larger range such as a week or Month. 
     * @param events The larger list of events for a week, month or year
     * @param thisDate The date that the data is being extracted for
     */
    static public List<Incident> extractForDate(List<Incident> events, DateInfo thisDate)
    {
        List<Incident> result = new ArrayList<Incident> ();
        
        if(events != null)
            for(Incident anEvent : events)
            {
                if(anEvent.getStart().equals(thisDate))
                {
                    result.add(anEvent);
                }
            }
        
        return result;
    }
}
