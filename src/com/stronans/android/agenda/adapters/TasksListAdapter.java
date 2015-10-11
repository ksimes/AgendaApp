package com.stronans.android.agenda.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.fragments.TasksFragment;
import com.stronans.android.agenda.model.AgendaConfiguration;
import com.stronans.android.agenda.model.Task;
import com.stronans.android.agenda.support.Utilities;

import java.util.List;

public class TasksListAdapter extends BaseAdapter
{
    List<Task> items;
    Context context;
    AgendaConfiguration config;

    public TasksListAdapter(Context context, List<Task> items)
    {
        super();
        this.context = context;
        this.items = items;
        config = AgendaStaticData.getStaticData().getConfig();
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

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.tasks_list_item, null);
        }

        Task item = items.get(position);

        Utilities.setTextView(view, R.id.taskTitle, item.title());

        return view;
    }
}
