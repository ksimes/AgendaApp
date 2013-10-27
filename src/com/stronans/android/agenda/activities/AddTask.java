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
import com.stronans.android.agenda.model.Task;

/**
 * @author SimonKing
 *
 */
public class AddTask extends Activity
{
    long parentPosition = 1;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        // Incident details are passed as strings from the calling Activity.
        Bundle parameters = getIntent().getExtras();
        if (parameters != null)
        {
            parentPosition = parameters.getLong("Parent");
        }
       // Set and inflate our UI from its XML layout description.
        // All fields in the layout are populated from resource strings.
        // See strings.xml
        setContentView(R.layout.task_add_layout);
        
        Button button = (Button) findViewById(R.id.submit);
        
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
            {
                EditText field = (EditText) findViewById(R.id.inputtitle);
                String result = field.getText().toString();
                Task newTask = new Task();
                newTask.setTitle(result);
                
                field = (EditText) findViewById(R.id.inputdescription);
                result = field.getText().toString();
                newTask.setDescription(result);
                
                field = (EditText) findViewById(R.id.inputnotes);
                result = field.getText().toString();
                newTask.setDescription(result);
                
                newTask.setParent(parentPosition);
                
                AgendaData.getInst().addNewTask(newTask);
                finish();
            }});
        
    }

    private void ending()
    {
//        Intent result = new Intent();
//        result.putExtra("paymentDetails", paymentDetails);
//        setResult(RESULT_OK, result);        
    }
}
