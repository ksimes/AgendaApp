package com.stronans.android.agenda.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ExpandableListFragment;
import android.view.*;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.activities.AgendaItemDisplay;
import com.stronans.android.agenda.adapters.AgendaListAdapter;
import com.stronans.android.agenda.dataaccess.AgendaData;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.interfaces.Refresher;
import com.stronans.android.agenda.model.AgendaConfiguration;
import com.stronans.android.agenda.model.AgendaItem;
import com.stronans.android.agenda.model.DateInfo;
import com.stronans.android.agenda.model.Happening;
import com.stronans.android.agenda.model.Incident;
import com.stronans.android.agenda.model.Message;
import com.stronans.android.agenda.support.Utilities;
import com.stronans.android.controllers.DateChangeListener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static com.stronans.android.agenda.support.AgendaUtilities.*;

public class AgendaFragment extends ExpandableListFragment implements Refresher, DateChangeListener {
    private AgendaConfiguration config;
    private AgendaListAdapter agendaItemsAdapter;
    private ExpandableListView listView;
    private OnChildClickListener onChildClickListener;
    private List<AgendaItem> allAgendaItems;

    public AgendaFragment() {
        super();
        config = AgendaStaticData.getStaticData().getConfig();
        config.addDateListener(this);
        setHasOptionsMenu(true);

        onChildClickListener = new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Happening item = allAgendaItems.get(groupPosition).eventsOnThisDay().get(childPosition);

                Intent intent = new Intent(v.getContext(), AgendaItemDisplay.class);

                intent.putExtra(Incident.Title, item.title());

                if (item.classType() == Happening.ClassType.Incident) {
                    Incident incident = item.getAsIncident();
                    String period;

                    if (!incident.isAllDay()) {
                        period = MessageFormat.format(
                                getString(R.string.time_period),
                                DateInfo.getTimeString(incident.startAt()),
                                DateInfo.getTimeString(incident.endsAt()));
                    } else
                        period = getString(R.string.all_day_event);

                    intent.putExtra(Incident.Period, period);

                    if (incident.eventLocation() != null) {
                        if (incident.eventLocation().length() > 0) {
                            intent.putExtra(Incident.Location, getString(R.string.location) + incident.eventLocation());
                        }
                    }
                }

                intent.putExtra(Incident.Description, item.description());

                startActivity(intent);
                return false;
            }
        };
    }

    @Override
    public void refreshDisplay() {
        allAgendaItems = getAgendaList(config.getAgendaRange(), 0);
        checkIfAnyEventsToday(allAgendaItems);

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
                config.getAgendaRange());
        Utilities.setTextView(view, R.id.txtHeader, intervalString);
        listView = (ExpandableListView) view.findViewById(android.R.id.list);
        listView.setOnChildClickListener(onChildClickListener);
        return view;
    }

    /**
     * Check to see if there are no events today and add a message regarding that.
     * @param allAgendaItems list of Agenda items and associated Events/tasks.
     */
    private void checkIfAnyEventsToday(List<AgendaItem> allAgendaItems) {

        if (allAgendaItems.size() == 0) {
            String titleString = MessageFormat.format(getString(R.string.nothing_happening), config.getAgendaRange());

            Happening newIncident = new Message(titleString, "");
            List<Happening> newEventsForDate = new ArrayList<>();
            newEventsForDate.add(newIncident);
            AgendaItem newItem = new AgendaItem(DateInfo.getNow(), newEventsForDate, true);
            // add this to the list of Agenda entries.
            allAgendaItems.add(newItem);
        } else {
            AgendaItem item = allAgendaItems.get(0);
            // If the first item is not for today then we have no events for today.
            if (!item.date().isToday()) {
                Message message = new Message(getString(R.string.no_activities), "");

                List<Happening> newEventsForDate = new ArrayList<>();
                newEventsForDate.add(message);
                AgendaItem newItem = new AgendaItem(DateInfo.getNow(), newEventsForDate);
                // add this into the beginning of the list
                allAgendaItems.add(0, newItem);
            }
        }
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
