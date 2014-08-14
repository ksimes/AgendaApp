package com.stronans.android.controllers;

import android.widget.TabHost;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.enums.ViewType;
import com.stronans.android.agenda.model.AgendaConfiguration;

public class AgendaController {
    ViewType viewType;
    TabHost tabHost;
    boolean refresh;
    AgendaConfiguration config;

    static AgendaController singletonInst = null;

    private AgendaController() {
        config = AgendaStaticData.getStaticData().getConfig();
        viewType = config.initalViewType();
        // Initial core data (load or setup)
    }

    // Singleton pattern
    static public AgendaController getInst() {
        if (singletonInst == null) {
            singletonInst = new AgendaController();
        }

        return singletonInst;
    }

    public ViewType getView() {
        return viewType;
    }

    public void setView(ViewType viewType) {
        this.viewType = viewType;
    }

    /**
     * @return int value of the enum where Agenda = 0, day = 1, week = 2, Month = 3, Year = 4, Tasks = 5
     */
    public int getViewInt() {
        return viewType.ordinal();
    }

    public boolean setViewInt(int viewType) {
        boolean result = true;

        switch (viewType) {
            case 0:
                this.viewType = ViewType.Agenda;
                break;

            case 1:
                this.viewType = ViewType.Day;
                break;

            case 2:
                this.viewType = ViewType.Week;
                break;

            case 3:
                this.viewType = ViewType.Month;
                break;

            case 4:
                this.viewType = ViewType.Year;
                break;

            case 5:
                this.viewType = ViewType.Tasks;
                break;

            default:
                result = false;
                break;
        }

        return result;
    }

    /**
     * @return the tabHost
     */
    public TabHost getTabHost() {
        return tabHost;
    }

    /**
     * @param tabHost the tabHost to set
     */
    public void setTabHost(TabHost tabHost) {
        this.tabHost = tabHost;
    }
}
