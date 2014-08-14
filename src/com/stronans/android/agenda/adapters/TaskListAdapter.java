package com.stronans.android.agenda.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.fragments.TasksFragment;
import com.stronans.android.agenda.model.AgendaConfiguration;
import com.stronans.android.agenda.model.Task;
import com.stronans.android.agenda.support.Utilities;

import java.util.List;

public class TaskListAdapter extends BaseAdapter
{
    List<Task> items;
    Context context;
    AgendaConfiguration config;
    TasksFragment parent;

    public TaskListAdapter(Context context, List<Task> items, TasksFragment parent)
    {
        super();
        this.context = context;
        this.items = items;
        this.parent = parent;
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
            view = vi.inflate(R.layout.task_list_item, null);

            ((ImageButton) view.findViewById(R.id.taskChildren)).setOnClickListener(taskChildrenClickListener);
        }

        Task item = items.get(position);

        Utilities.setTextView(view, R.id.taskTitle, item.title());

        View v = view.findViewById(R.id.taskChildren);
        if (v != null && item.hasChildren()) {
            v.setTag(item);
            v.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private View.OnClickListener taskChildrenClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Task item = (Task) v.getTag();
            long id = 1;

            if (item != null) {
                id = item.id();
            }

            parent.setParentPosition(id);
//            updateList();
        }
    };

    public void updateList()
    {
        notifyDataSetChanged();
    }
}
