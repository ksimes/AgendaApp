package com.stronans.android.agenda.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.dataaccess.AgendaData;
import com.stronans.android.agenda.model.Task;

/**
 * @author SimonKing
 */
public class TaskManagementDisplay extends FragmentActivity {
    long taskId = 1;

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate our UI from its XML layout description.
        setContentView(R.layout.task_menu);

        // Incident details are passed as strings from the calling Activity.
        Bundle parameters = getIntent().getExtras();
        if (parameters != null) {
            taskId = parameters.getLong(Task.IdKey);
        }

        Button button = (Button) findViewById(R.id.editTaskButton);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Using id of current task.
                Intent intent = new Intent(AgendaData.getInst().getContext(), AddTask.class);
                intent.putExtra(Task.IdKey, taskId);
                intent.putExtra("Edit", true);
                startActivity(intent);
                finish();
            }
        });

        button = (Button) findViewById(R.id.addSubTaskButton);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Using id of current task.
                Intent intent = new Intent(AgendaData.getInst().getContext(), AddTask.class);
                intent.putExtra(Task.ParentKey, taskId);
                startActivity(intent);
                finish();
            }
        });

        button = (Button) findViewById(R.id.taskDeleteButton);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : Add a confirmation dialog
                AgendaData.getInst().deleteTaskAndChildren(taskId);
                finish();
            }
        });

        button = (Button) findViewById(R.id.taskCancelButton);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
