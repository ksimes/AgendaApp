package com.stronans.android.agenda.dataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;
import com.stronans.android.agenda.model.DateInfo;
import com.stronans.android.agenda.model.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class TaskListTable implements BaseColumns
{
    private static final int ID = 0;
    private static final String TITLE = "title";
    private static final int TITLE_FIELD = 1;
    private static final String DESCRIPTION = "description";
    private static final int DESCRIPTION_FIELD = 2;
    private static final String NOTES = "notes";
    private static final int NOTES_FIELD = 3;
    private static final String PLANNED_START = "plannedstart";
    private static final int PLANNED_START_FIELD = 4;
    private static final String STARTED = "started";
    private static final int STARTED_FIELD = 5;
    private static final String PERCENT_COMPLETE = "percentage";
    private static final int PERCENT_COMPLETE_FIELD = 6;
    private static final String TARGET_DATE = "targetdate";
    private static final int TARGET_DATE_FIELD = 7;
    private static final String LAST_UPDATED = "lastupdated";
    private static final int LAST_UPDATED_FIELD = 8;
    private static final String PARENT_TASK = "Parenttask";
    private static final int PARENT_TASK_FIELD = 9;

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
            + " VALUES ( 'TODO/TASK LIST', '', '', 0, 0, 0, 0, "
            + new Date().getTime() + ", 0)";

    private static final String TASKLIST_TABLE_DELETE = "DROP TABLE " + TASKLIST_TABLE_NAME + ";";

    private String[] allColumns = {TaskListTable._ID, TaskListTable.TITLE, TaskListTable.DESCRIPTION,
            TaskListTable.NOTES,
            TaskListTable.PLANNED_START, TaskListTable.STARTED,
            TaskListTable.PERCENT_COMPLETE, TaskListTable.TARGET_DATE,
            TaskListTable.LAST_UPDATED, TaskListTable.PARENT_TASK};

    private String[] IDAndTitleColumns = {TaskListTable._ID, TaskListTable.TITLE};

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

    private TaskListDb taskListdb;
    private SQLiteDatabase database;

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

    public void purgeTasks()
    {
        // Drop the the table, recreate and add the base record.
//        database.execSQL(TASKLIST_TABLE_DELETE);
//        database.execSQL(TASKLIST_TABLE_CREATE);
//        database.execSQL(TASKLIST_ADD_ROOT_RECORD);

//        applicationContext.deleteDatabase(TaskListDb.DATABASE_NAME);

    }

    // Get the number of tasks in the database table.
    public long getTaskCount()
    {
        Cursor cursor = database.query(TASKLIST_TABLE_NAME, IDAndTitleColumns, null, null, null, null, null);

        cursor.moveToFirst();
        long count = 0;
        while (!cursor.isAfterLast())
        {
            count += 1;
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();

        return count;
    }

    List<Task> getAllTasks()
    {
        List<Task> tasks = new ArrayList<>();

        Cursor cursor = database.query(TASKLIST_TABLE_NAME, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            Task task = cursorToTask(cursor, false);
            tasks.add(task);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return tasks;
    }

    private Task cursorToTask(Cursor cursor, boolean getChildren)
    {
        List<Task> childTasks = new ArrayList<>();

        long id = cursor.getLong(ID);

        if(getChildren) {
            childTasks = getTasksWithParent(id, false);
        }

        return new Task(id,
                cursor.getString(TITLE_FIELD),
                cursor.getString(DESCRIPTION_FIELD),
                cursor.getString(NOTES_FIELD),
                DateInfo.fromLong(cursor.getLong(PLANNED_START_FIELD)),
                DateInfo.fromLong(cursor.getLong(STARTED_FIELD)),
                cursor.getInt(PERCENT_COMPLETE_FIELD),
                DateInfo.fromLong(cursor.getLong(TARGET_DATE_FIELD)),
                DateInfo.fromLong(cursor.getLong(LAST_UPDATED_FIELD)),
                cursor.getLong(PARENT_TASK_FIELD),
                childTasks.size() > 0,
                childTasks
                );
    }

    boolean hasChildTasks(long id) {
        boolean result = false;
        Cursor cursor = database.query(TASKLIST_TABLE_NAME, allColumns, TaskListTable.PARENT_TASK + " = " + id, null, null,
                null, null);

        cursor.moveToFirst();

        if (!cursor.isAfterLast()) {
            result = true;
        }

        cursor.close();

        return result;
    }

    List<Task> getTasksWithParent(long id, boolean getChildren)
    {
        List<Task> tasks = new ArrayList<>();
        Cursor cursor = database.query(TASKLIST_TABLE_NAME, allColumns, TaskListTable.PARENT_TASK + " = " + id, null, null,
                null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
            tasks.add(cursorToTask(cursor, getChildren));
            cursor.moveToNext();
        }

        cursor.close();

        return tasks;
    }

    public Task getTask(long id, boolean getChildren)
    {
        Cursor cursor = database.query(TASKLIST_TABLE_NAME, allColumns, TaskListTable._ID + " = " + id, null, null, null, null);

        cursor.moveToFirst();

        Task taskObtained = cursorToTask(cursor, getChildren);
        cursor.close();

        return taskObtained;
    }

    public Task insertTask(Task newTask)
    {
        ContentValues values = new ContentValues();
        values.put(TITLE, newTask.title());
        if (newTask.description() != null)
            values.put(DESCRIPTION, newTask.description());

        if (newTask.notes() != null)
            values.put(NOTES, newTask.notes());

        values.put(STARTED, newTask.started().getMilliseconds());
        values.put(PLANNED_START, newTask.plannedStart().getMilliseconds());
        values.put(PERCENT_COMPLETE, newTask.percentageComplete());
        values.put(TARGET_DATE, newTask.targetDate().getMilliseconds());
        values.put(LAST_UPDATED, new Date().getTime());
        values.put(PARENT_TASK, newTask.parent());

        long insertId = database.insert(TASKLIST_TABLE_NAME, null, values);

        // Now look it up again so we can get back the newly created ID and return that fully populated record.
        Cursor cursor = database.query(TASKLIST_TABLE_NAME, allColumns, TaskListTable._ID + " = " + insertId,
                null, null, null, null);
        cursor.moveToFirst();

        Task updatedNewTask = cursorToTask(cursor, false);
        cursor.close();

        return updatedNewTask;
    }

    /**
     * Deletes a record in the datastore with a database id.
     *
     * @param id of task item to delete. This id is the key value in the datastore.
     * @return true if successful
     */
    public boolean deleteTask(long id)
    {
//        System.out.println("Record deleted with id: " + id);
        database.delete(TASKLIST_TABLE_NAME, TaskListTable._ID + " = " + id, null);

        return true;
    }

    public boolean updateTask(Long id, Task updatedTask)
    {
        ContentValues values = new ContentValues();
        values.put(TITLE, updatedTask.title());
        values.put(DESCRIPTION, updatedTask.description());
        values.put(NOTES, updatedTask.notes());
        values.put(STARTED, updatedTask.started().getMilliseconds());
        values.put(PLANNED_START, updatedTask.plannedStart().getMilliseconds());
        values.put(PERCENT_COMPLETE, updatedTask.percentageComplete());
        values.put(TARGET_DATE, updatedTask.targetDate().getMilliseconds());
        values.put(LAST_UPDATED, new Date().getTime());
        values.put(PARENT_TASK, updatedTask.parent());

        database.update(TASKLIST_TABLE_NAME, values, TaskListTable._ID + " = ?",new String[] {String.valueOf(id)});

        return true;
    }
}
