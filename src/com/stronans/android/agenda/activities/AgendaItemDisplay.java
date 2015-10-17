package com.stronans.android.agenda.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.stronans.android.agenda.R;
import com.stronans.android.agenda.model.Incident;

/**
 * @author SimonKing
 * 
 */
public class AgendaItemDisplay extends Activity
{

    private void populateView(Bundle parameters, int resourceID, String parameterID)
    {
        TextView field = (TextView) findViewById(resourceID);
        String string = parameters.getString(parameterID);

        if (string != null)
        {
            field.setText(string);
        }
    }
    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Inflate our UI from its XML layout description.
        setContentView(R.layout.agenda_popup);
        
        // Incident details are passed as strings from the calling Activity.
        Bundle parameters = getIntent().getExtras();
        if (parameters != null)
        {
            populateView(parameters, R.id.incidentTitle, Incident.Title);
            populateView(parameters, R.id.incidentperiod, Incident.Period);
            populateView(parameters, R.id.incidentlocation, Incident.Location);
            populateView(parameters, R.id.incidentdescription, Incident.Description);
        }
    }
}
