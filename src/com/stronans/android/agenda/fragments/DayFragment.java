package com.stronans.android.agenda.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.ListView;
import android.widget.TextView;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.adapters.DayListAdapter;
import com.stronans.android.agenda.dataaccess.AgendaData;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.interfaces.Refresher;
import com.stronans.android.agenda.model.AgendaConfiguration;
import com.stronans.android.agenda.model.DateInfo;
import com.stronans.android.agenda.model.Incident;
import com.stronans.android.controllers.DateChangeListener;

import java.util.Date;
import java.util.List;

public class DayFragment extends Fragment implements Refresher, DateChangeListener {
    AgendaConfiguration config;
    DayListAdapter dayItemsAdapter;
    ListView incidentsToday;
    TextView dayListEmpty;

    public DayFragment() {
        super();
        config = AgendaStaticData.getStaticData().getConfig();
        config.addDateListener(this);
        setHasOptionsMenu(true);
    }

    private List<Incident> getTodaysEvents(DateInfo selected) {
        DateInfo start = DateInfo.fromDateInfo(selected);
        start.setToJustPastMidnight();
        DateInfo end = DateInfo.fromDateInfo(selected);
        end.setToMidnight();

        return AgendaData.getInst().getEvents(0, start, end);
    }

    public void refreshDisplay() {
        DateInfo selected = config.getDateInfo();

        List<Incident> todaysEvents = getTodaysEvents(selected);

        if (todaysEvents.isEmpty()) {
            dayListEmpty.setVisibility(View.VISIBLE);
            incidentsToday.setVisibility(View.GONE);
            dayListEmpty.setText(getString(R.string.no_activities));
            dayListEmpty.invalidate();
        } else {
            dayListEmpty.setVisibility(View.GONE);
            incidentsToday.setVisibility(View.VISIBLE);

//            if (dayItemsAdapter == null)
//            {
            dayItemsAdapter = new DayListAdapter(AgendaData.getInst().getContext(), todaysEvents);
            incidentsToday.setAdapter(dayItemsAdapter);
//            }
//            else
//            {
//                dayItemsAdapter.updateList(todaysEvents);
//            }
        }
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        setMenuVisibility(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.daylayout, container, false);

        incidentsToday = (ListView) fragmentView.findViewById(R.id.DayList);
        dayListEmpty = (TextView) fragmentView.findViewById(R.id.DayListEmpty);

        refreshDisplay();

        return fragmentView;
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
        inflater.inflate(R.menu.daymenu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
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

            case R.id.menu_gototoday:
                config.setDateInfo(new Date().getTime());
                refreshDisplay();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* (non-Javadoc)
     * @see com.stronans.android.controllers.DateChangeListener#dateChanged(long)
     */
    @Override
    public void dateChanged(long newDate) {
        refreshDisplay();
    }
}
