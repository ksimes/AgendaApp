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
import com.stronans.android.agenda.fragments.TasksFragment;
import com.stronans.android.agenda.model.Task;
import com.stronans.android.agenda.support.Utilities;

import java.text.MessageFormat;
import java.util.List;

/**
 * Lists an individual task in a list showing only those tasks at the same level (i.e. Children of an individual parent).
 */
public class TaskListAdapter extends BaseAdapter {
    private List<Task> items;
    private Context context;
    //    private AgendaConfiguration config;
    private TasksFragment parent;

    public TaskListAdapter(Context context, List<Task> items, TasksFragment parent) {
        super();
        this.context = context;
        this.items = items;
        this.parent = parent;
//        config = AgendaStaticData.getStaticData().getConfig();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int childTasksNotStarted = 0;
        int childTasksCompleted = 0;

        View view = convertView;

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.task_list_item, parent, false);

            view.findViewById(R.id.taskChildren).setOnClickListener(taskChildrenClickListener);
        }

        Task item = items.get(position);

        Utilities.setTextView(view, R.id.taskTitle, item.title());
        TextView field = (TextView) view.findViewById(R.id.taskTitle);

        // black text shows that the task is on-going, i.e. started but not yet 100%
        field.setTextColor(Color.BLACK);
        int size = item.children().size();

        // If we have child tasks then the status presented is based on the number of child tasks completed and
        // the value of the percentComplete field in this parent task is ignore.
        if (size > 0) {
            childTasksNotStarted = analyseNotStarted(item.children());
            childTasksCompleted = analyseCompleted(item.children());

            if (childTasksNotStarted == size) {
                field.setTextColor(Color.BLUE);
            } else if (childTasksCompleted == size) {
                field.setPaintFlags(field.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        } else {
            // We have no child tasks then the status presented is based on the percentComplete field.
            switch (item.percentageComplete()) {
                case 0:
                    field.setTextColor(Color.BLUE);
                    break;

                case 100:
                    field.setPaintFlags(field.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    break;
            }
        }

        View v = view.findViewById(R.id.taskChildren);
        if (v != null && item.hasChildren()) {
            v.setTag(item);
            v.setVisibility(View.VISIBLE);

            StringBuilder children = new StringBuilder(100);
            children.append(MessageFormat.format(context.getResources().getString(R.string.numberOftasks), item.children().size()));

            if (childTasksNotStarted > 0) {
                children.append(", ");
                children.append(MessageFormat.format(context.getResources().getString(R.string.notStarted), childTasksNotStarted));
            }

            if (childTasksCompleted > 0) {
                children.append(", ");
                children.append(MessageFormat.format(context.getResources().getString(R.string.tasksCompleted), childTasksCompleted));
            }

            Utilities.setTextView(view, R.id.childTasksMsg, children.toString(), true);
        } else {
            Utilities.textViewVisibility(view, R.id.childTasksMsg, false);
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

    private int sumPercentages(List<Task> childTasks, int value) {
        int result = 0;

        for (Task task : childTasks) {
            if (task.percentageComplete() == value) {
                result++;
            }
        }

        return result;
    }

    private int analyseNotStarted(List<Task> childTasks) {
        return sumPercentages(childTasks, 0);
    }

    private int analyseCompleted(List<Task> childTasks) {
        return sumPercentages(childTasks, 100);
    }

    public void updateList() {
        notifyDataSetChanged();
    }
}
