package com.stronans.android.agenda.dataaccess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Instances;

import com.stronans.android.agenda.model.Account2_2;
import com.stronans.android.agenda.model.Events2_2;
import com.stronans.android.agenda.model.Incident;
import com.stronans.android.agenda.model.Task;
import com.stronans.android.agenda.support.DateInfo;

/**
 * Gets data from persistent storage
 * 
 * @author Simon King
 * 
 * content://calendar/instances/when/#/#
 * content://calendar/instances/whenbyday/#/#
 * content://calendar/events
 * content://calendar/events/#
 * content://calendar/calendars
 * content://calendar/calendars/#
 * content://calendar/deleted_events
 * content://calendar/attendees
 * content://calendar/attendees/#
 * content://calendar/reminders
 * content://calendar/reminders/#
 * content://calendar/extendedproperties
 * content://calendar/extendedproperties/#
 * content://calendar/calendar_alerts
 * content://calendar/calendar_alerts/#
 * content://calendar/calendar_alerts/by_instance
 * content://calendar/busybits/when/#/#
 * 
 */
public class AgendaData 
{
    static AgendaData agendaData;
    static Context applicationContext;
    static TaskListTable taskListData;

    private AgendaData()
    {
        // Initial core data (load or setup)

    }

    // Singleton pattern
    static public AgendaData getInst()
    {
        if (agendaData == null)
        {
            agendaData = new AgendaData();
        }

        return agendaData;
    }
    
    private void setupTaskDB()
    {
        taskListData = new TaskListTable(applicationContext);
        taskListData.open();
    }

    public Context getContext()
    {
        return applicationContext;
    }

    public void setContext(Context context)
    {
        applicationContext = context;
        setupTaskDB();
    }

    private List<Incident> getEventsEmulator(int selectedCalendarId, DateInfo startDate, DateInfo endDate)
    {
        Date currentTime = new Date();
        List<Incident> result = new ArrayList<Incident>();

        Calendar x = Calendar.getInstance();
        x.setTime(currentTime);
        x.add(Calendar.DAY_OF_MONTH, 1);
        
        Incident event = new Incident();
        event.setTitle("Fake title which is quite long");
        event.setDescription(null);
        event.setStart(new DateInfo(x.getTime()));
        
        x.add(Calendar.HOUR, 1);
        event.setEnd(new DateInfo(x.getTime()));
        event.setCalendarColour(Color.BLUE);
        event.setCalendarId(1);
        event.setAllDay(false);
        result.add(event);

        event = new Incident();
        event.setTitle("Theo King's Birthday");
        event.setDescription("Large birthday celebrartions which will include the ritual counting of snails in the back garden and other things");
        x.add(Calendar.DAY_OF_MONTH, 1);
        event.setStart(new DateInfo(x.getTime()));
        x.add(Calendar.HOUR, 2);
        event.setEnd(new DateInfo(x.getTime()));
        event.setCalendarColour(Color.YELLOW);
        event.setCalendarId(1);
        event.setAllDay(true);
        result.add(event);

        event = new Incident();
        event.setTitle("Job Interview");
        event.setDescription("Remember to review CV and read up on Java services");
        event.setStart(new DateInfo(x.getTime()));
        x.add(Calendar.HOUR, 2);
        event.setEnd(new DateInfo(x.getTime()));
        event.setCalendarColour(Color.GREEN);
        event.setCalendarId(1);
        result.add(event);

        event = new Incident();
        event.setTitle("Meet for coffee");
        event.setDescription("look out copy of Lost Horizon for Steve");
        event.setStart(new DateInfo(x.getTime()));
        x.add(Calendar.HOUR, 2);
        event.setEnd(new DateInfo(x.getTime()));
        event.setCalendarColour(Color.BLUE);
        event.setCalendarId(1);
        result.add(event);

        event = new Incident();
        event.setTitle("Go To pub");
        event.setDescription("Meet up with John to give back DVD");
        event.setStart(new DateInfo(x.getTime()));
        x.add(Calendar.HOUR, 1);
        event.setEnd(new DateInfo(x.getTime()));
        event.setCalendarColour(Color.MAGENTA);
        event.setCalendarId(1);
        result.add(event);

        event = new Incident();
        event.setTitle("Another's Birthday");
        event.setDescription("Equally large birthday celebrartions which will mainly include the ritual counting of snails in the back garden");
        x.add(Calendar.DAY_OF_MONTH, 1);
        event.setStart(new DateInfo(x.getTime()));
        x.add(Calendar.HOUR, 1);
        event.setEnd(new DateInfo(x.getTime()));
        event.setCalendarColour(Color.GREEN);
        event.setCalendarId(1);
        event.setAllDay(false);
        result.add(event);

        event = new Incident();
        event.setTitle("Short piece of text");
        event.setDescription("Reasonable sized description");
        event.setEventLocation("Glasgow");
        x.add(Calendar.DAY_OF_MONTH, 2);
        event.setStart(new DateInfo(x.getTime()));
        x.add(Calendar.MINUTE, 35);
        event.setEnd(new DateInfo(x.getTime()));
        event.setCalendarColour(Color.MAGENTA);
        event.setCalendarId(1);
        event.setAllDay(false);
        result.add(event);

        event = new Incident();
        event.setTitle("Another reasonable long piece of text for the title text");
        event.setDescription("Reasonable sized description, which is not too long");
        event.setEventLocation("Glasgow");
        x.add(Calendar.DAY_OF_MONTH, 1);
        event.setStart(new DateInfo(x.getTime()));
        x.add(Calendar.MINUTE, 55);
        event.setEnd(new DateInfo(x.getTime()));
//        event.setCalendarColour(displayResources.getColor(R.color.Aquamarine));
        event.setCalendarColour(Color.RED);
        event.setCalendarId(1);
        event.setAllDay(false);
        result.add(event);

        return result;
    }
    
    private List<Incident> getEventsAndroid2_2(int selectedCalendarId, DateInfo startDate, DateInfo endDate)
    {
        final String ORDER_BY = "begin ASC, end ASC";
        
        List<Incident> result = new ArrayList<Incident>();

        Uri.Builder builder = Uri.parse("content://com.android.calendar/instances/when").buildUpon();
        ContentUris.appendId(builder, startDate.getDate().getTime());
        ContentUris.appendId(builder, endDate.getDate().getTime());
        
        String[] projection = new String[] { Events2_2.CALENDAR_ID, Events2_2.TITLE, Events2_2.DESCRIPTION, 
                Account2_2.COLOR, Instances.BEGIN, Instances.END, Events2_2.ALL_DAY };

        Cursor cursor = null;
        if(selectedCalendarId == 0)
            cursor = applicationContext.getContentResolver().query(builder.build(), projection,
                    "selected=1", null, ORDER_BY);
        else
            cursor = applicationContext.getContentResolver().query(builder.build(), projection,
                    "calendar_id=?", new String[] { Integer.toString(selectedCalendarId) },
                    ORDER_BY);

        if (cursor != null)
        {
            cursor.moveToFirst();
            int count = cursor.getCount();
            for (int i = 0; i < count; i++)
            {
                Incident event = new Incident();
                event.setTitle(cursor.getString(cursor.getColumnIndex(Events2_2.TITLE)));
                event.setDescription(cursor.getString(cursor.getColumnIndex(Events2_2.DESCRIPTION)));
                event.setStart(new DateInfo(cursor.getLong(cursor.getColumnIndex(Instances.BEGIN))));
                event.setEnd(new DateInfo(cursor.getLong(cursor.getColumnIndex(Instances.END))));
                event.setCalendarColour(cursor.getInt(cursor.getColumnIndex(Account2_2.COLOR)));
                event.setCalendarId(cursor.getInt(cursor.getColumnIndex(Events2_2.CALENDAR_ID)));
                event.setAllDay(getBoolean(cursor.getInt(cursor.getColumnIndex(Events2_2.ALL_DAY))));
                result.add(event);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return result;
    }
    
    private List<Incident> getEventsAndroid4Above(int selectedCalendarId, DateInfo startDate, DateInfo endDate)
    {
//        final String ORDER_BY = "begin ASC, end ASC";
        final String ORDER_BY = Instances.BEGIN + " ASC, " + Instances.END + " ASC";
        
        List<Incident> result = new ArrayList<Incident>();

     // Construct the query with the desired date range.
        Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startDate.getDate().getTime());
        ContentUris.appendId(builder, endDate.getDate().getTime());
        
        String[] projection = new String[] { Events.CALENDAR_ID, Events.TITLE, Events.DESCRIPTION, 
                Calendars.CALENDAR_COLOR, Events.EVENT_LOCATION, 
                Instances.BEGIN, Instances.END, Events.ALL_DAY };
        
        Cursor cursor = null;
        if(selectedCalendarId == 0)
            cursor = applicationContext.getContentResolver().query(builder.build(), projection,
                    "visible=1", null, ORDER_BY);
        else
            cursor = applicationContext.getContentResolver().query(builder.build(), projection,
                    "calendar_id=?", new String[] { Integer.toString(selectedCalendarId) },
                    ORDER_BY);

        if (cursor != null)
        {
            cursor.moveToFirst();
            int count = cursor.getCount();
            for (int i = 0; i < count; i++)
            {
                Incident event = new Incident();
                event.setTitle(cursor.getString(cursor.getColumnIndex(Events.TITLE)));
                event.setDescription(cursor.getString(cursor.getColumnIndex(Events.DESCRIPTION)));
                event.setEventLocation(cursor.getString(cursor.getColumnIndex(Events.EVENT_LOCATION)));
                event.setStart(new DateInfo(cursor.getLong(cursor.getColumnIndex(Instances.BEGIN))));
                event.setEnd(new DateInfo(cursor.getLong(cursor.getColumnIndex(Instances.END))));
                event.setAllDay(getBoolean(cursor.getInt(cursor.getColumnIndex(Events.ALL_DAY))));
                event.setCalendarColour(cursor.getInt(cursor.getColumnIndex(Calendars.CALENDAR_COLOR)));
                event.setCalendarId(cursor.getInt(cursor.getColumnIndex(Events.CALENDAR_ID)));
                result.add(event);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return result;
    }
    
    public List<Incident> getEvents(int selectedCalendarId, DateInfo startDate, DateInfo endDate)
    {
        String build = android.os.Build.MODEL; 
        int version = android.os.Build.VERSION.SDK_INT; 
        
        if (build.contains("sdk") || build.contains("Emulator")) 
        {
            return getEventsEmulator(selectedCalendarId, startDate, endDate);
        }
        else
            switch(version)
            {
            case android.os.Build.VERSION_CODES.FROYO :
                return getEventsAndroid2_2(selectedCalendarId, startDate, endDate);
                
            case android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH:
//            case android.os.Build.VERSION_CODES.JELLY_BEAN :
            default:
                return getEventsAndroid4Above(selectedCalendarId, startDate, endDate);
            }
    }
    
    private boolean getBoolean(int value)
    {
        Boolean result = false;
        
        if(value == 1)
            result = true;
        
        return result;
    }

    public List<Account2_2> getCalendarAccounts()
    {
        List<Account2_2> result = new ArrayList<Account2_2>();

        String[] projection = new String[] { Account2_2.ID, Account2_2.COLOR,
                Account2_2.DISPLAY_NAME, Account2_2.NAME, Account2_2.SELECTED }; 

        Uri calendars = Uri.parse("content://com.android.calendar/calendars");
        
        Cursor cursor = applicationContext.getContentResolver().query(
                calendars, projection, "selected=1", null, null); // active calendars

        if (cursor != null)
        {
            // fetching all active calendar accounts
            cursor.moveToFirst();
            int count = cursor.getCount();
            for (int i = 0; i < count; i++)
            {
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
//  ContentResolver cr = getContentResolver();
//  Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//  if (cur.getCount() > 0)
//  {
//      while (cur.moveToNext())
//      {
//          String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
//          String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//          if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
//          {
//              // Query phone here. Covered next
//          }
//      }
//  }
  
    public long addNewTask(Task newTask)
    {
        Task result = taskListData.insertTask(newTask); 
        return result.getId();
    }
    
    public List<Task> getTasks(long parent)
    {
        return taskListData.getTasksWithParent(parent);
    }
}
