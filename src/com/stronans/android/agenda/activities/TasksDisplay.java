package com.stronans.android.agenda.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ListView;
import android.widget.TextView;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.adapters.AboutListAdapter;
import com.stronans.android.agenda.adapters.TaskListAdapter;
import com.stronans.android.agenda.adapters.TasksListAdapter;
import com.stronans.android.agenda.dataaccess.AgendaData;
import com.stronans.android.agenda.model.Task;

import java.util.List;

/**
 * @author SimonKing
 */
public class TasksDisplay extends Activity {
    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set and inflate our UI from its XML layout description.
        setContentView(R.layout.tasks_layout);

        List<Task> taskItems = AgendaData.getInst().getAllTasks();

        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(new TasksListAdapter(AgendaData.getInst().getContext(), taskItems));
    }
}
