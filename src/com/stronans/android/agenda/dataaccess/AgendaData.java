package com.stronans.android.agenda.dataaccess;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Instances;
import com.stronans.android.agenda.model.*;

import java.util.*;

/**
 * Gets data from persistent storage
 *
 * @author Simon King
 *         <p/>
 *         content://calendar/instances/when/#/# content://calendar/instances/whenbyday/#/# content://calendar/events
 *         content://calendar/events/# content://calendar/calendars content://calendar/calendars/#
 *         content://calendar/deleted_events content://calendar/attendees content://calendar/attendees/#
 *         content://calendar/reminders content://calendar/reminders/# content://calendar/extendedproperties
 *         content://calendar/extendedproperties/# content://calendar/calendar_alerts content://calendar/calendar_alerts/#
 *         content://calendar/calendar_alerts/by_instance content://calendar/busybits/when/#/#
 */
public class AgendaData {
    private static final int DEFAULT_ICON = 1;

    private static AgendaData agendaData;
    private static Context applicationContext;
    private static TaskListTable taskListData;
    private Random randomGenerator;

    private AgendaData() {
        randomGenerator = new Random();

        // Initial core data (load or setup)

    }

    // Singleton pattern
    static public AgendaData getInst() {
        if (agendaData == null) {
            agendaData = new AgendaData();
        }

        return agendaData;
    }

    public Context getContext() {
        return applicationContext;
    }

    public void setContext(Context context) {
        applicationContext = context;
        setupTaskDB();
    }

    private List<Incident> getEventsEmulator(int selectedCalendarId, DateInfo startDate, DateInfo endDate) {
        Date currentTime = new Date();
        List<Incident> result = new ArrayList<>();

        Calendar x = Calendar.getInstance();
        x.setTime(currentTime);
        x.add(Calendar.DAY_OF_MONTH, 1);

        Calendar y = Calendar.getInstance();
        y.setTime(x.getTime());
        y.add(Calendar.HOUR, 1);

        Incident event = new Incident("Fake title which is quite long", null, null,
                DateInfo.fromLong(x.getTimeInMillis()), DateInfo.fromLong(y.getTimeInMillis()), false, -1,
                new EventCategory(1, "", randomGenerator.nextInt(32)), Color.BLUE, 1);
        result.add(event);

        x.add(Calendar.DAY_OF_MONTH, 1);
        y.setTime(x.getTime());
        y.add(Calendar.HOUR, 2);
        event = new Incident(
                "Theo King's Birthday",
                "Large birthday celebrations which will include the ritual counting of snails in the back garden and other things",
                null,
                DateInfo.fromLong(x.getTimeInMillis()), DateInfo.fromLong(y.getTimeInMillis()), true, -1,
                new EventCategory(1, "", randomGenerator.nextInt(32)), Color.YELLOW, 1);
        result.add(event);

        x.add(Calendar.HOUR, 2);
        y.setTime(x.getTime());

        event = new Incident("Job Interview",
                "Remember to review CV and read up on Java services", null,
                DateInfo.fromLong(x.getTimeInMillis()), DateInfo.fromLong(y.getTimeInMillis()), true, -1,
                new EventCategory(1, "", randomGenerator.nextInt(32)), Color.GREEN, 1);
        result.add(event);

        x.add(Calendar.HOUR, 2);
        y.setTime(x.getTime());
        y.add(Calendar.HOUR, 2);
        event = new Incident("Meet for coffee",
                "look out copy of Lost Horizon for Steve", null,
                DateInfo.fromLong(x.getTimeInMillis()), DateInfo.fromLong(y.getTimeInMillis()), true, -1,
                new EventCategory(1, "", randomGenerator.nextInt(32)), Color.BLUE, 1);
        result.add(event);

        x.add(Calendar.HOUR, 1);
        y.setTime(x.getTime());
        y.add(Calendar.HOUR, 4);
        event = new Incident("Go To pub",
                "Meet up with John to give back DVD", null,
                DateInfo.fromLong(x.getTimeInMillis()), DateInfo.fromLong(y.getTimeInMillis()), true, -1,
                new EventCategory(1, "", randomGenerator.nextInt(32)), Color.MAGENTA, 1);
        result.add(event);

        // event = new Incident();
        // event.setTitle("Another's Birthday");
        // event.setDescription("Equally large birthday celebrartions which will mainly include the ritual counting of snails in the back garden");
        // x.add(Calendar.DAY_OF_MONTH, 1);
        // event.setStart(new DateInfo(x.getTime()));
        // x.add(Calendar.HOUR, 1);
        // event.setEnd(new DateInfo(x.getTime()));
        // event.setCalendarColour(Color.GREEN);
        // event.setCalendarId(1);
        // event.setAllDay(false);
        // result.add(event);
        //
        // event = new Incident();
        // event.setTitle("Short piece of text");
        // event.setDescription("Reasonable sized description");
        // event.setEventLocation("Glasgow");
        // x.add(Calendar.DAY_OF_MONTH, 2);
        // event.setStart(new DateInfo(x.getTime()));
        // x.add(Calendar.MINUTE, 35);
        // event.setEnd(new DateInfo(x.getTime()));
        // event.setCalendarColour(Color.MAGENTA);
        // event.setCalendarId(1);
        // event.setAllDay(false);
        // result.add(event);
        //
        // event = new Incident();
        // event.setTitle("Another reasonable long piece of text for the title text");
        // event.setDescription("Reasonable sized description, which is not too long");
        // event.setEventLocation("Glasgow");
        // x.add(Calendar.DAY_OF_MONTH, 1);
        // event.setStart(new DateInfo(x.getTime()));
        // x.add(Calendar.MINUTE, 55);
        // event.setEnd(new DateInfo(x.getTime()));
        // // event.setCalendarColour(displayResources.getColor(R.color.Aquamarine));
        // event.setCalendarColour(Color.RED);
        // event.setCalendarId(1);
        // event.setAllDay(false);
        // result.add(event);

        return result;
    }

    private List<Incident> getEventsAndroid2_2(int selectedCalendarId, DateInfo startDate, DateInfo endDate) {
        final String ORDER_BY = "begin ASC, end ASC";

        List<Incident> result = new ArrayList<>();

        Uri.Builder builder = Uri.parse("content://com.android.calendar/instances/when").buildUpon();
        ContentUris.appendId(builder, startDate.getMilliseconds());
        ContentUris.appendId(builder, endDate.getMilliseconds());

        String[] projection = new String[]{Events2_2.CALENDAR_ID, Events2_2.TITLE, Events2_2.DESCRIPTION,
                Account2_2.COLOR, Instances.BEGIN, Instances.END, Events2_2.ALL_DAY};

        Cursor cursor;
        if (selectedCalendarId == 0)
            cursor = applicationContext.getContentResolver().query(builder.build(), projection,
                    "selected=1", null, ORDER_BY);
        else
            cursor = applicationContext.getContentResolver().query(builder.build(), projection,
                    "calendar_id=?", new String[]{Integer.toString(selectedCalendarId)},
                    ORDER_BY);

        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getCount();
            for (int i = 0; i < count; i++) {
                String title = cursor.getString(cursor.getColumnIndex(Events2_2.TITLE));
                String description = cursor.getString(cursor.getColumnIndex(Events2_2.DESCRIPTION));
                String location = "";
                DateInfo start = DateInfo.fromLong(cursor.getLong(cursor.getColumnIndex(Instances.BEGIN)));
                DateInfo end = DateInfo.fromLong(cursor.getLong(cursor.getColumnIndex(Instances.END)));
                int calendarColour = cursor.getInt(cursor.getColumnIndex(Account2_2.COLOR));
                int calendarId = cursor.getInt(cursor.getColumnIndex(Events2_2.CALENDAR_ID));
                boolean allDay = getBoolean(cursor.getInt(cursor.getColumnIndex(Events2_2.ALL_DAY)));

                result.add(new Incident(title, description, location, start, end, allDay, -1,
                        new EventCategory(1, "", randomGenerator.nextInt(32)), calendarColour,
                        calendarId));
                cursor.moveToNext();
            }
            cursor.close();
        }

        return result;
    }

    private List<Incident> getEventsAndroid4Above(int selectedCalendarId, DateInfo startDate, DateInfo endDate) {
        // final String ORDER_BY = "begin ASC, end ASC";
        final String ORDER_BY = Instances.BEGIN + " ASC, " + Instances.END + " ASC";

        // Construct the query with the desired date range.
        Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startDate.getMilliseconds());
        ContentUris.appendId(builder, endDate.getMilliseconds());

        // Get the columns that you are interested in.
        String[] projection = new String[]{Events.CALENDAR_ID, Events.TITLE, Events.DESCRIPTION,
                Calendars.CALENDAR_COLOR, Events.EVENT_LOCATION,
                Instances.BEGIN, Instances.END, Events.ALL_DAY};

        Cursor cursor;
        // if we have not selected a Calendar ID the we want all of the Incidents which fall between these periods for all calendars and are visible.
        if (selectedCalendarId == 0)
            cursor = applicationContext.getContentResolver().query(builder.build(), projection,
                    "visible=1", null, ORDER_BY);
        else
            // just select the Incidents which fall between these periods for the specific calendar.
            cursor = applicationContext.getContentResolver().query(builder.build(), projection,
                    "calendar_id=?", new String[]{Integer.toString(selectedCalendarId)},
                    ORDER_BY);

        List<Incident> result = new ArrayList<>();

        if (cursor != null) {
            cursor.moveToFirst();
            // extract these functions into static variables to give us a little extra speed when extracting from the tables.
            int count = cursor.getCount();

            int titleCol = cursor.getColumnIndex(Events.TITLE);
            int descriptionCol = cursor.getColumnIndex(Events.DESCRIPTION);
            int eventCol = cursor.getColumnIndex(Events.EVENT_LOCATION);
            int beginCol = cursor.getColumnIndex(Instances.BEGIN);
            int endCol = cursor.getColumnIndex(Instances.END);
            int allDatCol = cursor.getColumnIndex(Events.ALL_DAY);
            int calCol = cursor.getColumnIndex(Calendars.CALENDAR_COLOR);
            int calIdCol = cursor.getColumnIndex(Events.CALENDAR_ID);

            for (int i = 0; i < count; i++) {
                String title = cursor.getString(titleCol);
                String description = cursor.getString(descriptionCol);
                String location = cursor.getString(eventCol);
                DateInfo start = DateInfo.fromLong(cursor.getLong(beginCol));
                DateInfo end = DateInfo.fromLong(cursor.getLong(endCol));
                boolean allDay = getBoolean(cursor.getInt(allDatCol));
                int calendarColour = cursor.getInt(calCol);
                int calendarId = cursor.getInt(calIdCol);

                result.add(new Incident(title, description, location, start, end, allDay, -1,
                        new EventCategory(1, "", DEFAULT_ICON), calendarColour, calendarId));

                cursor.moveToNext();
            }
            cursor.close();
        }

        return result;
    }

    public List<Incident> getEvents(int selectedCalendarId, DateInfo startDate, DateInfo endDate) {
        String build = android.os.Build.MODEL;
        int version = android.os.Build.VERSION.SDK_INT;

        if (build.contains("x86")) {
            return getEventsEmulator(selectedCalendarId, startDate, endDate);
        } else
            switch (version) {
                case android.os.Build.VERSION_CODES.FROYO:
                    return getEventsAndroid2_2(selectedCalendarId, startDate, endDate);

                case android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH:
                    // case android.os.Build.VERSION_CODES.JELLY_BEAN :
                default:
                    return getEventsAndroid4Above(selectedCalendarId, startDate, endDate);
            }
    }

    private boolean getBoolean(int value) {
        Boolean result = false;

        if (value == 1)
            result = true;

        return result;
    }

    public List<Account2_2> getCalendarAccounts() {
        List<Account2_2> result = new ArrayList<Account2_2>();

        String[] projection = new String[]{Account2_2.ID, Account2_2.COLOR,
                Account2_2.DISPLAY_NAME, Account2_2.NAME, Account2_2.SELECTED};

        Uri calendars = Uri.parse("content://com.android.calendar/calendars");

        Cursor cursor = applicationContext.getContentResolver().query(
                calendars, projection, "selected=1", null, null); // active calendars

        if (cursor != null) {
            // fetching all active calendar accounts
            cursor.moveToFirst();
            int count = cursor.getCount();
            for (int i = 0; i < count; i++) {
                Account2_2 cal = new Account2_2();
                cal.setId(cursor.getInt(cursor.getColumnIndex(Account2_2.ID)));
                cal.setCalendarColour(cursor.getInt(cursor.getColumnIndex(Account2_2.COLOR)));
                cal.setCalendarDisplayName(cursor.getString(cursor.getColumnIndex(Account2_2.DISPLAY_NAME)));
                cal.setName(cursor.getString(cursor.getColumnIndex(Account2_2.NAME)));

                result.add(cal);
                cursor.moveToNext();
            }

            cursor.close();
        }

        return result;
    }

    // TODO stub for pulling in Contacts
    // ContentResolver cr = getContentResolver();
    // Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
    // if (cur.getCount() > 0)
    // {
    // while (cur.moveToNext())
    // {
    // String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
    // String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
    // if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
    // {
    // // Query phone here. Covered next
    // }
    // }
    // }

    private void setupTaskDB() {
        taskListData = new TaskListTable(applicationContext);
        taskListData.open();
    }

    public long addNewTask(Task newTask) {
        Task result = taskListData.insertTask(newTask);
        return result.id();
    }

    public boolean updateTask(Long id, Task newTask) {
        return taskListData.updateTask(id, newTask);
    }

    public boolean deleteTask(Long id) {
        return taskListData.deleteTask(id);
    }

    public List<Task> getTasks(long parent) {
        return taskListData.getTasksWithParent(parent, true);
    }

    public List<Task> getAllTasks() {
        return taskListData.getAllTasks();
    }

    public Task getTask(long parent, boolean getChildren) {
        return taskListData.getTask(parent, getChildren);
    }

    public Boolean hasTaskChildren(long parent) {
        return taskListData.hasChildTasks(parent);
    }

//    public void purgeAllTasks() {
//        taskListData.purgeTasks();
//    }

    // Recursive task to descend and delete all the child tasks which may be associated with the task being deleted.
    public void deleteTaskAndChildren(long taskId) {
        List<Task> childTasks = AgendaData.getInst().getTasks(taskId);

        if (!childTasks.isEmpty()) {
            // Delete all of the child tasks
            for (Task task : childTasks) {
                deleteTaskAndChildren(task.id());
            }
        }

        // Using id of current task delete this task.
        deleteTask(taskId);
    }
}
