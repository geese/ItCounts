package com.example.sixgeese.itcounts.model;

/**
 * Created by sixge on 3/22/2018.
 */

public class ThingSet {
    private int reps;
    private int year, month, date, id, ordinal_position, thingMonthId;

    public ThingSet(int year, int month, int date){
        this.year = year;
        this.month = month;
        this.date = date;
        this.id = -1;
        this.thingMonthId = -1;
    }

    public ThingSet(int year, int month, int date, int id){
        this.year = year;
        this.month = month;
        this.date = date;
        this.id = id;
    }

    public ThingSet(int date){
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void incrementReps(int howMany) {
        this.reps += howMany;
    }
    public void decrementReps(int howMany) {
        this.reps -= howMany;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getThingMonthId() {
        return thingMonthId;
    }

    public void setThingMonthId(int thingMonthId) {
        this.thingMonthId = thingMonthId;
    }

    public int getOrdinalPosition() {
        return ordinal_position;
    }

    public void setOrdinalPosition(int ordinal_position) {
        this.ordinal_position = ordinal_position;
    }
}
