package com.stronans.android.agenda.model;

import com.stronans.android.agenda.support.DateInfo;

public class Task
{
    public static final String Title = "title";
    public static final String Description = "description";
    public static final String Notes = "notes";
    public static final String Planned = "planned";
    public static final String Actual = "actual";
    public static final String Percentage = "percentage";
    public static final String Target = "actual";
    public static final String Updated = "lastupdated";
    
    long id;
    String title;
    String description;
    String notes;
    DateInfo plannedStart;
    DateInfo started;
    int percentageComplete;
    DateInfo TargetDate;
    DateInfo lastUpdated;
    long parent;
    
    public Task()
    {
        super();
        this.plannedStart = new DateInfo(0L);
        this.started = new DateInfo(0L);
        this.percentageComplete = 0;
        TargetDate = new DateInfo(0L);
        this.lastUpdated = new DateInfo(0L);
        this.parent = 0;
    }

    public Task(long id, String title, String description, String notes, DateInfo plannedStart, DateInfo started,
            int percentageComplete, DateInfo targetDate, DateInfo lastUpdated, long parent)
    {
        this();
        this.id = id;
        this.title = title;
        this.description = description;
        this.notes = notes;
        this.plannedStart = plannedStart;
        this.started = started;
        this.percentageComplete = percentageComplete;
        TargetDate = targetDate;
        this.lastUpdated = lastUpdated;
        this.parent = parent;
    }

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
     * @return the parent
     */
    public long getParent()
    {
        return parent;
    }
    /**
     * @param parent the parent to set
     */
    public void setParent(long parent)
    {
        this.parent = parent;
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
     * @return the notes
     */
    public String getNotes()
    {
        return notes;
    }
    /**
     * @param notes the notes to set
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }
    /**
     * @return the plannedStart
     */
    public DateInfo getPlannedStart()
    {
        return plannedStart;
    }
    /**
     * @param plannedStart the plannedStart to set
     */
    public void setPlannedStart(DateInfo plannedStart)
    {
        this.plannedStart = plannedStart;
    }
    /**
     * @return the started
     */
    public DateInfo getStarted()
    {
        return started;
    }
    /**
     * @param started the started to set
     */
    public void setStarted(DateInfo started)
    {
        this.started = started;
    }
    /**
     * @return the percentageComplete
     */
    public int getPercentageComplete()
    {
        return percentageComplete;
    }
    /**
     * @param percentageComplete the percentageComplete to set
     */
    public void setPercentageComplete(int percentageComplete)
    {
        this.percentageComplete = percentageComplete;
    }
    /**
     * @return the targetDate
     */
    public DateInfo getTargetDate()
    {
        return TargetDate;
    }
    /**
     * @param targetDate the targetDate to set
     */
    public void setTargetDate(DateInfo targetDate)
    {
        TargetDate = targetDate;
    }
    /**
     * @return the lastUpdated
     */
    public DateInfo getLastUpdated()
    {
        return lastUpdated;
    }
    /**
     * @param lastUpdated the lastUpdated to set
     */
    public void setLastUpdated(DateInfo lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }
}
