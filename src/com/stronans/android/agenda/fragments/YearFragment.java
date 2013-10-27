package com.stronans.android.agenda.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stronans.android.agenda.R;
import com.stronans.android.agenda.dataaccess.AgendaData;
import com.stronans.android.agenda.interfaces.Refresher;

public class YearFragment extends Fragment implements Refresher
{
    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        // Inflate our UI from its XML layout description.
        return inflater.inflate(R.layout.yearlayout, container, false);
    }

    /* (non-Javadoc)
     * @see com.stronans.android.agenda.fragments.Refresher#refreshDisplay()
     */
    @Override
    public void refreshDisplay()
    {
        // TODO Auto-generated method stub
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.yearmenu, menu);
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
            Toast.makeText(AgendaData.getInst().getContext(), "Refresh", Toast.LENGTH_SHORT).show();
            refreshDisplay();
            return true;

        case R.id.menu_gototoday:
            Toast.makeText(AgendaData.getInst().getContext(), "Go To Today", Toast.LENGTH_SHORT).show();
            refreshDisplay();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
