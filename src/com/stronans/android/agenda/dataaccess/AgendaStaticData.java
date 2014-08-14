package com.stronans.android.agenda.dataaccess;

import android.graphics.Color;
import com.stronans.android.agenda.model.AgendaConfiguration;
import com.stronans.android.agenda.model.DateInfo;

/**
 * Provides static data held within the program
 *
 * @author SimonKing
 */
public class AgendaStaticData {
    static AgendaStaticData agendaData = null;
    static AgendaConfiguration agendaConf = null;

    int[][] monthRange = {
            {Color.WHITE, Color.BLUE}, // January
            {Color.BLUE, Color.CYAN}, // February
            {Color.WHITE, Color.CYAN}, // March
            {Color.WHITE, Color.YELLOW}, // April
            {Color.WHITE, Color.LTGRAY}, // May
            {Color.GREEN, Color.WHITE}, // June
            {Color.GREEN, Color.RED}, // July
            {Color.RED, Color.CYAN}, // August
            {Color.WHITE, Color.RED}, // September
            {Color.WHITE, Color.CYAN}, // October
            {Color.WHITE, Color.CYAN}, // November
            {Color.CYAN, Color.WHITE}
    };     // December

    private AgendaStaticData() {
        // Initial core data (load or setup)

        agendaConf = new AgendaConfiguration();
    }

    // Ensures that there is only one occurrence of this class used in the application.
    static public AgendaStaticData getStaticData() {
        if (agendaData == null) {
            agendaData = new AgendaStaticData();
        }

        return agendaData;
    }

    public int[] monthColourRange(DateInfo selected) {
        int month = selected.getMonth();

        return monthRange[month];
    }

    public AgendaConfiguration getConfig() {
        return agendaConf;
    }
}
