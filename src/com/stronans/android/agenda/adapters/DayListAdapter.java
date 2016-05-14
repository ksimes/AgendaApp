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
import com.stronans.android.agenda.model.Happening;
import com.stronans.android.agenda.model.Incident;
import com.stronans.android.agenda.support.Utilities;

import java.util.List;

import static com.stronans.android.agenda.support.AgendaUtilities.getPeriodData;

public class DayListAdapter extends BaseAdapter {
    private List<Happening> items;
    private Context context;
    private AgendaConfiguration config;
    private Resources resources;

    public DayListAdapter(Context context, List<Happening> items) {
        super();
        this.context = context;
        this.items = items;
        config = AgendaStaticData.getStaticData().getConfig();
        resources = context.getResources();
    }

    public void updateList(List<Happening> items) {
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
            view = vi.inflate(R.layout.daylistitem, parent, false);
        }

        Happening happening = items.get(position);
        Utilities.setTextView(view, R.id.incidentTitle, happening.title());

        switch(happening.classType())
        {
            case Incident:
                Incident item = happening.getAsIncident();

                Utilities.setTextView(view, R.id.incidentperiod, getPeriodData(item, resources));

                TextView location = (TextView) view.findViewById(R.id.dayIncidentlocation);
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
                break;

            case TaskWrapper:
                break;
        }

        return view;
    }
}
