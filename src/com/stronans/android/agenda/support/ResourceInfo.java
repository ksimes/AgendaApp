package com.stronans.android.agenda.support;

import java.text.MessageFormat;

import android.content.res.Resources;

import com.stronans.android.agenda.R;

public class ResourceInfo
{

    public static String getIntervalString(DateInfo selected, Resources resources)
    {
        String intervalString;
        
        int interval = selected.intervalToToday();
        switch(interval)
        {
            case 0 : 
                intervalString = resources.getString(R.string.today);
                break;
                
            case 1 : 
                intervalString = resources.getString(R.string.yesterday);
                break;
     
            case -1 : 
                intervalString = resources.getString(R.string.tomorrow);
                break;
            
            default:
                if(interval > 0)
                    intervalString = MessageFormat.format(resources.getString(R.string.inPast), 
                                new Object[] { interval });
                else
                    intervalString = MessageFormat.format(resources.getString(R.string.inFuture),
                                new Object[] { Math.abs(interval) });
                break;
        }
        
        return intervalString;
    }
}
