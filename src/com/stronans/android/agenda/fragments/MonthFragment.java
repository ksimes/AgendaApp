package com.stronans.android.agenda.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.stronans.android.agenda.R;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.interfaces.Refresher;
import com.stronans.android.agenda.support.DateInfo;
import com.stronans.android.agenda.views.DateHeaderView;
import com.stronans.android.agenda.views.MonthGridView;
import com.stronans.android.controllers.AgendaConfiguration;
import com.stronans.android.controllers.DateChangeListener;

/**
 */
public class MonthFragment extends Fragment implements Refresher, DateChangeListener
{
    AgendaConfiguration config;
    DateHeaderView dateHeader;
    MonthGridView monthGrid;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        config = AgendaStaticData.getStaticData().getConfig();
        config.addDateListener(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        // Inflate our UI from its XML layout description.
        View view = inflater.inflate(R.layout.monthlayout, container, false);

        dateHeader = (DateHeaderView)view.findViewById(R.id.monthDateHeader);
        
        monthGrid = (MonthGridView)view.findViewById(R.id.monthView);
        monthGrid.addRefreshNotifier(this);
        
        return view;
    }

    /* (non-Javadoc)
     * @see com.stronans.android.agenda.fragments.Refresher#refreshDisplay()
     */
    @Override
    public void refreshDisplay()
    {
        // Date has probably been changed in the config so reset it in the views.
        dateHeader.setNewDate(config.getDateInfo());
        monthGrid.invalidate();
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
        inflater.inflate(R.menu.monthmenu, menu);
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

        case R.id.menu_gototoday:
            config.setDateInfo(new DateInfo());
            refreshDisplay();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* (non-Javadoc)
     * @see com.stronans.android.controllers.DateChangeListener#dateChanged(long)
     */
    @Override
    public void dateChanged(long newDate)
    {
        refreshDisplay();
    }
}
