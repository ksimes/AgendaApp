/**
 *
 */
package com.stronans.android.agenda.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.enums.FormatStyle;
import com.stronans.android.agenda.model.DateInfo;
import com.stronans.android.agenda.support.FormattedInfo;
import com.stronans.android.agenda.support.ResourceInfo;

import java.text.MessageFormat;

/**
 * @author SimonKing
 */
public class DateHeaderDisplay extends Activity {
    DateInfo selectedDate;

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate our UI from its XML layout description.
        setContentView(R.layout.dateheader_popup);

        // Incident details are passed as strings from the calling Activity.
        Bundle parameters = getIntent().getExtras();
        if (parameters != null) {
            selectedDate = DateInfo.fromLong(parameters.getLong("DateInfo"));

            String viewString = FormattedInfo.getDateString(selectedDate);
            TextView field = (TextView) findViewById(R.id.HeaderDate);
            field.setText(viewString);

            viewString = ResourceInfo.getIntervalString(selectedDate, getResources(), FormatStyle.longStyle);
            field = (TextView) findViewById(R.id.Pivot);
            field.setText(viewString);

            int dayOfYear = DateInfo.getDayOfYear(selectedDate);
            viewString = MessageFormat.format(getResources().getString(R.string.DayOfYear),
                    new Object[]{dayOfYear, FormattedInfo.suffix(dayOfYear)});
            field = (TextView) findViewById(R.id.DayOfYearView);
            field.setText(viewString);

            viewString = MessageFormat.format(getResources().getString(R.string.WeekOfYear),
                    new Object[]{DateInfo.getWeekOfYear(selectedDate)});
            field = (TextView) findViewById(R.id.WeekOfYearView);
            field.setText(viewString + " " + FormattedInfo.getYearString(selectedDate));
        }
    }

}
