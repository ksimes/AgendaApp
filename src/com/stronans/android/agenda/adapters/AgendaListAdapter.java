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
import com.stronans.android.agenda.model.*;
import com.stronans.android.agenda.support.FormattedInfo;
import com.stronans.android.agenda.support.ResourceInfo;
import com.stronans.android.agenda.support.Utilities;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;

import static android.support.v4.content.ContextCompat.getColor;
import static com.stronans.android.agenda.support.AgendaUtilities.getTaskWarningColour;

public class AgendaListAdapter extends BaseExpandableListAdapter {
    private List<AgendaItem> agendaItems;
    private Context context;
    private AgendaConfiguration config;
    private Resources resources;

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

        AgendaItem item = agendaItems.get(groupPosition);

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (item.isMessageOnly()) {
                view = vi.inflate(R.layout.agenda_message_item, parent, false);
            } else {
                view = vi.inflate(R.layout.agenda_list_item, parent, false);
            }
        }

        if (item.isMessageOnly()) {
            Utilities.setTextView(view, R.id.MessageInfo, resources.getString(R.string.agendaMessage));
        } else {
            String newDateFormat = MessageFormat.format(dateFormat, FormattedInfo.suffix(item.date().getDateInMonth()));
            String dateInfoTxt = item.date().format(newDateFormat);
            String intervalTxt = ResourceInfo.getIntervalString(item.date(), resources, FormatStyle.shortStyle);

            if (item.date().getYear() != DateInfo.getNow().getYear()) {
                dateInfoTxt += " " + DateInfo.getYearString(item.date());
            }

            Utilities.setTextView(view, R.id.IntervalInfo, intervalTxt);
            Utilities.setTextView(view, R.id.DateInfo, "(" + dateInfoTxt + ")");
        }

        return view;
    }

    /**
     * Show each happening in a particular day.
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.incident_list_item, parent, false);
        }

        AgendaItem agendaItem = agendaItems.get(groupPosition);

        Happening item = agendaItem.eventsOnThisDay().get(childPosition);

        Utilities.setTextView(view, R.id.incidentTitle, item.title());
        Utilities.textViewVisibility(view, R.id.incidentlocation, false);
        Utilities.textViewVisibility(view, R.id.incidentperiod, false);

        switch (item.classType()) {
            case Message: {
                view.setBackgroundColor(getColor(context, R.color.Ivory));
            }
            break;

            case Incident: {
                Incident incident = item.getAsIncident();
                String period;

                if (!incident.isAllDay()) {
                    period = MessageFormat.format(resources.getString(R.string.time_period),
                            DateInfo.getTimeString(incident.startAt()),
                            DateInfo.getTimeString(incident.endsAt()));

                } else
                    period = resources.getString(R.string.all_day_event);

                Utilities.setTextView(view, R.id.incidentperiod, period);
                Utilities.textViewVisibility(view, R.id.incidentperiod, true);

                String location;
                if (Utilities.hasContent(incident.eventLocation())) {
                    location = resources.getString(R.string.location) + incident.eventLocation();
                    Utilities.setTextView(view, R.id.incidentlocation, location);
                    Utilities.textViewVisibility(view, R.id.incidentlocation, true);
                }

                int dayOfWeek = agendaItems.get(groupPosition).date().getCurrentDayOfMonth();

                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                    view.setBackgroundColor(getColor(context, R.color.Chalk));
                } else {
                    view.setBackgroundColor(getColor(context, R.color.Ivory));
                }
            }
            break;

            case TaskWrapper: {
                TaskWrapper wrappedTask = item.getAsTaskWrapper();

                String taskTitle = wrappedTask.task().title();

                if (wrappedTask.status() == TaskWrapper.Status.PlannedStart) {
                    Utilities.setTextView(view, R.id.incidentTitle, resources.getString(R.string.plannedstarttext) + ": " + taskTitle);
                    view.setBackgroundColor(getColor(context, R.color.LightGreen));
                }

                if (wrappedTask.status() == TaskWrapper.Status.TargetDate) {
                    Utilities.setTextView(view, R.id.incidentTitle, resources.getString(R.string.targetcompletetext) + ": " + taskTitle);

                    view.setBackgroundColor(getColor(context, getTaskWarningColour(agendaItem.date(), config)));
                }
            }
            break;
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
