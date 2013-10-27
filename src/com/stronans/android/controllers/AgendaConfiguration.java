package com.stronans.android.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.stronans.android.agenda.R;
import com.stronans.android.agenda.enums.LabelOrientation;
import com.stronans.android.agenda.support.DateInfo;

public class AgendaConfiguration
{
    DateInfo date = new DateInfo();
    List<DateChangeListener> dateListeners = new ArrayList<DateChangeListener>();
    /**
     * It should be possible to customise the day the week starts on
     */
    int weekStart = Calendar.MONDAY;
    LabelOrientation headerOrientation = LabelOrientation.left;
    String[] weekDaysShort = null;
    String[] monthNamesLong = null;
    Bitmap tokens = null;
    Bitmap window = null;
    /**
     * By default the agenda page shows the next ten days, this should be customisable.
     */
    int agendaRange = 10;

    public void setResources(Resources resources)
    {
        String temp = resources.getText(R.string.shortweekdays).toString();
        weekDaysShort = new String(temp).split(",");

        temp = resources.getText(R.string.longmonthnames).toString();
        monthNamesLong = new String(temp).split(",");

        tokens = BitmapFactory.decodeResource(resources, R.drawable.agenda);
        window = BitmapFactory.decodeResource(resources, R.drawable.ic_menu_info_details);      // windows
    }
    
    public void addDateListener(DateChangeListener listener)
    {
        dateListeners.add(listener);
    }
    
    public void removeDateListener(DateChangeListener listener)
    {
        dateListeners.remove(listener);
    }
    
    public String[] getShortWeekDayNames()
    {
        return weekDaysShort;
    }
    
    private void fireDateListeners()
    {
        for(DateChangeListener listener:dateListeners)
        {
            listener.dateChanged(date.getDate().getTime());
        }
    }
    
    public DateInfo getDateInfo()
    {
        return date;
    }
    
    public void setDateInfo(DateInfo selected)
    {
        this.date = selected;
        fireDateListeners();
    }
    
    public void setDateInfo(long selected)
    {
        this.date = new DateInfo(selected);
    }
    
    public String[] getLongMonthNames()
    {
        return monthNamesLong;
    }
    
    public Bitmap getTokens()
    {
        return tokens;
    }
    
    public Bitmap getToken(int index)
    {
        int tokenSize = tokens.getHeight(); // All tokens are square
        Bitmap result = Bitmap.createBitmap(tokens, index * tokenSize, 0, tokenSize, tokenSize); 
        return result;
    }
    
    public Bitmap getScaledToken(int index, int width, int height)
    {
        Bitmap result = Bitmap.createScaledBitmap(window, width, height, false);
        return result;
    }
    
    public int getTokenWidth()
    {
        return tokens.getHeight();
    }
    
    public int getWeekStart()
    {
        return weekStart;
    }
    
    public void setWeekStart(int weekStart)
    {
        this.weekStart = weekStart;
    }
    
    public LabelOrientation getHeaderOrientation()
    {
        return headerOrientation;
    }
    
    public void setHeaderOrientation(LabelOrientation orientation)
    {
        headerOrientation = orientation;
    }

    /**
     * @return the agendaRange
     */
    public int getAgendaRange()
    {
        return agendaRange;
    }

    /**
     * @param agendaRange the agendaRange to set
     */
    public void setAgendaRange(int agendaRange)
    {
        this.agendaRange = agendaRange;
    }
}
