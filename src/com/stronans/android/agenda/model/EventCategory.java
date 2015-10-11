package com.stronans.android.agenda.model;

/**
 * Immutable object to hold EventCategory
 *
 * @author SimonKing
 */
public final class EventCategory {
    private final long id;         // Database identifier
    private final String description; // Description of Category
    private final int marker;     // id for an icon.

    /**
     * @param id
     * @param description
     * @param marker icon index to be used for this event
     */
    public EventCategory(final long id, final String description, final int marker) {
        super();

        this.id = id;
        this.description = description;
        this.marker = marker;
    }

    /**
     * @return the id
     */
    public long id() {
        return id;
    }

    /**
     * @return the description
     */
    public String description() {
        return description;
    }

    /**
     * @return the marker
     */
    public int marker() {
        return marker;
    }
}
