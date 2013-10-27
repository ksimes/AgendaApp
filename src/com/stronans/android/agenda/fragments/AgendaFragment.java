package com.stronans.android.agenda.fragments;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ExpandableListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.stronans.android.agenda.R;
import com.stronans.android.agenda.activities.AgendaItemDisplay;
import com.stronans.android.agenda.adapters.AgendaListAdapter;
import com.stronans.android.agenda.dataaccess.AgendaData;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.interfaces.Refresher;
import com.stronans.android.agenda.model.AgendaItem;
import com.stronans.android.agenda.model.Incident;
import com.stronans.android.agenda.support.DateInfo;
import com.stronans.android.agenda.support.FormattedInfo;
import com.stronans.android.controllers.AgendaConfiguration;
import com.stronans.android.controllers.DateChangeListener;

public class AgendaFragment extends ExpandableListFragment implements Refresher, DateChangeListener
{
    AgendaConfiguration config;
    AgendaListAdapter agendaItemsAdapter;
    int agendaRange;
    ExpandableListView listView;
    OnChildClickListener onChildClickListener;
    List<AgendaItem> allAgendaItems;

    public AgendaFragment()
    {
        super();
        config = AgendaStaticData.getStaticData().getConfig();
        config.addDateListener(this);
        
        onChildClickListener = new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                Incident item = allAgendaItems.get(groupPosition).getEventsOnThisDay().get(childPosition);
                
                Intent intent = new Intent(v.getContext(), AgendaItemDisplay.class);
                
                intent.putExtra(Incident.Title, item.getTitle());
                String period = "";
                if (!item.isAllDay())
                {
                    period = MessageFormat.format(getString(R.string.time_period),
                            new Object[] { FormattedInfo.getTimeString(item.getStart()), FormattedInfo.getTimeString(item.getEnd()) });
                }
                else
                    period = getString(R.string.all_day_event);
                
                intent.putExtra(Incident.Period, period);
                
                if (item.getEventLocation() != null)
                {
                    if (item.getEventLocation().length() > 0)
                    {
                        intent.putExtra(Incident.Location, getString(R.string.location) + item.getEventLocation());
                    }
                }
                
                intent.putExtra(Incident.Description, item.getDescription());
                
                startActivity(intent);      
                return false;
            }};
    }

    private List<AgendaItem> getAgendaList()
    {
        // over what range of days to we want to show the agenda.
        agendaRange = config.getAgendaRange();

        DateInfo start = new DateInfo(new Date()); // set to today.
        start.setToJustPastMidnight();

        DateInfo end = new DateInfo(start);
        end.getCalendar().add(Calendar.DATE, agendaRange);
        end.setToMidnight();

        List<Incident> agendaEvents = AgendaData.getInst().getEvents(0, start, end);

        List<AgendaItem> allAgendaItems = new ArrayList<AgendaItem>();
        DateInfo startAgenda = new DateInfo(start);
        boolean finished = false;
        while (!finished)
        {
            List<Incident> eventsForDate = Incident.extractForDate(agendaEvents, startAgenda);

            if (eventsForDate.size() > 0)
            {
                AgendaItem item = new AgendaItem(new DateInfo(startAgenda), eventsForDate);
                allAgendaItems.add(item);
            }

            startAgenda.getCalendar().add(Calendar.DATE, 1);
            if (startAgenda.getCalendar().after(end.getCalendar()))
            {
                finished = true;
            }
        }

        return allAgendaItems;
    }

    private void checkForEventsToday(List<AgendaItem> allAgendaItems)
    {
        if (allAgendaItems.size() > 0)
        {
            AgendaItem item = allAgendaItems.get(0);
            // If the first item is not for today then we have no events for today.
            if (!item.getDate().isToday())
            {
                Incident newIncident = new Incident();
                newIncident.setTitle(getString(R.string.no_activities));
                newIncident.setAllDay(true);
                List<Incident> newEventsForDate = new ArrayList<Incident>();
                newEventsForDate.add(newIncident);
                AgendaItem newItem = new AgendaItem(new DateInfo(), newEventsForDate);
                // add this into the beginning of the list
                allAgendaItems.add(0, newItem);
            }
        }
    }

    @Override
    public void refreshDisplay()
    {
        allAgendaItems = getAgendaList();
        checkForEventsToday(allAgendaItems);

        if (agendaItemsAdapter == null)
        {
            agendaItemsAdapter = new AgendaListAdapter(AgendaData.getInst().getContext(), allAgendaItems);
            setListAdapter(agendaItemsAdapter);
        }
        else
        {
            agendaItemsAdapter.updateList(allAgendaItems);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
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
    public void onStart()
    {
        super.onStart();

        // Make sure all the groups are expanded so we can see what's
        // happening over the next x days.
        int count = agendaItemsAdapter.getGroupCount();
        for (int position = 0; position < count; position++)
        {
            listView.expandGroup(position);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.agendalayout, container, false);

        String intervalString = MessageFormat.format(getString(R.string.agendaHeaderText),
                new Object[] { agendaRange });

        TextView txtHeader = (TextView) view.findViewById(R.id.txtHeader);

        txtHeader.setText(intervalString);

        listView = (ExpandableListView) view.findViewById(android.R.id.list);
        
        listView.setOnChildClickListener(onChildClickListener);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.mainmenu, menu);
        inflater.inflate(R.menu.agendamenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Called when a menu item is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case R.id.menu_refresh:
            refreshDisplay();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* (non-Javadoc)
     * @see com.stronans.android.controllers.DateChangeListener#dateChanged(com.stronans.android.agenda.support.DateInfo)
     */
    @Override
    public void dateChanged(long newDate)
    {
        refreshDisplay();
    }
}
