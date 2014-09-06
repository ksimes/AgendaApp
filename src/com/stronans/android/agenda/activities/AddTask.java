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
import com.stronans.android.agenda.model.DateInfo;
import com.stronans.android.agenda.model.Task;

/**
 * @author SimonKing
 */
public class AddTask extends Activity {
    long id = 1;
    long parentPosition = 1;
    Boolean editState = false;

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pointer to parent Task is passed as a bundle from the calling Activity and converted.
        Bundle parameters = getIntent().getExtras();
        if (parameters != null) {
            parentPosition = parameters.getLong(Task.Parent);
            editState = parameters.getBoolean("Edit");
            if (editState) {
                if (parameters.containsKey(Task.Id)) {
                    id = parameters.getLong(Task.Id);
                } else {
                    editState = false;
                }
            }
        }
        // Set and inflate our UI from its XML layout description.
        // All fields in the layout are populated from resource strings.
        // See strings.xml
        setContentView(R.layout.task_add_dialogue);

        // If we have said that this is an edit then get the information from the parameters and
        // display it in the fields.
        if (editState) {
            id = parameters.getLong(Task.Id);
            Task editTask = AgendaData.getInst().getTask(id);
            populateDisplay(editTask);
        } else {
            // Otherwise set the defaults for adding data.
            Button button = (Button) findViewById(R.id.inputplannedstart);
            String notSet = getResources().getString(R.string.dateNotSet);
            button.setText(notSet);

            button = (Button) findViewById(R.id.inputstarted);
            button.setText(notSet);

            button = (Button) findViewById(R.id.inputtargetdate);
            button.setText(notSet);

            EditText field = (EditText) findViewById(R.id.inputpercentage);
            field.setText("0");
        }

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

    private void populateDisplay(Task task) {
        String temp = "";

        EditText field = (EditText) findViewById(R.id.inputtitle);
        field.setText(task.title());

        field = (EditText) findViewById(R.id.inputdescription);
        field.setText(task.description());

        field = (EditText) findViewById(R.id.inputnotes);
        field.setText(task.notes());

        Button button = (Button) findViewById(R.id.inputplannedstart);

        if (task.plannedStart().isDefined()) {
            temp = DateInfo.getDateTimeString(task.plannedStart());
        } else {
            temp = getString(R.string.dateNotSet);
        }
        button.setText(temp);

        button = (Button) findViewById(R.id.inputstarted);
        if (task.started().isDefined()) {
            temp = DateInfo.getDateTimeString(task.started());
        } else {
            temp = getString(R.string.dateNotSet);
        }
        button.setText(temp);

        button = (Button) findViewById(R.id.inputtargetdate);
        if (task.targetDate().isDefined()) {
            temp = DateInfo.getDateTimeString(task.targetDate());
        } else {
            temp = getString(R.string.dateNotSet);
        }
        button.setText(temp);

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

        return new Task(0, title, description, notes, DateInfo.getUndefined(),
                DateInfo.getUndefined(), 0, DateInfo.getUndefined(),
                DateInfo.getNow(), parentPosition, false);
    }

    // private void ending()
    // {
    // Intent result = new Intent();
    // result.putExtra("paymentDetails", paymentDetails);
    // setResult(RESULT_OK, result);
    // }
}
