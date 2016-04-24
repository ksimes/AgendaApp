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
    private DateInfo                 date              = DateInfo.getNow();
    private List<DateChangeListener> dateListeners     = new ArrayList<>();
    private String[]                 weekDaysShort     = null;
    private String[]                 monthNamesLong    = null;
    private Bitmap                   tokens            = null;
    private Bitmap                   window            = null;
    // customisable settings.
    /*
     * It will be possible to customise the day the week starts on
     */
    private int                      weekStart         = Calendar.MONDAY;
    private LabelOrientation         headerOrientation = LabelOrientation.left;
    /**
     * By default the agenda page shows the next ten days
     */
    private int                      agendaRange       = 10;
    /**
     * By default the start tab will be agenda.
     */
    private ViewType                 initialViewType   = ViewType.Agenda;
    /**
     * By default the task first level warning will be 5.
     */
    private int                      TaskWarningFirst = 5;
    private int                      TaskWarningFirstColour = R.color.Amber;
    /**
     * By default the task second and final level warning will be 3.
     */
    private int                      TaskWarningFinal = 3;
    private int                      TaskWarningFinalColour = R.color.SoftPink;

    private int                      defaultTaskColour = R.color.AvocadoGreen;

    public void setResources(Resources resources)
    {
        String temp = resources.getText(R.string.shortweekdays).toString();
        weekDaysShort = temp.split(",");

        temp = resources.getText(R.string.longmonthnames).toString();
        monthNamesLong = temp.split(",");

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

    public int getTaskWarningFirstColour() {
        return TaskWarningFirstColour;
    }

    public void setTaskWarningFirstColour(int taskWarningFirstColour) {
        TaskWarningFirstColour = taskWarningFirstColour;
    }

    public int getTaskWarningFirst() {
        return TaskWarningFirst;
    }

    public void setTaskWarningFirst(int taskWarningFirst) {
        TaskWarningFirst = taskWarningFirst;
    }

    public int getTaskWarningFinal() {
        return TaskWarningFinal;
    }

    public void setTaskWarningFinal(int taskWarningFinal) {
        TaskWarningFinal = taskWarningFinal;
    }

    public int getTaskWarningFinalColour() {
        return TaskWarningFinalColour;
    }

    public void setTaskWarningFinalColour(int taskWarningFinalColour) {
        TaskWarningFinalColour = taskWarningFinalColour;
    }

    public int getDefaultTaskColour() {
        return defaultTaskColour;
    }

    public void setDefaultTaskColour(int defaultTaskColour) {
        this.defaultTaskColour = defaultTaskColour;
    }

    public ViewType getInitialViewType() {
        return initialViewType;
    }

    public void setInitialViewType(ViewType initialViewType) {
        this.initialViewType = initialViewType;
    }
}
