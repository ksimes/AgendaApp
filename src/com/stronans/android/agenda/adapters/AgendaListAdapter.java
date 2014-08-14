package com.stronans.android.agenda.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.enums.FormatStyle;
import com.stronans.android.agenda.model.AgendaConfiguration;
import com.stronans.android.agenda.model.AgendaItem;
import com.stronans.android.agenda.model.DateInfo;
import com.stronans.android.agenda.model.Incident;
import com.stronans.android.agenda.support.FormattedInfo;
import com.stronans.android.agenda.support.ResourceInfo;
import com.stronans.android.agenda.support.Utilities;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;

public class AgendaListAdapter extends BaseExpandableListAdapter {
    List<AgendaItem> agendaItems;
    Context context;
    AgendaConfiguration config;
    Resources resources;

    public AgendaListAdapter(Context context, List<AgendaItem> agendaItems) {
        super();
        this.context = context;
        this.agendaItems = agendaItems;
        config = AgendaStaticData.getStaticData().getConfig();
        resources = context.getResources();
    }

    public void updateList(List<AgendaItem> items) {
        this.agendaItems = items;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return agendaItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return agendaItems.get(groupPosition).eventsOnThisDay().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return agendaItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return agendaItems.get(groupPosition).eventsOnThisDay().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final String dateFormat = "WWWW, MMMM D|{0}|";
        View view = convertView;

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.agenda_list_item, null);
        }

        AgendaItem item = agendaItems.get(groupPosition);

        String newDateFormat = MessageFormat.format(dateFormat,
                new Object[]{FormattedInfo.suffix(item.date().getDateInMonth())});
        String dateInfoTxt = item.date().format(newDateFormat);
        String intervalTxt = ResourceInfo.getIntervalString(item.date(), resources, FormatStyle.shortStyle);

        if (item.date().getYear() != DateInfo.getNow().getYear()) {
            dateInfoTxt += " " + FormattedInfo.getYearString(item.date());
        }

        Utilities.setTextView(view, R.id.IntervalInfo, intervalTxt);
        Utilities.setTextView(view, R.id.DateInfo, "(" + dateInfoTxt + ")");
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        /**
         * Show each incident in a particular day.
         */
        View view = convertView;

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.incident_list_item, null);
        }

        Incident item = agendaItems.get(groupPosition).eventsOnThisDay().get(childPosition);

        Utilities.setTextView(view, R.id.incidenttitle, item.title());

        StringBuffer sb = new StringBuffer(30);

        if (!item.isAllDay()) {
            sb.append(MessageFormat.format(resources.getString(R.string.time_period),
                    new Object[]{FormattedInfo.getTimeString(item.startAt()), FormattedInfo.getTimeString(item.endsAt())}));
        } else
            sb.append(resources.getString(R.string.all_day_event));

        Utilities.setTextView(view, R.id.incidentperiod, sb.toString());

        String location = "";
        if (Utilities.hasContent(item.eventLocation())) {
            location = resources.getString(R.string.location) + item.eventLocation();
        }

        Utilities.setTextView(view, R.id.incidentlocation, location);

        int dayOfWeek = agendaItems.get(groupPosition).date().getCurrentDayOfMonth();

        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            view.setBackgroundColor(resources.getColor(R.color.Chalk));
        } else {
            view.setBackgroundColor(resources.getColor(R.color.Ivory));
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
