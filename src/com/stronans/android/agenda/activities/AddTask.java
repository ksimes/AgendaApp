/**
 *
 */
package com.stronans.android.agenda.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.dataaccess.AgendaData;
import com.stronans.android.agenda.fragments.dialogfragments.DateSelection;
import com.stronans.android.agenda.fragments.dialogfragments.DateSelectionDialogListener;
import com.stronans.android.agenda.model.DateInfo;
import com.stronans.android.agenda.model.Task;

/**
 * @author SimonKing
 */
public class AddTask extends Activity implements DateSelectionDialogListener {
    private final static String plannedStartKey = "plannedStart";
    private final static String actualStartKey = "actualStart";
    private final static String targetDatetKey = "targetDate";
    private long parentPosition = 1;
    private Boolean editState = false;
    DateInfo plannedStart = DateInfo.getNow(),
            actualStart = DateInfo.getNow(),
            targetDate = DateInfo.getNow();

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set and inflate our UI from its XML layout description.
        setContentView(R.layout.task_add_dialogue);

        // Pointer to parent Task is passed as a bundle from the calling Activity and converted.
        Bundle parameters = getIntent().getExtras();

        Task editTask = new Task(0, "", "", "", DateInfo.getUndefined(),
                DateInfo.getUndefined(), 0, DateInfo.getUndefined(),
                DateInfo.getNow(), parentPosition, false);

        if (parameters != null) {
            parentPosition = parameters.getLong(Task.ParentKey);
            editState = parameters.getBoolean("Edit");
            if (editState) {
                if (parameters.containsKey(Task.IdKey)) {
                    long id = parameters.getLong(Task.IdKey);
                    editTask = AgendaData.getInst().getTask(id);
                } else {
                    editState = false;
                }
            }
        }

        populateDisplay(editTask);

        // Set the submit and cancel result buttons.
        Button button = (Button) findViewById(R.id.submit);
        // Submit data so get all of the information from the entry dialog and update or create a new record.
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Task task = getTaskFromDisplay();

                // if we are editing then update the existing record.
                if (editState) {
                    AgendaData.getInst().updateTask(task);
                } else {
                    AgendaData.getInst().addNewTask(task);
                }
                finish();
            }
        });

        button = (Button) findViewById(R.id.cancel);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onFinishDateSelection(DateInfo dateInfo, String id) {
        Button button;

        switch(id)
        {
            case plannedStartKey :
                plannedStart = dateInfo;
                button = (Button) findViewById(R.id.inputplannedstart);
                button.setText(DateInfo.getDateString(plannedStart));
                break;

            case actualStartKey :
                actualStart = dateInfo;
                button = (Button) findViewById(R.id.inputstarted);
                button.setText(DateInfo.getDateString(actualStart));
                break;

            case targetDatetKey :
                targetDate = dateInfo;
                button = (Button) findViewById(R.id.inputtargetdate);
                button.setText(DateInfo.getDateString(targetDate));
                break;

        }
    }

    private void populateDisplay(final Task task) {
        String temp;

        EditText field = (EditText) findViewById(R.id.inputtitle);
        field.setText(task.title());

        field = (EditText) findViewById(R.id.inputdescription);
        field.setText(task.description());

        field = (EditText) findViewById(R.id.inputnotes);
        field.setText(task.notes());

        String notSet = getResources().getString(R.string.dateNotSet);
        Button button = (Button) findViewById(R.id.inputplannedstart);

        if (task.plannedStart().isDefined()) {
            temp = DateInfo.getDateTimeString(task.plannedStart());
        } else {
            temp = notSet;
        }

        button.setText(temp);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DateSelection dateSelectionFragment = DateSelection.newInstance(task.plannedStart(), plannedStartKey);
                // Create the fragment and show it as a dialog.
                dateSelectionFragment.show(getFragmentManager(), plannedStartKey);
            }
        });

        button = (Button) findViewById(R.id.inputstarted);
        if (task.started().isDefined()) {
            temp = DateInfo.getDateTimeString(task.started());
        } else {
            temp = notSet;
        }
        button.setText(temp);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DateSelection dateSelectionFragment = DateSelection.newInstance(task.plannedStart(), actualStartKey);
                // Create the fragment and show it as a dialog.
                dateSelectionFragment.show(getFragmentManager(), actualStartKey);
            }
        });

        button = (Button) findViewById(R.id.inputtargetdate);
        if (task.targetDate().isDefined()) {
            temp = DateInfo.getDateTimeString(task.targetDate());
        } else {
            temp = notSet;
        }
        button.setText(temp);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DateSelection dateSelectionFragment = DateSelection.newInstance(task.plannedStart(), targetDatetKey);
                // Create the fragment and show it as a dialog.
                dateSelectionFragment.show(getFragmentManager(), targetDatetKey);
            }
        });

        field = (EditText) findViewById(R.id.inputpercentage);
        field.setText(String.valueOf(task.percentageComplete()));
    }

    private Task getTaskFromDisplay() {
        EditText field = (EditText) findViewById(R.id.inputtitle);
        String title = field.getText().toString();

        field = (EditText) findViewById(R.id.inputdescription);
        String description = field.getText().toString();

        field = (EditText) findViewById(R.id.inputnotes);
        String notes = field.getText().toString();

        // TODO: Still need to save the rest of the fields here.

        return new Task(0, title, description, notes, plannedStart,
                actualStart, 0, targetDate,
                DateInfo.getNow(), parentPosition, false);
    }
}
