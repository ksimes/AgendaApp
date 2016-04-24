package com.stronans.android.agenda.model;

/**
 * Wraps a task so that a status value can be provided.
 *
 * Created by S.King on 23/04/2016.
 */
public class TaskWrapper extends Happening {

    public enum Status {
        PlannedStart,
        TargetDate
    }

    private final Task task;
    private final Status status;

    public TaskWrapper(String title, String description, Task task, Status status) {
        super(title, description);

        this.task = task;
        this.status = status;
    }

    public Task task() {
        return this.task;
    }

    public Status status() {
        return this.status;
    }

    @Override
    public ClassType classType() {
        return ClassType.TaskWrapper;
    }
}
