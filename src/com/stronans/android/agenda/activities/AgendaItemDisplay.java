package com.stronans.android.agenda.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.model.Incident;

/**
 * @author SimonKing
 */
public class AgendaItemDisplay extends Activity {

    private void populateView(Bundle parameters, int resourceID, String parameterID, boolean show) {
        TextView field = (TextView) findViewById(resourceID);
        String string = parameters.getString(parameterID);

        if (string != null) {
            field.setText(string);
            if (show) {
                field.setVisibility(View.VISIBLE);
            } else {
                field.setVisibility(View.GONE);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate our UI from its XML layout description.
        setContentView(R.layout.agenda_popup);

        // Incident details are passed as strings from the calling Activity.
        Bundle parameters = getIntent().getExtras();
        if (parameters != null) {
            boolean status = false;
            populateView(parameters, R.id.agendaIncidentTitle, Incident.Title, true);
            populateView(parameters, R.id.agendaIncidentperiod, Incident.Period, true);
            if (!Incident.Location.isEmpty()) {
                status = true;
            }
            populateView(parameters, R.id.agendaIncidentlocation, Incident.Location, status);
            if (!Incident.Description.isEmpty()) {
                status = true;
            } else {
                status = false;
            }
            populateView(parameters, R.id.agendaIncidentdescription, Incident.Description, status);
        }
    }
}
