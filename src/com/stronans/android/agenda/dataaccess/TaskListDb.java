package com.stronans.android.agenda.dataaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskListDb extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Tasks";
    
    public TaskListDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TaskListTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        TaskListTable.onUpgrade(db, oldVersion, newVersion);
    }
}
