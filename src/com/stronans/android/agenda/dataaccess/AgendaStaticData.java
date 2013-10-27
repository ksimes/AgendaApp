package com.stronans.android.agenda.dataaccess;

import java.util.Calendar;
import java.util.Date;

import android.graphics.Color;

import com.stronans.android.agenda.support.DateInfo;
import com.stronans.android.controllers.AgendaConfiguration;

/**
 * Provides static data held within the program
 * 
 * @author SimonKing
 *
 */
public class AgendaStaticData
{
    static AgendaStaticData agendaData = null;
    static AgendaConfiguration agendaConf = null;

    int[][] monthRange = { 
            { Color.WHITE, Color.BLUE },        // January
            { Color.BLUE, Color.CYAN },        // February
            { Color.WHITE, Color.CYAN },        // March
            { Color.WHITE, Color.YELLOW },        // April
            { Color.WHITE, Color.LTGRAY },        // May
            { Color.GREEN, Color.WHITE },        // June
            { Color.GREEN, Color.RED },        // July
            { Color.RED, Color.CYAN },        // August
            { Color.WHITE, Color.RED },        // September
            { Color.WHITE, Color.CYAN },        // October
            { Color.WHITE, Color.CYAN },        // November
            { Color.CYAN, Color.WHITE }};       // December
    
    private AgendaStaticData()
    {
        // Initial core data (load or setup)
        
        agendaConf = new AgendaConfiguration();
        DateInfo di = new DateInfo();
        di.setSelected(new Date());
        agendaConf.setDateInfo(di);
    }
    
    // Ensures that there is only one occurrence of this class used in the application.
    static public AgendaStaticData getStaticData()
    {
        if(agendaData == null)
        {
            agendaData = new AgendaStaticData();
        }
        
        return agendaData;
    }
    
    public int[] monthColourRange(DateInfo selected)
    {
        int month = selected.getCalendar().get(Calendar.MONTH);
        
        return monthRange[month];
    }
    
    public AgendaConfiguration getConfig()
    {
        return agendaConf;
    }
}
