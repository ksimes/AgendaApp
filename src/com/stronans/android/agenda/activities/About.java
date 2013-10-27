package com.stronans.android.agenda.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

import com.stronans.android.agenda.R;

public class About extends Activity
{

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Set and inflate our UI from its XML layout description.
        // All fields in the layout are populated from resource strings.
        // See strings.xml
        setContentView(R.layout.aboutlayout);

        // Increase the size and use the BlackAdder ITC (OpenType) font for the Title field. 
        TextView titleDisplay = (TextView) findViewById(R.id.TitleDisplay);
        Typeface font = Typeface.createFromAsset(getAssets(), "ITCBLKAD.TTF");
        titleDisplay.setTypeface(font);
        titleDisplay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 60);
    }
}
