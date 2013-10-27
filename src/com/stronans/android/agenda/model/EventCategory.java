package com.stronans.android.agenda.model;

/**
 * Immutable object to hold EventCategory
 * @author SimonKing
 *
 */
public class EventCategory
{
    long id;                // Database identifier
    String description;     // Description of Category
    int marker = 0;         // id for an icon.
    
    public EventCategory(long id, String description, int marker)
    {
        super();
        
        this.id = id;
        this.description = description;
        this.marker = marker;
    }

    /**
     * @return the id
     */
    public long id()
    {
        return id;
    }
    /**
     * @return the description
     */
    public String description()
    {
        return description;
    }
    /**
     * @return the marker
     */
    public int marker()
    {
        return marker;
    }
}
