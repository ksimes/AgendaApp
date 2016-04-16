package com.stronans.android.agenda.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.model.AgendaConfiguration;
import com.stronans.android.agenda.model.DateInfo;
import com.stronans.android.agenda.model.Incident;
import com.stronans.android.agenda.support.Utilities;

import java.text.MessageFormat;
import java.util.List;

public class IncidentListAdapter extends BaseAdapter {
    private List<Incident> items;
    private Context context;
    private AgendaConfiguration config;
    private Resources resources;

    public IncidentListAdapter(Context context, List<Incident> items) {
        super();
        this.context = context;
        this.items = items;
        config = AgendaStaticData.getStaticData().getConfig();
        resources = context.getResources();
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
            view = vi.inflate(R.layout.incident_list_item, parent, false);
        }

        Incident item = items.get(position);

        Utilities.setTextView(view, R.id.incidentTitle, item.title());

        StringBuilder sb = new StringBuilder(30);

        if (!item.isAllDay()) {
            sb.append(MessageFormat.format(resources.getString(R.string.time_period),
                    new Object[]{DateInfo.getTimeString(item.startAt()), DateInfo.getTimeString(item.endsAt())}));
        } else
            sb.append(resources.getString(R.string.all_day_event));

        Utilities.setTextView(view, R.id.incidentperiod, sb.toString());

        if (Utilities.hasContent(item.eventLocation())) {
            String location = "";
            location = resources.getString(R.string.location) + item.eventLocation();
            Utilities.setTextView(view, R.id.incidentlocation, location);
            Utilities.textViewVisibility(view, R.id.incidentlocation, true);
        } else {
            Utilities.textViewVisibility(view, R.id.incidentlocation, false);
        }

        return view;
    }
}
