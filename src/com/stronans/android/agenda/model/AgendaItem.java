package com.stronans.android.agenda.model;

import java.util.List;


/**
 * Immutable object set only by the constructor
 *
 * @author SimonKing
 */
public final class AgendaItem {
    private final List<Happening> eventsOnThisDay;
    private final DateInfo dateToday;
    private final boolean messageOnly;

    public AgendaItem(final DateInfo dateToday, final List<Happening> eventsOnThisDay, boolean messageOnly) {
        super();
        this.eventsOnThisDay = eventsOnThisDay;
        this.dateToday = dateToday;
        this.messageOnly = messageOnly;
    }

    public AgendaItem(final DateInfo dateToday, final List<Happening> eventsOnThisDay) {
        this(dateToday, eventsOnThisDay, false);
    }

    /**
     * @return is this event to be display as a message
     */
    public boolean isMessageOnly() {
        return messageOnly;
    }

    /**
     * @return the list of eventsOnThisDay
     */
    public List<Happening> eventsOnThisDay() {
        return eventsOnThisDay;
    }

    /**
     * @return the dateToday
     */
    public DateInfo date() {
        return dateToday;
    }
}
