package com.stronans.android.agenda.adapters;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.stronans.android.agenda.R;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.model.AgendaItem;
import com.stronans.android.agenda.model.Incident;
import com.stronans.android.agenda.support.DateInfo;
import com.stronans.android.agenda.support.FormattedInfo;
import com.stronans.android.agenda.support.ResourceInfo;
import com.stronans.android.agenda.support.Utilities;
import com.stronans.android.controllers.AgendaConfiguration;

public class AgendaListAdapter extends BaseExpandableListAdapter
{
    List<AgendaItem> agendaItems;
    Context context;
    AgendaConfiguration config;
    Resources resources;

    public AgendaListAdapter(Context context, List<AgendaItem> agendaItems)
    {
        super();
        this.context = context;
        this.agendaItems = agendaItems;
        config = AgendaStaticData.getStaticData().getConfig();
        resources = context.getResources();
    }

    public void updateList(List<AgendaItem> items)
    {
        this.agendaItems = items;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount()
    {
        return agendaItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return agendaItems.get(groupPosition).getEventsOnThisDay().size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return agendaItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return agendaItems.get(groupPosition).getEventsOnThisDay().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        View view = convertView;

        if (view == null)
        {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.agenda_list_item, null);
        }

        AgendaItem item = agendaItems.get(groupPosition);

        String dateInfoTxt = FormattedInfo.getDateString(item.getDate());
        String intervalTxt = ResourceInfo.getIntervalString(item.getDate(), resources);

        if (item.getDate().getCalendar().get(Calendar.YEAR) != new DateInfo().getCalendar().get(Calendar.YEAR))
        {
            dateInfoTxt += " " + FormattedInfo.getYearString(item.getDate());
        }

        Utilities.setTextView(view, R.id.DateInfo, dateInfoTxt);
        Utilities.setTextView(view, R.id.IntervalInfo, "(" + intervalTxt + ")");
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        /**
         * Show each incident in a particular day.
         */
        View view = convertView;

        if (view == null)
        {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.incident_list_item, null);
        }

        Incident item = agendaItems.get(groupPosition).getEventsOnThisDay().get(childPosition);

        Utilities.setTextView(view, R.id.incidenttitle, item.getTitle());

        StringBuffer sb = new StringBuffer(30);

        if (!item.isAllDay())
        {
            sb.append(MessageFormat.format(resources.getString(R.string.time_period),
                    new Object[] { FormattedInfo.getTimeString(item.getStart()), FormattedInfo.getTimeString(item.getEnd()) }));
        }
        else
            sb.append(resources.getString(R.string.all_day_event));

        Utilities.setTextView(view, R.id.incidentperiod, sb.toString());

        String location = "";
        if(Utilities.hasContent(item.getEventLocation()))
        {
            location = resources.getString(R.string.location) + item.getEventLocation();
        }

        Utilities.setTextView(view, R.id.incidentlocation, location);

        int dayOfWeek = agendaItems.get(groupPosition).getDate().getCurrentDayOfMonth();

        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)
        {
            view.setBackgroundColor(resources.getColor(R.color.Chalk));
        }
        else
        {
            view.setBackgroundColor(resources.getColor(R.color.Ivory));
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}
