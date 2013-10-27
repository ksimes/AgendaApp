package com.stronans.android.agenda.model;

public class EventCategory
{
    long id;                // Database identifier
    String description;     // Description of Category
    int marker = 10;         // id for an icon.
    
    /**
     * @return the id
     */
    public long getId()
    {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(long id)
    {
        this.id = id;
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
     * @return the marker
     */
    public int getMarker()
    {
        return marker;
    }
    /**
     * @param marker the marker to set
     */
    public void setMarker(int marker)
    {
        this.marker = marker;
    }
}
