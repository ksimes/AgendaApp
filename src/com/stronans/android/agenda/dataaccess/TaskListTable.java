package com.stronans.android.agenda.dataaccess;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.stronans.android.agenda.model.Task;
import com.stronans.android.agenda.support.DateInfo;

public class TaskListTable implements BaseColumns
{
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String NOTES = "notes";
    public static final String PLANNED_START = "plannedstart";
    public static final String STARTED = "started";
    public static final String PERCENT_COMPLETE = "percentage";
    public static final String TARGET_DATE = "targetdate";
    public static final String LAST_UPDATED = "lastupdated";
    public static final String PARENT_TASK = "Parenttask";

    private static final String TASKLIST_TABLE_NAME = "tasklist";
    private static final String TASKLIST_TABLE_CREATE = "CREATE TABLE " + TASKLIST_TABLE_NAME + " (" 
            + TaskListTable._ID + " integer primary key autoincrement, "
            + TaskListTable.TITLE + " TEXT, " 
            + TaskListTable.DESCRIPTION + " TEXT,"
            + TaskListTable.NOTES + " TEXT,"
            + TaskListTable.PLANNED_START + " INTEGER,"
            + TaskListTable.STARTED + " INTEGER,"
            + TaskListTable.PERCENT_COMPLETE + " INTEGER,"
            + TaskListTable.TARGET_DATE + " INTEGER," 
            + TaskListTable.LAST_UPDATED + " INTEGER," 
            + TaskListTable.PARENT_TASK + " INTEGER NOT NULL );";

    private static final String TASKLIST_ADD_ROOT_RECORD = "INSERT INTO " + TASKLIST_TABLE_NAME + " (" 
    + TaskListTable.TITLE + ","
    + TaskListTable.DESCRIPTION + ","
    + TaskListTable.NOTES + ","
    + TaskListTable.PLANNED_START + ","
    + TaskListTable.STARTED + ","
    + TaskListTable.PERCENT_COMPLETE + ","
    + TaskListTable.TARGET_DATE + "," 
    + TaskListTable.LAST_UPDATED + "," 
    + TaskListTable.PARENT_TASK + " )" 
    + " VALUES ( 'TODO/TASK LIST', '', '', 0, 0, 0, 0, " + new Date().getTime() + ", 0)";
    
    private String[] allColumns = { TaskListTable._ID, TaskListTable.TITLE, TaskListTable.DESCRIPTION, TaskListTable.NOTES,
            TaskListTable.PLANNED_START, TaskListTable.STARTED, TaskListTable.PERCENT_COMPLETE, TaskListTable.TARGET_DATE, 
            TaskListTable.LAST_UPDATED, TaskListTable.PARENT_TASK };

    public static void onCreate(SQLiteDatabase db)
    {
        db.execSQL(TASKLIST_TABLE_CREATE);
        db.execSQL(TASKLIST_ADD_ROOT_RECORD);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w(TaskListDb.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TASKLIST_TABLE_NAME);
        onCreate(db);
    }

    TaskListDb taskListdb;
    SQLiteDatabase database;

    public TaskListTable(Context context)
    {
        taskListdb = new TaskListDb(context);
    }

    public void open() throws SQLException
    {
        database = taskListdb.getWritableDatabase();
    }

    public void close()
    {
        taskListdb.close();
    }

    public List<Task> getAllTasks()
    {
        List<Task> tasks = new ArrayList<Task>();

        Cursor cursor = database.query(TASKLIST_TABLE_NAME, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            Task task = cursorToTask(cursor);
            tasks.add(task);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return tasks;
    }

    private Task cursorToTask(Cursor cursor)
    {
        Task task = new Task();
        task.setId(cursor.getLong(0));
        task.setTitle(cursor.getString(1));
        task.setDescription(cursor.getString(2));
        task.setNotes(cursor.getString(3));
        task.setPlannedStart(new DateInfo(cursor.getLong(4)));
        task.setStarted(new DateInfo(cursor.getLong(5)));
        task.setPercentageComplete(cursor.getInt(6));
        task.setTargetDate(new DateInfo(cursor.getLong(7)));
        task.setLastUpdated(new DateInfo(cursor.getLong(8)));
        task.setParent(cursor.getLong(9));
        return task;
    }

    public List<Task> getTasksWithParent(long id)
    {
        List<Task> tasks = new ArrayList<Task>();
        Cursor cursor = database.query(TASKLIST_TABLE_NAME, allColumns, TaskListTable.PARENT_TASK + " = " + id, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
            Task taskObtained = cursorToTask(cursor);
            tasks.add(taskObtained);
            cursor.moveToNext();
        }
        
        cursor.close();

        return tasks;
    }

    public Task getTask(int id)
    {
        Cursor cursor = database.query(TASKLIST_TABLE_NAME, allColumns, TaskListTable._ID + " = " + id, null, null, null, null);

        cursor.moveToFirst();

        Task taskObtained = cursorToTask(cursor);
        cursor.close();

        return taskObtained;
    }

    public Task insertTask(Task newTask)
    {
        ContentValues values = new ContentValues();
        values.put(TITLE, newTask.getTitle());
        if (newTask.getDescription() != null)
            values.put(DESCRIPTION, newTask.getDescription());

        if (newTask.getNotes() != null)
            values.put(NOTES, newTask.getNotes());

        values.put(STARTED, newTask.getStarted().getDate().getTime());
        values.put(PLANNED_START, newTask.getPlannedStart().getDate().getTime());
        values.put(PERCENT_COMPLETE, newTask.getPercentageComplete());
        values.put(TARGET_DATE, newTask.getTargetDate().getDate().getTime());
        values.put(LAST_UPDATED, new Date().getTime());
        values.put(PARENT_TASK, newTask.getParent());

        long insertId = database.insert(TASKLIST_TABLE_NAME, null, values);
        
        Cursor cursor = database.query(TASKLIST_TABLE_NAME, allColumns, TaskListTable._ID + " = " + insertId, null, null, null,
                null);
        cursor.moveToFirst();

        Task updatedNewTask = cursorToTask(cursor);
        cursor.close();

        return updatedNewTask;
    }

    public boolean deleteTask(long id)
    {
        System.out.println("Record deleted with id: " + id);
        database.delete(TASKLIST_TABLE_NAME, TaskListTable._ID + " = " + id, null);

        return false;
    }

    public boolean updateTask(int id, Task updatedTask)
    {
        return false;
    }
}
