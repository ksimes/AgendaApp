package com.stronans.android.agenda.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.activities.AddTask;
import com.stronans.android.agenda.activities.TaskItemDisplay;
import com.stronans.android.agenda.activities.TaskManagementDisplay;
import com.stronans.android.agenda.activities.TasksDisplay;
import com.stronans.android.agenda.adapters.TaskListAdapter;
import com.stronans.android.agenda.dataaccess.AgendaData;
import com.stronans.android.agenda.interfaces.Refresher;
import com.stronans.android.agenda.interfaces.SetParent;
import com.stronans.android.agenda.model.Task;

import java.util.List;

public class TasksFragment extends Fragment implements Refresher, SetParent {
    private static final long ROOT = 1L;

    //    private AgendaConfiguration config;
    private OnItemClickListener onClickListener;
    private OnItemLongClickListener onLongClickListener;
    private View mainView;
    private ListView listView;
    private List<Task> taskItems;
    private long parentPosition = ROOT;

    public TasksFragment() {
        super();
//        config = AgendaStaticData.getStaticData().getConfig();
        setHasOptionsMenu(true);
    }

    public void setParentPosition(long newPosition) {
        parentPosition = newPosition;
        refreshDisplay();
    }

    private void displayTaskItem(View view, int position) {
        Task task = taskItems.get(position);

        Intent intent = new Intent(view.getContext(), TaskItemDisplay.class);
        intent.putExtra(Task.IdKey, task.id());
        startActivity(intent);
    }

    private void displayTaskManagement(View view, int position) {
        Task task = taskItems.get(position);

        Intent intent = new Intent(view.getContext(), TaskManagementDisplay.class);
        intent.putExtra(Task.IdKey, task.id());
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        config = AgendaStaticData.getStaticData().getConfig();
        setHasOptionsMenu(true);

        onClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayTaskItem(view, position);
            }
        };

        onLongClickListener = new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                displayTaskManagement(view, position);
                return true;
            }
        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
        refreshDisplay();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate our UI from its XML layout description.
        mainView = inflater.inflate(R.layout.tasklayout, container, false);

        listView = (ListView) mainView.findViewById(R.id.taskList);
        listView.setOnItemClickListener(onClickListener);

        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(onLongClickListener);

        ImageButton imageButton = (ImageButton) mainView.findViewById(R.id.taskParent);
        imageButton.setOnClickListener(taskParentClickListener);

        refreshDisplay();

        return mainView;
    }

    private List<Task> getTaskList(long parentPosition) {
        return AgendaData.getInst().getTasks(parentPosition);
    }


    private View.OnClickListener taskParentClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Task item = AgendaData.getInst().getTask(parentPosition, true);
            long parent = 1;

            if (item != null) {
                parent = item.parent();
            }

            setParentPosition(parent);
        }
    };

    @Override
    public void refreshDisplay() {
        TaskListAdapter taskItemsAdapter;

        taskItems = getTaskList(parentPosition);

        if (mainView != null) {
            TextView tv = (TextView) mainView.findViewById(R.id.taskHeader);
            ImageButton imageButton = (ImageButton) mainView.findViewById(R.id.taskParent);

            if (parentPosition == ROOT) {
                tv.setText(getString(R.string.task_title));
                imageButton.setVisibility(View.GONE);
            } else {
                Task t = AgendaData.getInst().getTask(parentPosition, true);
                tv.setText(t.title());
                imageButton.setVisibility(View.VISIBLE);
            }
        }

        taskItemsAdapter = new TaskListAdapter(AgendaData.getInst().getContext(), taskItems, this);
        listView.setAdapter(taskItemsAdapter);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.mainmenu, menu);
        inflater.inflate(R.menu.taskbasemenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Called when a menu item is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                refreshDisplay();
                return true;

            case R.id.menu_addtask:
                Intent intent = new Intent(AgendaData.getInst().getContext(), AddTask.class);
                intent.putExtra(Task.ParentKey, parentPosition);

                startActivityForResult(intent, 0);
                return true;

            case R.id.menu_task_display:
                Intent intent2 = new Intent(AgendaData.getInst().getContext(), TasksDisplay.class);
                startActivity(intent2);
                return true;
/**
 case R.id.menu_task_purge:
 makeText(AgendaData.getInst().getContext(), "purge DB is Selected", LENGTH_SHORT).show();
 AgendaData.getInst().purgeAllTasks();
 return true;
 **/
        }

        return super.onOptionsItemSelected(item);
    }
}
