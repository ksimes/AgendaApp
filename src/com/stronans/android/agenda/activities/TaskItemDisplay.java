package com.stronans.android.agenda.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.dataaccess.AgendaData;
import com.stronans.android.agenda.model.DateInfo;
import com.stronans.android.agenda.model.Task;

/**
 * @author SimonKing
 */
public class TaskItemDisplay extends FragmentActivity {
    long taskId = 1;

    private void populateView(int resourceID, String parameter) {
        TextView field = (TextView) findViewById(resourceID);
        field.setText(parameter);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate our UI from its XML layout description.
        setContentView(R.layout.task_popup);

        // Incident details are passed as strings from the calling Activity.
        Bundle parameters = getIntent().getExtras();
        if (parameters != null) {
            taskId = parameters.getLong(Task.IdKey);

            if(taskId > 0) {
                Task task = AgendaData.getInst().getTask(taskId);

                populateView(R.id.title, task.title());
                populateView(R.id.description, task.description());

                populateView(R.id.notes, task.notes());

                String notSet = getString(R.string.dateNotSet);

                if (task.plannedStart().isDefined()) {
                    populateView(R.id.plannedstart, DateInfo.getDateTimeString(task.plannedStart()));
                } else {
                    populateView(R.id.plannedstart, notSet);
                }

                if (task.started().isDefined()) {
                    populateView(R.id.actualstart, DateInfo.getDateTimeString(task.started()));
                } else {
                    populateView(R.id.actualstart, notSet);
                }

                if (task.percentageComplete() > 0) {
                    populateView(R.id.percentcomplete, "" + task.plannedStart() + "%");
                } else {
                    populateView(R.id.percentcomplete, getString(R.string.notYetStarted));
                }

                if (task.targetDate().isDefined()) {
                    populateView(R.id.targetdate, DateInfo.getDateTimeString(task.targetDate()));
                } else {
                    populateView(R.id.targetdate, notSet);
                }

                if (task.lastUpdated().isDefined()) {
                    populateView(R.id.lastupdated, DateInfo.getDateTimeString(task.lastUpdated()));
                } else {
                    populateView(R.id.lastupdated, notSet);
                }
            }
        }
    }
}
