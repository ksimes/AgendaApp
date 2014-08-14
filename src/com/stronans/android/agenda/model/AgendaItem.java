package com.stronans.android.agenda.model;

import java.util.List;


/**
 * Immutable object set only by the constructor
 *
 * @author SimonKing
 */
public final class AgendaItem {
    private final List<Incident> eventsOnThisDay;
    private final DateInfo dateToday;

    public AgendaItem(final DateInfo dateToday, final List<Incident> eventsOnThisDay) {
        super();
        this.eventsOnThisDay = eventsOnThisDay;
        this.dateToday = dateToday;
    }

    /**
     * @return the eventsOnThisDay
     */
    public List<Incident> eventsOnThisDay() {
        return eventsOnThisDay;
    }

    /**
     * @return the dateToday
     */
    public DateInfo date() {
        return dateToday;
    }
}
