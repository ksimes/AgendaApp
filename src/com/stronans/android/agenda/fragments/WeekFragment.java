package com.stronans.android.agenda.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.dataaccess.AgendaData;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.interfaces.Refresher;
import com.stronans.android.agenda.interfaces.WeekViewParent;
import com.stronans.android.agenda.model.AgendaConfiguration;
import com.stronans.android.agenda.model.DateInfo;
import com.stronans.android.agenda.model.Happening;
import com.stronans.android.agenda.model.Incident;
import com.stronans.android.agenda.views.DateHeaderView;
import com.stronans.android.agenda.views.WeekDayView;
import com.stronans.android.controllers.DateChangeListener;

import java.util.List;

import static com.stronans.android.agenda.support.AgendaUtilities.extractForDate;

public class WeekFragment extends Fragment implements WeekViewParent, Refresher, DateChangeListener {
    AgendaConfiguration config;
    DateInfo selected;
    int Week[] = {R.id.weekDay1, R.id.weekDay2, R.id.weekDay3, R.id.weekDay4, R.id.weekDay5, R.id.weekDay6,
            R.id.weekDay7};
    WeekDayView lastview;
    View fragmentView;
    DateHeaderView dateHeaderview;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        config = AgendaStaticData.getStaticData().getConfig();
        config.addDateListener(this);
        setHasOptionsMenu(true);
    }

    private List<Happening> getWeeksEvents(DateInfo weekDay) {
        DateInfo weekEndsOn = DateInfo.fromDateInfo(weekDay);
        weekEndsOn.addToDate(7);
        weekEndsOn.setToMidnight();

        return AgendaData.getInst().getEvents(0, weekDay, weekEndsOn);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate our UI from its XML layout description.
        fragmentView = inflater.inflate(R.layout.weeklayout, container, false);
        dateHeaderview = (DateHeaderView) fragmentView.findViewById(R.id.dateHeader);
        refreshDisplay();
        return fragmentView;
    }

    private void processDisplay(DateInfo selected) {
        int weekstart = config.getWeekStart(); // User defined start day of the week, i.e. Sunday, Saturday, Monday, etc.
        int weekBegins = ((selected.getCurrentDayOfMonth() + 7 - weekstart) % 7);

        DateInfo weekDay = DateInfo.fromDateInfo(selected);
        weekDay.addToDate(-weekBegins);
        weekDay.setToJustPastMidnight();

        List<Happening> events = getWeeksEvents(weekDay);

        for (int i = 0; i < 7; i++, weekstart++) {
            if (weekstart == 8)
                weekstart = 1;

            List<Happening> todaysEvents = extractForDate(events, weekDay);

            WeekDayView dayView = (WeekDayView) fragmentView.findViewById(Week[i]);

            dayView.setParent(this);

            dayView.setDaysEvents(todaysEvents);
            dayView.setSelectedDate(weekDay);
            dayView.setCurrentDay(weekstart - 1);

            weekDay.addToDate(1);
        }
    }

    @Override
    public WeekDayView getlastDayView() {
        return lastview;
    }

    @Override
    public void setlastDayView(WeekDayView view) {
        lastview = view;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.stronans.android.agenda.fragments.Refresher#refreshDisplay()
     */
    @Override
    public void refreshDisplay() {
        selected = config.getDateInfo();
        dateHeaderview.setNewDate(selected);
        processDisplay(selected);
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
        inflater.inflate(R.menu.weekmenu, menu);
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

            case R.id.menu_gototoday:
                config.setDateInfo(DateInfo.getNow());
                refreshDisplay();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.stronans.android.controllers.DateChangeListener#dateChanged(long)
     */
    @Override
    public void dateChanged(long newDate) {
        refreshDisplay();
    }
}
