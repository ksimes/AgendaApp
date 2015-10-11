package com.stronans.android.agenda.model;

import android.graphics.Color;
import com.stronans.android.agenda.BuildConfig;

import java.util.ArrayList;
import java.util.List;


/**
 * Immutable object
 *
 * @author SimonKing
 */
public final class Incident {
    public static final String Title = "title";
    public static final String Description = "description";
    public static final String Location = "location";
    public static final String Period = "period";

    private final String title;                      // Short title of an event
    private final String description;                // Detailed description
    private final String location;                   // Location (or alternate notes)
    private final DateInfo start;                    // Start date and time
    private final DateInfo end;                      // End date and time
    private final boolean allDay;                    // Flag to indicate an all day event
    private final int eventStatus;                   // eventStatus
    private final int calendarColour;
    private final int calendarId;                 // calendar_id
    private final EventCategory category;

    public Incident(final String title, final String description, final String location, final DateInfo start,
                    final DateInfo end, final boolean allDay,
                    final int eventStatus, final EventCategory category, final int calendarColour, final int calendarId) {
        super();
        if (BuildConfig.DEBUG) {
//            if(id != 0) throw new IllegalArgumentException();
            if (start == null) throw new IllegalArgumentException();
            if (end == null) throw new IllegalArgumentException();
        }

        this.title = title;
        this.description = description;
        this.location = location;
        this.start = start;
        this.end = end;
        this.allDay = allDay;
        this.eventStatus = eventStatus;
        this.calendarColour = calendarColour;
        this.calendarId = calendarId;
        this.category = category;
    }

    // Although this constructor creates an incident this item should be treated like a message which will appear on the display.
    public Incident(final String title) {
        this(title, null, null,
                DateInfo.getUndefined(), DateInfo.getUndefined(), true, -1, null,
                Color.BLACK, 1);
    }

    /**
     * @return the title
     */
    public String title() {
        return title;
    }

    /**
     * Checks the title to see if it is non-null and has some text in it
     *
     * @return boolean if title is non-null and non-zero length
     */
    public boolean hasTitle() {
        boolean result = false;
        if (title != null)
            if (title.isEmpty())
                result = true;

        return result;
    }

    /**
     * @return the description
     */
    public String description() {
        return description;
    }

    /**
     * Checks the description to see if it is non-null and has some text in it
     *
     * @return boolean if description is non-null and non-zero length
     */
    public boolean hasDescription() {
        boolean result = false;
        if (description != null)
            if (description.isEmpty())
                result = true;

        return result;
    }

    /**
     * @return the eventLocation
     */
    public String eventLocation() {
        return location;
    }

    /**
     * Checks the eventLocation to see if it is non-null and has some text in it
     *
     * @return boolean if eventLocation is non-null and non-zero length
     */
    public boolean hasLocation() {
        boolean result = false;
        if (location != null)
            if (location.length() > 0)
                result = true;

        return result;
    }

    /**
     * @return the start date/time of the event
     */
    public DateInfo startAt() {
        return start;
    }

    /**
     * @return the end date/time
     */
    public DateInfo endsAt() {
        return end;
    }

    /**
     * @return true if the event is an allDay event
     */
    public boolean isAllDay() {
        return allDay;
    }

    /**
     * @return the eventStatus
     */
    public int eventStatus() {
        return eventStatus;
    }

    /**
     * @return the calendarId
     */
    public int calendarId() {
        return calendarId;
    }

    /**
     * @return the category
     */
    public EventCategory category() {
        return category;
    }

    /**
     * @return the calendarColour
     */
    public int calendarColour() {
        return calendarColour;
    }

    /**
     * Extracts a subset of data for a specific date assuming that the list passed in contains a larger range such as a week or
     * Month.
     *
     * @param events   The larger list of events for a week, month or year
     * @param thisDate The date that the data is being extracted for
     */
    static public List<Incident> extractForDate(List<Incident> events, DateInfo thisDate) {
        List<Incident> result = new ArrayList<Incident>();

        if (events != null)
            for (Incident anEvent : events) {
                if (anEvent.startAt().equals(thisDate)) {
                    result.add(anEvent);
                }
            }

        return result;
    }
}
