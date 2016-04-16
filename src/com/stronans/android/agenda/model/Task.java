package com.stronans.android.agenda.model;

import com.stronans.android.agenda.BuildConfig;

import java.util.List;

public final class Task {
    public static final String IdKey = "id";
    public static final String ParentKey = "parent";
    public static final String TitleKey = "title";
    public static final String DescriptionKey = "description";
    public static final String NotesKey = "notes";
    public static final String PlannedKey = "planned";
    public static final String ActualKey = "actual";
    public static final String PercentageKey = "percentage";
    public static final String TargetKey = "target";
    public static final String UpdatedKey = "lastupdated";

    private final long id;
    private final String title;
    private final String description;
    private final String notes;
    private final DateInfo plannedStart;
    private final DateInfo started;
    private final int percentageComplete;
    private final DateInfo TargetDate;
    private final DateInfo lastUpdated;
    private final long parent;
    private final boolean hasChildren;
    private final List<Task> children;

    public Task(final long id, final String title, final String description, final String notes, final DateInfo plannedStart,
                final DateInfo started,
                final int percentageComplete, final DateInfo targetDate, final DateInfo lastUpdated, final long parent,
                final boolean hasChildren, List<Task> children
    ) {
        super();

        if (BuildConfig.DEBUG) {
//            if(id == 0) throw new IllegalArgumentException();
            if (plannedStart == null) throw new IllegalArgumentException();
            if (started == null) throw new IllegalArgumentException();
            if (targetDate == null) throw new IllegalArgumentException();
            if (lastUpdated == null) throw new IllegalArgumentException();
        }

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
        this.hasChildren = hasChildren;
        this.children = children;
    }

    /**
     * @return the id
     */
    public long id() {
        return id;
    }

    /**
     * @return the parent
     */
    public long parent() {
        return parent;
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

    /**
     * @return the notes
     */
    public String notes() {
        return notes;
    }

    /**
     * @return the plannedStart
     */
    public DateInfo plannedStart() {
        return plannedStart;
    }

    /**
     * @return the started
     */
    public DateInfo started() {
        return started;
    }

    /**
     * @return the percentageComplete
     */
    public int percentageComplete() {
        return percentageComplete;
    }

    /**
     * @return the targetDate
     */
    public DateInfo targetDate() {
        return TargetDate;
    }

    /**
     * @return the lastUpdated
     */
    public DateInfo lastUpdated() {
        return lastUpdated;
    }

    /**
     * @return true if the task has children, otherwise false
     */
    public boolean hasChildren() {
        return hasChildren;
    }

    public List<Task> children() {
        return this.children;
    }
}
