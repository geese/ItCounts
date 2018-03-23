package com.example.sixgeese.itcounts.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sixgeese.itcounts.areTheseNeeded.ThingItCounts;

import java.util.ArrayList;

import static com.example.sixgeese.itcounts.database.DatabaseConstantsAndStrings.*;

/**
 * Created by sixge on 3/23/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private SQLiteDatabase database; //class level

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String[] tableCreationCommands = {CREATE_THING_TABLE, CREATE_THINGMONTH_TABLE, CREATE_THINGSET_TABLE};
        // create the tables and begin populating data
        for (String tableCreationCommand : tableCreationCommands) {
            try {
                db.execSQL(tableCreationCommand);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        fillDatabaseWithData(db);
    }

    public SQLiteDatabase open() {
        database = getWritableDatabase();
        return database;
    }

    public void close() {
        if (database != null)
            database.close();
    }

    public Cursor execQuery(String query) {
        Cursor cursor = null;
        if (open() != null) {
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }


   /*    INSERT methods   */
   public long insertThing(String title)throws Exception {
       long rowID = -1;

       ContentValues newThing = new ContentValues();
       newThing.put(COLUMN_TITLE, title.trim());

       if (open() != null) {
           rowID = database.insertOrThrow(TABLE_THING, null, newThing);
           close();
       }
       return rowID;
   }

    public long insertThingMonth(int thingID, int year, int month)throws Exception {
        long rowID = -1;

        ContentValues newThingMonth = new ContentValues();
        newThingMonth.put(COLUMN_THINGMONTH_THING_ID, thingID);
        newThingMonth.put(COLUMN_THINGMONTH_YEAR, year);
        newThingMonth.put(COLUMN_THINGMONTH_MONTH, month);

        if (open() != null) {
            rowID = database.insertOrThrow(TABLE_THINGMONTH, null, newThingMonth);
            close();
        }
        return rowID;
    }

    public long insertThingSet(int thingMonthID, int date, int reps)throws Exception {
        long rowID = -1;

        ContentValues newThingSet = new ContentValues();
        newThingSet.put(COLUMN_THINGSET_MONTH_ID, thingMonthID);
        newThingSet.put(COLUMN_THINGSET_DATE, date);
        newThingSet.put(COLUMN_THINGSET_REPS, reps);

        if (open() != null) {
            rowID = database.insertOrThrow(TABLE_THINGSET, null, newThingSet);
            close();
        }
        return rowID;
    }

    public long insertThingSet(String title, int year, int month, int date, int reps)throws Exception {
        long rowID = -1;
        long thingMonthID = -1;

        title = title.replace("'", "''").trim();
        thingMonthID = getThingMonthID(title, year, month);
        if (thingMonthID == -1){
            try {
                thingMonthID = insertThingMonth(getThingID(title), year, month);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ContentValues newThingSet = new ContentValues();
        newThingSet.put(COLUMN_THINGSET_MONTH_ID, thingMonthID);
        newThingSet.put(COLUMN_THINGSET_DATE, date);
        newThingSet.put(COLUMN_THINGSET_REPS, reps);

        if (open() != null) {
            rowID = database.insertOrThrow(TABLE_THINGSET, null, newThingSet);
            close();
        }
        return rowID;
    }



   /*    QUERY methods   */

    public int getThingID(String title){
        Cursor cursor = null;
        int theId = -1;

        if (open() != null){
            title = title.replace("'", "''").trim();
            cursor = database.rawQuery("SELECT _id FROM " + TABLE_THING
                    + " WHERE " + COLUMN_TITLE + " = '" + title + "'", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                theId = cursor.getInt(0);
            }
        }
        return theId;
    }

    public int getThingMonthID(String title, int year, int month){
        Cursor cursor = null;
        int thingMonthID = -1;
        long thingID = -1;

        if (open() != null){
            title = title.replace("'", "''").trim();
            thingID = getThingID(title);
            if (thingID == -1){
                try {
                    thingID = insertThing(title);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (thingID != -1){
                cursor = database.rawQuery("SELECT _id FROM " + TABLE_THINGMONTH
                        + " WHERE " + COLUMN_THINGMONTH_THING_ID + " = " + thingID
                        + " AND " + COLUMN_THINGMONTH_YEAR + " = " + year
                        + " AND " + COLUMN_THINGMONTH_MONTH + " = " + month, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    thingMonthID = cursor.getInt(0);
                }
            }

        }
        return thingMonthID;
    }







    private void fillDatabaseWithData(SQLiteDatabase db) {
        String[] titles = {"Pushups", "Take Vitamin C", "Practice Hindemith", "Add Water to Fish Tank"};

        try {
            insertThingSet(titles[0], 2017, 10, 2, 11);
            insertThingSet(titles[0], 2017, 11, 9, 140);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
