package com.stronans.android.agenda.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.stronans.android.agenda.R;
import com.stronans.android.agenda.model.Task;

/**
 * @author SimonKing
 * 
 */
public class TaskItemDisplay extends Activity
{

    private void populateView(Bundle parameters, int resourceID, String parameterID)
    {
        TextView field = (TextView) findViewById(resourceID);
        String string = parameters.getString(parameterID);

        if (string != null)
        {
            field.setText(string);
        }
    }
    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Inflate our UI from its XML layout description.
        setContentView(R.layout.task_popup);
        
        // Incident details are passed as strings from the calling Activity.
        Bundle parameters = getIntent().getExtras();
        if (parameters != null)
        {
            populateView(parameters, R.id.title, Task.Title);
            populateView(parameters, R.id.description, Task.Description);
            populateView(parameters, R.id.notes, Task.Notes);
            populateView(parameters, R.id.plannedstart, Task.Planned);
            populateView(parameters, R.id.actualstart, Task.Actual);
            populateView(parameters, R.id.percentcomplete, Task.Percentage);
            populateView(parameters, R.id.targetdate, Task.Target);
            populateView(parameters, R.id.lastupdated, Task.Updated);
        }
    }
}
