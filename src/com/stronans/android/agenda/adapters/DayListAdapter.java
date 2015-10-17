package com.stronans.android.agenda.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.model.AgendaConfiguration;
import com.stronans.android.agenda.model.DateInfo;
import com.stronans.android.agenda.model.Incident;
import com.stronans.android.agenda.support.FormattedInfo;
import com.stronans.android.agenda.support.Utilities;

import java.text.MessageFormat;
import java.util.List;

public class DayListAdapter extends BaseAdapter {
    List<Incident> items;
    Context context;
    AgendaConfiguration config;
    Resources resources;

    public DayListAdapter(Context context, List<Incident> items) {
        super();
        this.context = context;
        this.items = items;
        config = AgendaStaticData.getStaticData().getConfig();
        resources = context.getResources();
    }

    public void updateList(List<Incident> items) {
        this.items = items;
        notifyDataSetChanged();
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
        View view = convertView;

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.daylistitem, null);
        }

        Incident item = items.get(position);

        Utilities.setTextView(view, R.id.incidentTitle, item.title());

        String periodTxt = new String();
        if (!item.isAllDay()) {
            periodTxt = MessageFormat.format(resources.getString(R.string.time_period),
                    new Object[]{DateInfo.getTimeString(item.startAt()), DateInfo.getTimeString(item.endsAt())});
        } else
            periodTxt = resources.getString(R.string.all_day_event);

        Utilities.setTextView(view, R.id.incidentperiod, periodTxt);

        TextView location = (TextView) view.findViewById(R.id.incidentlocation);
        if (Utilities.hasContent(item.eventLocation())) {
            location.setVisibility(View.VISIBLE);
            location.setText(resources.getString(R.string.location) + item.eventLocation());
        } else
            location.setVisibility(View.GONE);

        TextView description = (TextView) view.findViewById(R.id.incidentdescription);
        if (Utilities.hasContent(item.description())) {
            description.setVisibility(View.VISIBLE);
            description.setText(item.description());
        } else
            description.setVisibility(View.GONE);

        return view;
    }
}
