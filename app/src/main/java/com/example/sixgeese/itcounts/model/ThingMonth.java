package com.example.sixgeese.itcounts.model;

import java.util.ArrayList;

/**
 * Created by sixge on 3/22/2018.
 */

public class ThingMonth {

    private String thingTitle;
    private int year, month;
    private ArrayList<ThingSet> thingSets;

    public ThingMonth(int year, int month){
        this.year = year;
        this.month = month;
    }

    public String getThingTitle() {
        return thingTitle;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }
}
