package com.stronans.android.agenda.support;

import com.stronans.android.agenda.dataaccess.AgendaData;
import com.stronans.android.agenda.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilities used to process data items for Agenda and other displays
 * Created by S.King on 23/04/2016.
 */
public class AgendaUtilities {

    /**
     * Extracts a subset of data for a specific date assuming that the list passed in contains a larger range such as a week or
     * Month.
     *
     * @param events   The larger list of events for a week, month or year
     * @param thisDate The date that the data is being extracted for
     */
    static public List<Happening> extractForDate(List<Happening> events, DateInfo thisDate) {
        List<Happening> result = new ArrayList<>();

        if (events != null)
            for (Happening anEvent : events) {
                switch (anEvent.classType()) {
                    case Incident:
                        if (anEvent.getAsIncident().startAt().equals(thisDate)) {
                            result.add(anEvent);
                        }
                        break;

                    case TaskWrapper:
                        Task task = anEvent.getAsTaskWrapper().task();
                        TaskWrapper.Status status = anEvent.getAsTaskWrapper().status();

                        if ((task.plannedStart().equals(thisDate) && status.equals(TaskWrapper.Status.PlannedStart))
                                || (task.targetDate().equals(thisDate) && status.equals(TaskWrapper.Status.TargetDate))) {
                            result.add(anEvent);
                        }
                        break;

                    case Message:
                        break;
                }
            }

        return result;
    }

    /**
     * Get the list of Happenings from today for the next x days, where x is part of the config.
     * Gather events from the default Android Events table and Tasks from the tasks table.
     *
     * @param agendaRange        over what range of days to we want to show the agenda.
     * @param selectedCalendarId used by Event processing to select by Android Calendar ID.
     * @return List of Agenda items with Happenings for that date.
     */
    static public List<AgendaItem> getAgendaList(int agendaRange, int selectedCalendarId) {

        DateInfo start = DateInfo.getNow(); // set to today.
        start.setToJustPastMidnight();

        // Set to agendaRange days in the future.
        DateInfo end = DateInfo.fromDateInfo(start);
        end.addToDate(agendaRange);
        end.setToMidnight();

        List<Happening> agendaHappenings = getAgendaHappenings(selectedCalendarId, start, end);

        List<AgendaItem> allAgendaItems = new ArrayList<>();
        DateInfo startAgenda = DateInfo.fromDateInfo(start);

        // Build the agendaList for those days which actually have events/tasks in them.
        boolean finished = false;
        while (!finished) {
            List<Happening> eventsForDate = extractForDate(agendaHappenings, startAgenda);

            if (eventsForDate.size() > 0) {
                AgendaItem item = new AgendaItem(DateInfo.fromDateInfo(startAgenda), eventsForDate);
                allAgendaItems.add(item);
            }

            startAgenda.addToDate(1);
            if (startAgenda.after(end)) {
                finished = true;
            }
        }

        return allAgendaItems;
    }

    /**
     * Select all all events for a specific date range and then combine with the filtered list of tasks which have
     * planned start or target dates for the same date range.
     *
     * @param selectedCalendarId used by Event processing to select by Android Calendar ID.
     * @param startDate          Start of date range (inclusive)
     * @param endDate            End of date range (inclusive)
     * @return List of combined tasks and events for that date range.
     */
    static private List<Happening> getAgendaHappenings(int selectedCalendarId, DateInfo startDate, DateInfo endDate) {

        // Get all events which occur on this day.
        List<Happening> allHappeningItems = AgendaData.getInst().getEvents(selectedCalendarId, startDate, endDate);

        // get all the tasks and then filter them by start and end date.
        List<Task> taskHappeningItems = AgendaData.getInst().getAllTasks();

        for (Task task : taskHappeningItems) {
            // Check it twice as it is possible that the plannedStart and TargetEnd
            // are in the same date range or even on the same day.
            if (DateInfo.inRange(task.plannedStart(), startDate, endDate)) {
                allHappeningItems.add(new TaskWrapper(task.title(), task.description(), task, TaskWrapper.Status.PlannedStart));
            }

            if (DateInfo.inRange(task.targetDate(), startDate, endDate)) {
                allHappeningItems.add(new TaskWrapper(task.title(), task.description(), task, TaskWrapper.Status.TargetDate));
            }
        }

        return allHappeningItems;
    }

    /**
     * Select the correct warning colour for the task based on the days setting in the configuration.
     *
     * @param date   Date to calculate interval from
     * @param config Agenda global configuration.
     * @return Colour value as defined in colors.xml
     */
    static public int getTaskWarningColour(DateInfo date, AgendaConfiguration config) {
        int interval = Math.abs(date.intervalToToday());
        int result;

        if (interval <= config.getTaskWarningFinal()) {
            result = config.getTaskWarningFinalColour();
        } else if (interval <= config.getTaskWarningFirst()) {
            result = config.getTaskWarningFirstColour();
        } else {
            result = config.getDefaultTaskColour();
        }

        return result;
    }
}
