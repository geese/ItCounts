package com.example.sixgeese.itcounts.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Property;

/**
 * Created by sixge on 3/22/2018.
 */

public class ThingSet implements Parcelable{
    private int reps;
    private int year, month, date, id, ordinal_position, thingMonthId;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(":id:").append(this.id)
                .append(":monthId:").append(this.thingMonthId)
                .append(":date:").append(this.date)
                .append(":reps:").append(this.reps)
                .append(":ordinal:").append(this.ordinal_position);
        return sb.toString();
    }

    public ThingSet(Parcel parcel) {
        this.reps = parcel.readInt();
        this.year = parcel.readInt();
        this.month = parcel.readInt();
        this.date = parcel.readInt();
        this.id = parcel.readInt();
        this.ordinal_position = parcel.readInt();
        this.thingMonthId = parcel.readInt();
    }

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

    public int getId() {return id;}

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(reps);
        parcel.writeInt(year);
        parcel.writeInt(month);
        parcel.writeInt(date);
        parcel.writeInt(id);
        parcel.writeInt(ordinal_position);
        parcel.writeInt(thingMonthId);
    }

    //used when un-parceling the parcel (creating the object)
    public static final Parcelable.Creator<ThingSet> CREATOR = new Parcelable.Creator<ThingSet>(){

        @Override
        public ThingSet createFromParcel(Parcel parcel) {
            return new ThingSet(parcel);
        }

        @Override
        public ThingSet[] newArray(int size) {
            return new ThingSet[size];
        }
    };
}
