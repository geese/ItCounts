package com.example.sixgeese.itcounts.model;

import java.util.ArrayList;

/**
 * Created by sixge on 3/22/2018.
 */

public class ThingMonth {

    private String thingTitle;
    private int year, month;
    private ArrayList<ThingSet> thingSets;

    public ThingMonth(String thingTitle, int year, int month){
        this.year = year;
        this.month = month;
        thingSets = new ArrayList<>();
    }

    public ThingSet addThingSet(int date){
        ThingSet newThingSet = new ThingSet(date);
        thingSets.add(newThingSet);
        return newThingSet;
    }

    public ThingSet removeThingSet(int index){
        return thingSets.remove(index);
    }

    public boolean removeThingSet(ThingSet thingSet){
        return thingSets.remove(thingSet);
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

    public ArrayList<ThingSet> getThingSets() {
        return this.thingSets;
    }

    public int getTotalReps(int date){
        int totalReps = 0;
        if (!thingSets.isEmpty()){
            for (ThingSet thingSet : thingSets) {
                if (thingSet.getDate() == date){
                    totalReps += thingSet.getReps();
                }
            }
        }
        return totalReps;
    }
}
