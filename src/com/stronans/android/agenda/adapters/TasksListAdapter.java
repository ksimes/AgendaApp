package com.stronans.android.agenda.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.model.Task;
import com.stronans.android.agenda.support.Utilities;

import java.util.List;

public class TasksListAdapter extends BaseAdapter
{
    private List<Task> items;
    private Context context;

    public TasksListAdapter(Context context, List<Task> items)
    {
        super();
        this.context = context;
        this.items = items;
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
