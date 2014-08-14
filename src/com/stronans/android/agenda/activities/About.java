package com.stronans.android.agenda.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ListView;
import android.widget.TextView;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.adapters.AboutListAdapter;
import com.stronans.android.agenda.dataaccess.AgendaData;

/**
 * Generates the About sub-dialog content and display.
 *
 * @author SimonKing
 */
public class About extends Activity {

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set and inflate our UI from its XML layout description.
        // Most fields in the layout are populated from resource strings.
        // See strings.xml
        setContentView(R.layout.aboutlayout);

        // Increase the size and use the BlackAdder ITC (OpenType) font for the Title field.
        TextView titleDisplay = (TextView) findViewById(R.id.TitleDisplay);
        Typeface font = Typeface.createFromAsset(getAssets(), "ITCBLKAD.TTF");
        titleDisplay.setTypeface(font);
        titleDisplay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 60);

        ListView lv = (ListView) findViewById(R.id.listView);

        lv.setAdapter(new AboutListAdapter(AgendaData.getInst().getContext()));
    }
}
