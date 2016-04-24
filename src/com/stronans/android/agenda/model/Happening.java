package com.stronans.android.agenda.model;

/**
 * Parent class for Incidents and Tasks.
 *
 * Created by S.King on 20/04/2016.
 */
abstract public class Happening {

    public enum ClassType
    {
        Message,
        Incident,
        TaskWrapper
    }

    protected final String title;                      // Short title of an Happening
    protected final String description;                // Detailed description of the Happening.

    Happening(String title, String description) {
        this.title = title;
        this.description = description;
    }

    /**
     * @return the title
     */
    public String title() {
        return title;
    }

    /**
     * @return the description
     */
    public String description() {
        return description;
    }

    public abstract ClassType classType();

    public Incident getAsIncident()
    {
        return (Incident)this;
    }

    public TaskWrapper getAsTaskWrapper()
    {
        return (TaskWrapper)this;
    }
}
