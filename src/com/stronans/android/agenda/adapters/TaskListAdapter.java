package com.stronans.android.agenda.adapters;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.stronans.android.agenda.R;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.model.Task;
import com.stronans.android.agenda.support.Utilities;
import com.stronans.android.controllers.AgendaConfiguration;

public class TaskListAdapter extends BaseAdapter
{
    List<Task> items;
    Context context;
    AgendaConfiguration config;
    Resources resources;
    
    public TaskListAdapter(Context context, List<Task> items)
    {
        super();
        this.context = context;
        this.items = items;
        config = AgendaStaticData.getStaticData().getConfig();
        resources = context.getResources();
    }

    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public Object getItem(int location)
    {
        return items.get(location);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;

        if(view == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.task_list_item, null);
        }

        Task item = items.get(position);

        Utilities.setTextView(view, R.id.taskTitle, item.getTitle());

        return view;
    }

    public void updateList(List<Task> items)
    {
        this.items = items;
        notifyDataSetChanged();
    }
}
