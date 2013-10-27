package com.stronans.android.agenda.fragments;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.stronans.android.agenda.R;
import com.stronans.android.agenda.activities.AddTask;
import com.stronans.android.agenda.activities.TaskItemDisplay;
import com.stronans.android.agenda.adapters.TaskListAdapter;
import com.stronans.android.agenda.dataaccess.AgendaData;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.interfaces.Refresher;
import com.stronans.android.agenda.model.Task;
import com.stronans.android.agenda.support.FormattedInfo;
import com.stronans.android.controllers.AgendaConfiguration;

public class TasksFragment extends Fragment implements Refresher
{
    AgendaConfiguration config;
    TaskListAdapter taskItemsAdapter;
    OnItemClickListener onClickListener;
    ListView listView;
    List<Task> taskItems;
    long parentPosition = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = AgendaStaticData.getStaticData().getConfig();
        setHasOptionsMenu(true);

        onClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Task item = taskItems.get(position);
                
                Intent intent = new Intent(view.getContext(), TaskItemDisplay.class);
                
                intent.putExtra(Task.Title, item.getTitle());
                intent.putExtra(Task.Description, item.getDescription());
                intent.putExtra(Task.Notes, item.getNotes());

                if (item.getPlannedStart().getDate().getTime() != 0)
                {
                    intent.putExtra(Task.Planned, FormattedInfo.getDateTimeString(item.getPlannedStart()));
                }
                
                if (item.getStarted().getDate().getTime() != 0)
                {
                    intent.putExtra(Task.Actual, FormattedInfo.getDateTimeString(item.getStarted()));
                }
                
                if (item.getPercentageComplete() > 0)
                {
                    intent.putExtra(Task.Percentage, "" + item.getPlannedStart() + "%");
                }
                else
                    intent.putExtra(Task.Percentage, getString(R.string.notYetStarted));
                    
                if (item.getTargetDate().getDate().getTime() != 0)
                {
                    intent.putExtra(Task.Target, FormattedInfo.getDateTimeString(item.getTargetDate()));
                }
                
                if (item.getLastUpdated().getDate().getTime() != 0)
                {
                    intent.putExtra(Task.Updated, FormattedInfo.getDateTimeString(item.getLastUpdated()));
                }
                
                
                startActivity(intent);      
            }};
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume()
    {
        super.onResume();
        refreshDisplay();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        // Inflate our UI from its XML layout description.
        View view = inflater.inflate(R.layout.tasklayout, container, false);
        
        listView = (ListView)view.findViewById(R.id.taskList);
        listView.setOnItemClickListener(onClickListener);
        
        refreshDisplay();
        
        return view;
    }

    private List<Task> getTaskList(long parentPosition)
    {

        List<Task> items = AgendaData.getInst().getTasks(parentPosition);

        return items;
    }

    @Override
    public void refreshDisplay()
    {
        taskItems = getTaskList(parentPosition);

//        if (taskItemsAdapter == null)
//        {
            taskItemsAdapter = new TaskListAdapter(AgendaData.getInst().getContext(), taskItems);
            listView.setAdapter(taskItemsAdapter);
//        }
//        else
//        {
//            taskItemsAdapter.updateList(taskItems);
//        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.mainmenu, menu);
        inflater.inflate(R.menu.taskbasemenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Called when a menu item is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case R.id.menu_refresh:
            refreshDisplay();
            return true;

        case R.id.menu_addtask:
            Intent intent = new Intent(AgendaData.getInst().getContext(), AddTask.class);
            
            intent.putExtra("Parent", parentPosition);
            
            startActivityForResult(intent, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        refreshDisplay();
        if(resultCode == Activity.RESULT_OK)
        {
        }
    }
}
