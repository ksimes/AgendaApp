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
import com.stronans.android.agenda.fragments.dialogfragments.DateSelectionDialog;
import com.stronans.android.agenda.model.DateInfo;
import com.stronans.android.agenda.model.Task;
import com.stronans.android.agenda.support.AlertDialog;

/**
 * @author SimonKing
 */
public class AddTask extends Activity implements DateSelectionDialog.DialogListener, AlertDialog.DialogListener {
    private Task editTask;
    private Boolean editState = false;
    private DateInfo plannedStart = DateInfo.getNow(),
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

        if (parameters != null) {
            long parentPosition = parameters.getLong(Task.ParentKey);

            // Make sure we have a valid task if this is a create
            editTask = new Task(0, "", "", "", DateInfo.getUndefined(),
                    DateInfo.getUndefined(), 0, DateInfo.getUndefined(),
                    DateInfo.getNow(), parentPosition, false);

            editState = parameters.getBoolean("Edit");
            if (editState) {
                if (parameters.containsKey(Task.IdKey)) {
                    // Get the task to edit
                    long id = parameters.getLong(Task.IdKey);
                    editTask = AgendaData.getInst().getTask(id);
                } else {
                    editState = false;
                }
            }

            populateDisplay(editTask);

            // Set the submit and cancel result buttons.
            Button button = (Button) findViewById(R.id.submit);
            // Submit data so get all of the information from the entry dialog and update or create a new record.
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(hasTitle()) {
                        Task task = getTaskFromDisplay(editTask);

                        // if we are editing then update the existing record.
                        if (editState) {
                            AgendaData.getInst().updateTask(editTask.id(), task);
                        } else {
                            AgendaData.getInst().addNewTask(task);
                        }
                        finish();
                    }
                    else
                    {
                        AlertDialog alertFragment = AlertDialog.newInstance(getResources().getString(R.string.taskTitleError),
                                getResources().getString(R.string.taskTitleErrorMsg));
                        // Create the fragment and show it as a dialog.
                        alertFragment.show(getFragmentManager(), "AlertNoTitle");
                    }
                }
            });

        }

        Button button = (Button) findViewById(R.id.cancel);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setButtonText(int resourceID, DateInfo value) {
        Button field = (Button) findViewById(resourceID);

        if (value.isDefined()) {
            field.setText(DateInfo.getDateString(value));
        } else {
            field.setText(getResources().getString(R.string.dateNotSet));
        }
    }

    @Override
    public void onFinishDateSelection(DateInfo dateInfo, String id) {
        switch (id) {
            case Task.PlannedKey:
                plannedStart = dateInfo;
                setButtonText(R.id.inputplannedstart, plannedStart);
                break;

            case Task.ActualKey:
                actualStart = dateInfo;
                setButtonText(R.id.inputstarted, actualStart);
                break;

            case Task.TargetKey:
                targetDate = dateInfo;
                setButtonText(R.id.inputtargetdate, targetDate);
                break;

        }
    }

    private void processDateSelection(DateInfo date, String key) {
        DateSelectionDialog dateSelectionDialogFragment = DateSelectionDialog.newInstance(date, key);
        // Create the fragment and show it as a dialog.
        dateSelectionDialogFragment.show(getFragmentManager(), key);
    }

    private void populateDisplay(final Task task) {

        EditText field = (EditText) findViewById(R.id.inputtitle);
        field.setText(task.title());

        field = (EditText) findViewById(R.id.inputdescription);
        field.setText(task.description());

        field = (EditText) findViewById(R.id.inputnotes);
        field.setText(task.notes());

        Button button = (Button) findViewById(R.id.inputplannedstart);
        setButtonText(R.id.inputplannedstart, task.plannedStart());
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                processDateSelection(task.plannedStart(), Task.PlannedKey);
            }
        });

        button = (Button) findViewById(R.id.inputstarted);
        setButtonText(R.id.inputstarted, task.started());
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                processDateSelection(task.started(), Task.ActualKey);
            }
        });

        button = (Button) findViewById(R.id.inputtargetdate);
        setButtonText(R.id.inputtargetdate, task.targetDate());
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                processDateSelection(task.targetDate(), Task.TargetKey);
            }
        });

        field = (EditText) findViewById(R.id.inputpercentage);
        field.setText(String.valueOf(task.percentageComplete()));
    }

    private Boolean hasTitle()
    {
        EditText field = (EditText) findViewById(R.id.inputtitle);

        return !field.getText().toString().isEmpty();
    }

    private Task getTaskFromDisplay(final Task editTask) {
        EditText field = (EditText) findViewById(R.id.inputtitle);
        String title = field.getText().toString();

        field = (EditText) findViewById(R.id.inputdescription);
        String description = field.getText().toString();

        field = (EditText) findViewById(R.id.inputnotes);
        String notes = field.getText().toString();

        // TODO: Still need to process the percentage complete field here.

        return new Task(editTask.id(), title, description, notes, plannedStart,
                actualStart, 0, targetDate,
                DateInfo.getNow(), editTask.parent(), false);
    }

    @Override
    public void doPositiveClick() {

    }

    @Override
    public void doNegativeClick() {

    }
}
