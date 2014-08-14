package com.stronans.android.agenda.support;

import android.content.res.Resources;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.enums.FormatStyle;
import com.stronans.android.agenda.model.DateInfo;

import java.text.MessageFormat;

public class ResourceInfo {
    private ResourceInfo() {
    }

    public static String getIntervalString(DateInfo selected, Resources resources, FormatStyle style) {
        String intervalString;

        int interval = selected.intervalToToday();
        switch (interval) {
            case 0:
                intervalString = resources.getString(R.string.today);
                break;

            case 1:
                intervalString = resources.getString(R.string.yesterday);
                break;

            case -1:
                intervalString = resources.getString(R.string.tomorrow);
                break;

            default:
                if (interval > 0)
                    if (style == FormatStyle.shortStyle)
                        intervalString = MessageFormat.format(
                                resources.getString(R.string.inPastShort), new Object[]{interval});

                    else
                        intervalString = MessageFormat.format(resources.getString(R.string.inPast), new Object[]{interval});
                else if (style == FormatStyle.shortStyle)
                    intervalString = MessageFormat.format(
                            resources.getString(R.string.inFutureShort),
                            new Object[]{Math.abs(interval)});
                else
                    intervalString = MessageFormat.format(resources.getString(R.string.inFuture),
                            new Object[]{Math.abs(interval)});
                break;
        }

        return intervalString;
    }
}
