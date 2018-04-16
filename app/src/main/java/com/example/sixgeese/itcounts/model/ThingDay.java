package com.example.sixgeese.itcounts.model;

import java.util.ArrayList;

/**
 * Created by sixge on 3/25/2018.
 */

public class ThingDay {
    private String title;
    private int year, month, date;
    private ArrayList<ThingSet> thingSets;

    public ThingDay(String title, int year, int month, int date){
        this.title = title;
        this.year = year;
        this.month = month;
        this.date = date;
        thingSets = new ArrayList<>();
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalReps(){
        int totalReps = 0;
        if (!thingSets.isEmpty()){
            for (ThingSet thingSet : thingSets) {
                totalReps += thingSet.getReps();
            }
        }
        return totalReps;
    }

    public void addThingSet(ThingSet thingSet){
        thingSets.add(thingSet);
    }

    public ArrayList<ThingSet> getThingSets() {
        return thingSets;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDate() {
        return date;
    }
}
