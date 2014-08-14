package com.stronans.android.agenda.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.stronans.android.agenda.R;
import com.stronans.android.agenda.enums.LabelOrientation;
import com.stronans.android.agenda.enums.ViewType;
import com.stronans.android.controllers.DateChangeListener;

public class AgendaConfiguration
{
    DateInfo                 date              = DateInfo.getNow();
    List<DateChangeListener> dateListeners     = new ArrayList<DateChangeListener>();
    /**
     * It will be possible to customise the day the week starts on
     */
    int                      weekStart         = Calendar.MONDAY;
    LabelOrientation         headerOrientation = LabelOrientation.left;
    String[]                 weekDaysShort     = null;
    String[]                 monthNamesLong    = null;
    Bitmap                   tokens            = null;
    Bitmap                   window            = null;
    /**
     * By default the agenda page shows the next ten days, this will be customisable.
     */
    int                      agendaRange       = 10;
    /**
     * By default the start tab should be agenda, this will be customisable.
     */
    ViewType                 initialViewType   = ViewType.Agenda;

    public void setResources(Resources resources)
    {
        String temp = resources.getText(R.string.shortweekdays).toString();
        weekDaysShort = new String(temp).split(",");

        temp = resources.getText(R.string.longmonthnames).toString();
        monthNamesLong = new String(temp).split(",");

        tokens = BitmapFactory.decodeResource(resources, R.drawable.agenda);
        window = BitmapFactory.decodeResource(resources, R.drawable.ic_menu_info_details); // windows
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
        for (DateChangeListener listener : dateListeners)
        {
            listener.dateChanged(date.getMilliseconds());
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
        this.date = DateInfo.fromLong(selected);
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
        int tokenSize = tokens.getHeight(); // All tokens are square
        Bitmap step1 = Bitmap.createBitmap(tokens, index * tokenSize, 0, tokenSize, tokenSize);
        Bitmap result = Bitmap.createScaledBitmap(step1, width, height, false);
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
     * @param agendaRange
     *            the agendaRange to set
     */
    public void setAgendaRange(int agendaRange)
    {
        this.agendaRange = agendaRange;
    }

    /**
     * @return initialViewType
     */
    public ViewType initalViewType()
    {
        return initialViewType;
    }
}
