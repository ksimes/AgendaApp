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
    long parentPosition = 1;

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
            parentPosition = parameters.getLong("Parent");
        }
        // Set and inflate our UI from its XML layout description.
        // All fields in the layout are populated from resource strings.
        // See strings.xml
        setContentView(R.layout.task_add_dialogue);

        Button button = (Button) findViewById(R.id.submit);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText field = (EditText) findViewById(R.id.inputtitle);
                String title = field.getText().toString();

                field = (EditText) findViewById(R.id.inputdescription);
                String description = field.getText().toString();

                field = (EditText) findViewById(R.id.inputnotes);
                String notes = field.getText().toString();

                // TODO: Still need to add the rest of the fields here.

                AgendaData.getInst().addNewTask(
                        new Task(0, title, description, notes, DateInfo.getUndefined(),
                                DateInfo.getUndefined(), 0, DateInfo.getUndefined(), DateInfo.getNow(), parentPosition, false));
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

    // private void ending()
    // {
    // Intent result = new Intent();
    // result.putExtra("paymentDetails", paymentDetails);
    // setResult(RESULT_OK, result);
    // }
}
