package com.stronans.android.agenda.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ExpandableListFragment;
import android.view.*;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.activities.AgendaItemDisplay;
import com.stronans.android.agenda.adapters.AgendaListAdapter;
import com.stronans.android.agenda.dataaccess.AgendaData;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.interfaces.Refresher;
import com.stronans.android.agenda.model.AgendaConfiguration;
import com.stronans.android.agenda.model.AgendaItem;
import com.stronans.android.agenda.model.DateInfo;
import com.stronans.android.agenda.model.Incident;
import com.stronans.android.controllers.DateChangeListener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class AgendaFragment extends ExpandableListFragment implements Refresher, DateChangeListener {
    AgendaConfiguration config;
    AgendaListAdapter agendaItemsAdapter;
    int agendaRange;
    ExpandableListView listView;
    OnChildClickListener onChildClickListener;
    List<AgendaItem> allAgendaItems;

    public AgendaFragment() {
        super();
        config = AgendaStaticData.getStaticData().getConfig();
        config.addDateListener(this);

        onChildClickListener = new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Incident item = allAgendaItems.get(groupPosition).eventsOnThisDay().get(childPosition);

                Intent intent = new Intent(v.getContext(), AgendaItemDisplay.class);

                intent.putExtra(Incident.Title, item.title());
                String period = "";
                if (!item.isAllDay()) {
                    period = MessageFormat.format(
                            getString(R.string.time_period),
                            DateInfo.getTimeString(item.startAt()),
                            DateInfo.getTimeString(item.endsAt()));
                } else
                    period = getString(R.string.all_day_event);

                intent.putExtra(Incident.Period, period);

                if (item.eventLocation() != null) {
                    if (item.eventLocation().length() > 0) {
                        intent.putExtra(Incident.Location, getString(R.string.location) + item.eventLocation());
                    }
                }

                intent.putExtra(Incident.Description, item.description());

                startActivity(intent);
                return false;
            }
        };
    }

    private List<AgendaItem> getAgendaList() {
        // over what range of days to we want to show the agenda.
        agendaRange = config.getAgendaRange();

        DateInfo start = DateInfo.getNow(); // set to today.
        start.setToJustPastMidnight();

        DateInfo end = DateInfo.fromDateInfo(start);
        end.addToDate(agendaRange);
        end.setToMidnight();

        List<Incident> agendaEvents = AgendaData.getInst().getEvents(0, start, end);

        List<AgendaItem> allAgendaItems = new ArrayList<AgendaItem>();
        DateInfo startAgenda = DateInfo.fromDateInfo(start);
        boolean finished = false;
        while (!finished) {
            List<Incident> eventsForDate = Incident.extractForDate(agendaEvents, startAgenda);

            if (eventsForDate.size() > 0) {
                AgendaItem item = new AgendaItem(DateInfo.fromDateInfo(startAgenda), eventsForDate);
                allAgendaItems.add(item);
            }

            startAgenda.addToDate(1);
            if (startAgenda.after(end)) {
                finished = true;
            }
        }

        return allAgendaItems;
    }

    private void checkForEventsToday(List<AgendaItem> allAgendaItems) {

        if (allAgendaItems.size() == 0) {
            String titleString = MessageFormat.format(getString(R.string.nothing_happening), agendaRange);

            Incident newIncident = new Incident(titleString);
            List<Incident> newEventsForDate = new ArrayList<Incident>();
            newEventsForDate.add(newIncident);
            AgendaItem newItem = new AgendaItem(DateInfo.getNow(), newEventsForDate, true);
            // add this to the list of Agenda entries.
            allAgendaItems.add(newItem);
        } else {
            AgendaItem item = allAgendaItems.get(0);
            // If the first item is not for today then we have no events for today.
            if (!item.date().isToday()) {
                Incident newIncident = new Incident(getString(R.string.no_activities), null, null,
                        DateInfo.getUndefined(), DateInfo.getUndefined(), true, -1, null,
                        Color.BLACK, 1);
                List<Incident> newEventsForDate = new ArrayList<Incident>();
                newEventsForDate.add(newIncident);
                AgendaItem newItem = new AgendaItem(DateInfo.getNow(), newEventsForDate);
                // add this into the beginning of the list
                allAgendaItems.add(0, newItem);
            }
        }
    }

    @Override
    public void refreshDisplay() {
        allAgendaItems = getAgendaList();
        checkForEventsToday(allAgendaItems);

        if (agendaItemsAdapter == null) {
            agendaItemsAdapter = new AgendaListAdapter(AgendaData.getInst().getContext(), allAgendaItems);
            setListAdapter(agendaItemsAdapter);
        } else {
            agendaItemsAdapter.updateList(allAgendaItems);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        refreshDisplay();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onStart()
     */
    @Override
    public void onStart() {
        super.onStart();

        // Make sure all the groups are expanded so we can see what's
        // happening over the next x days.
        int count = agendaItemsAdapter.getGroupCount();
        for (int position = 0; position < count; position++) {
            listView.expandGroup(position);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.agendalayout, container, false);

        String intervalString = MessageFormat.format(getString(R.string.agendaHeaderText),
                agendaRange);

        TextView txtHeader = (TextView) view.findViewById(R.id.txtHeader);

        txtHeader.setText(intervalString);

        listView = (ExpandableListView) view.findViewById(android.R.id.list);

        listView.setOnChildClickListener(onChildClickListener);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.mainmenu, menu);
        inflater.inflate(R.menu.agendamenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Called when a menu item is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                refreshDisplay();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.stronans.android.controllers.DateChangeListener#dateChanged(com.stronans.android.agenda.support.DateInfo)
     */
    @Override
    public void dateChanged(long newDate) {
        refreshDisplay();
    }
}
