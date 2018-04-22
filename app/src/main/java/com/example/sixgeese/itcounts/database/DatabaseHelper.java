package com.example.sixgeese.itcounts.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sixgeese.itcounts.model.Thing;
import com.example.sixgeese.itcounts.model.ThingDay;
import com.example.sixgeese.itcounts.model.ThingMonth;
import com.example.sixgeese.itcounts.model.ThingSet;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.sixgeese.itcounts.database.DatabaseConstantsAndStrings.*;

/**
 * Created by sixge on 3/23/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    //private static int countCreation = 0;

    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        fillDatabaseWithData();
        /*if (countCreation++ == 0){

        }*/
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
    }






   /*    INSERT methods   */
   public long insertThing(String title)throws Exception {
       long rowID = -1;
       rowID = getThingID(title);

       ContentValues newThing = new ContentValues();
       newThing.put(COLUMN_TITLE, title.trim());

       try {
           if (mWritableDB == null) {
               mWritableDB = getWritableDatabase();
           }
           // don't duplicate a thing record--only create one if rowID comes back -1
           if (rowID == -1) {
               rowID = mWritableDB.insertOrThrow(TABLE_THING, null, newThing);
               generateThingMonths((int) rowID);
           }
       } catch (Exception e) {
           Log.d(TAG, "INSERT EXCEPTION! " + e.getMessage());
       }
       return rowID;
   }


    /*
        Called everytime a new Thing is inserted.  It inserts ThingMonth records going for about two
        years prior to and following the date the new Thing is inserted.
     */
    private void generateThingMonths(int rowID) throws Exception {
        Calendar cal = Calendar.getInstance();
        int thisYear = cal.get(Calendar.YEAR);
        int thisMonth = cal.get(Calendar.MONTH);
        for (int year = thisYear-1; year < thisYear+2; year++){
            for(int month=0; month<12; month++){
                insertThingMonth(rowID, year, month);
            }
        }
    }

    public long insertThingMonth(int thingID, int year, int month)throws Exception {
        long rowID = -1;
        rowID = getThingMonthID(thingID, year, month);

        ContentValues newThingMonth = new ContentValues();
        newThingMonth.put(COLUMN_THINGMONTH_THING_ID, thingID);
        newThingMonth.put(COLUMN_THINGMONTH_YEAR, year);
        newThingMonth.put(COLUMN_THINGMONTH_MONTH, month);

        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            // don't duplicate a thingMonth record--only create one if rowID comes back -1
            if (rowID == -1){
                rowID = mWritableDB.insertOrThrow(TABLE_THINGMONTH, null, newThingMonth);
            }
        } catch (Exception e) {
            Log.d(TAG, "INSERT EXCEPTION! " + e.getMessage());
        }
        return rowID;
    }

    public long insertThingMonth(String thingTitle, int year, int month)throws Exception {
        long rowID = -1;
        rowID = getThingMonthID(thingTitle, year, month);

       // Gonna create Things first before ThingMonths
        //long thingID = getThingID(thingTitle);
        /*if (thingID == -1){
            try {
                thingID = insertThing(thingTitle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        ContentValues newThingMonth = new ContentValues();
        newThingMonth.put(COLUMN_THINGMONTH_THING_ID, getThingID(thingTitle));
        newThingMonth.put(COLUMN_THINGMONTH_YEAR, year);
        newThingMonth.put(COLUMN_THINGMONTH_MONTH, month);

        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            // don't duplicate a thingMonth record--only create one if rowID comes back -1
            if (rowID == -1){
                rowID = mWritableDB.insertOrThrow(TABLE_THINGMONTH, null, newThingMonth);
            }
        } catch (SQLException e) {
            Log.d(TAG, "INSERT EXCEPTION! " + e.getMessage());
        }
        return rowID;
    }

    public long insertThingSet(int thingMonthID, int date, int reps, int ordPos)throws Exception {
        long rowID = -1;

        ContentValues newThingSet = new ContentValues();
        newThingSet.put(COLUMN_THINGSET_MONTH_ID, thingMonthID);
        newThingSet.put(COLUMN_THINGSET_DATE, date);
        newThingSet.put(COLUMN_THINGSET_REPS, reps);
        newThingSet.put(COLUMN_THINGSET_ORDINAL_POSITION, ordPos);

        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            rowID = mWritableDB.insertOrThrow(TABLE_THINGSET, null, newThingSet);
        } catch (SQLException e) {
            Log.d(TAG, "INSERT EXCEPTION! " + e.getMessage());
        }
        return rowID;
    }




    public long insertThingSet(String title, int year, int month, int date, int reps, int ordPos)throws Exception {
        long rowID = -1;
        long thingMonthID = -1;

        title = title.replace("'", "''").trim();
        thingMonthID = getThingMonthID(title, year, month);
        if (thingMonthID == -1){
            try {
                thingMonthID = insertThingMonth(title, year, month);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ContentValues newThingSet = new ContentValues();
        newThingSet.put(COLUMN_THINGSET_MONTH_ID, thingMonthID);
        newThingSet.put(COLUMN_THINGSET_DATE, date);
        newThingSet.put(COLUMN_THINGSET_REPS, reps);
        newThingSet.put(COLUMN_THINGSET_ORDINAL_POSITION, ordPos);

        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            rowID = mWritableDB.insertOrThrow(TABLE_THINGSET, null, newThingSet);
        } catch (SQLException e) {
            Log.d(TAG, "INSERT EXCEPTION! " + e.getMessage());
        }
        return rowID;
    }

    public long insertThingSet(ThingSet thingSet)throws Exception {
        long rowID = -1;

        ContentValues newThingSet = new ContentValues();
        newThingSet.put(COLUMN_THINGSET_MONTH_ID, thingSet.getThingMonthId());
        newThingSet.put(COLUMN_THINGSET_DATE, thingSet.getDate());
        newThingSet.put(COLUMN_THINGSET_REPS, thingSet.getReps());
        newThingSet.put(COLUMN_THINGSET_ORDINAL_POSITION, thingSet.getOrdinalPosition());

        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            rowID = mWritableDB.insertOrThrow(TABLE_THINGSET, null, newThingSet);
        } catch (SQLException e) {
            Log.d(TAG, "INSERT EXCEPTION! " + e.getMessage());
        }
        return rowID;
    }

    public long updateThingSet(ThingSet thingSet) {
        long rowID = -1;

        ContentValues newThingSet = new ContentValues();
        newThingSet.put(COLUMN_THINGSET_REPS, thingSet.getReps());
        newThingSet.put(COLUMN_THINGSET_ORDINAL_POSITION, thingSet.getOrdinalPosition());

        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            rowID = mWritableDB.update(TABLE_THINGSET, newThingSet, "_id = " + thingSet.getId(), null);
        } catch (SQLException e) {
            Log.d(TAG, "INSERT EXCEPTION! " + e.getMessage());
        }
        return rowID;
    }



   /*    QUERY methods   */

    public int getThingID(String title){
        Cursor cursor = null;
        int theId = -1;

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            title = title.replace("'", "''").trim();
            cursor = mReadableDB.query(TABLE_THING, new String[]{"_id"}, COLUMN_TITLE + "= ?", new String[]{title}, null, null, null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                theId = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION! " + e.getMessage());
        } finally {
            cursor.close();
            return theId;
        }
    }

    public String getThingTitle(int thingID){
        Cursor cursor = null;
        String theTitle = null;

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.query(TABLE_THING, new String[]{COLUMN_TITLE},  "_id = ?", new String[]{String.valueOf(thingID)}, null, null, null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                theTitle = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
            }
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION! " + e.getMessage());
        } finally {
            cursor.close();
            return theTitle;
        }
    }


    public int getThingMonthID(String title, int year, int month){
        Cursor cursor = null;
        int thingMonthID = -1;
        long thingID = -1;

        title = title.replace("'", "''").trim();
        thingID = getThingID(title);


        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            if (thingID != -1){
                cursor = mReadableDB.rawQuery("SELECT _id FROM " + TABLE_THINGMONTH
                        + " WHERE " + COLUMN_THINGMONTH_THING_ID + " = " + thingID
                        + " AND " + COLUMN_THINGMONTH_YEAR + " = " + year
                        + " AND " + COLUMN_THINGMONTH_MONTH + " = " + month, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    thingMonthID = cursor.getInt(0);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION! " + e.getMessage());
        } finally {
            cursor.close();
            return thingMonthID;
        }

    }

    public int getThingMonthID(int thingID, int year, int month){
        Cursor cursor = null;
        int thingMonthID = -1;

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            if (thingID != -1){
                cursor = mReadableDB.rawQuery("SELECT _id FROM " + TABLE_THINGMONTH
                        + " WHERE " + COLUMN_THINGMONTH_THING_ID + " = " + thingID
                        + " AND " + COLUMN_THINGMONTH_YEAR + " = " + year
                        + " AND " + COLUMN_THINGMONTH_MONTH + " = " + month, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    thingMonthID = cursor.getInt(0);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION! " + e.getMessage());
        } finally {
            cursor.close();
            return thingMonthID;
        }

    }


    /*public Cursor getAllThings() {
        Cursor cursor = null;
        if (openWritable() != null) {
            cursor = database.rawQuery("SELECT * FROM " + TABLE_THING, null); //don't put a semicolon in this query
        }
        return cursor;
    }*/

    public ArrayList<Thing> getAllThings() {
        ArrayList<Thing> things = new ArrayList<Thing>();

        Thing thing = null;
        Cursor cursor = null;

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery("SELECT " + COLUMN_TITLE + " FROM " + TABLE_THING
                    + " ORDER BY " + COLUMN_TITLE, null); //don't put a semicolon in this query
            if (cursor.moveToFirst()) {
                do {
                    thing = new Thing(cursor.getString(0));
                    things.add(thing);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION! " + e.getMessage());
        } finally {
            cursor.close();
            return things;
        }
    }

    public ArrayList<ThingMonth> getThingMonthsByTitle(String title) {
        ArrayList<ThingMonth> thingMonths = new ArrayList<ThingMonth>();
        int thingID = getThingID(title);

        ThingMonth thingMonth = null;
        Cursor cursor = null;

        // populate the list of ThingMonths from database
        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(
                    "SELECT * FROM " + TABLE_THINGMONTH + " WHERE " + COLUMN_THINGMONTH_THING_ID
                    + " = " + thingID, null); //don't put a semicolon in this query
            if (cursor.moveToFirst()) {
                do {
                    thingMonth = new ThingMonth(title,
                            cursor.getInt(cursor.getColumnIndex(COLUMN_THINGMONTH_YEAR)),
                            cursor.getInt(cursor.getColumnIndex(COLUMN_THINGMONTH_MONTH)));
                    thingMonth.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                    thingMonths.add(thingMonth);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION! " + e.getMessage());
        } finally {
            cursor.close();
        }

        // for each ThingMonth, populate its ThingSets list with ThingSets associated with the ThingMonth _id.
        for (ThingMonth thMonth : thingMonths ) {
            try {
                if (mReadableDB == null) {
                    mReadableDB = getReadableDatabase();
                }
                cursor = mReadableDB.rawQuery(
                        "SELECT * FROM " + TABLE_THINGSET + " WHERE " + COLUMN_THINGSET_MONTH_ID
                                + " = " + thMonth.getId(), null); //don't put a semicolon in this query
                if (cursor.moveToFirst()) {
                    do {
                        ThingSet thingSet = new ThingSet(
                                thMonth.getYear(), thMonth.getMonth(),
                                cursor.getInt(cursor.getColumnIndex(COLUMN_THINGSET_DATE)));
                        thingSet.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                        thingSet.setReps(cursor.getInt(cursor.getColumnIndex(COLUMN_THINGSET_REPS)));
                        thMonth.addThingSet(thingSet);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.d(TAG, "EXCEPTION! " + e.getMessage());
            } finally {
                cursor.close();
            }
        }
        return thingMonths;
    }

    public ThingMonth getThingMonth(String title, int year, int month) {
        // select ThingMonth matching the Thing title, year, and month
        // select ThingSets belonging to the ThingMonth
        return null;
    }

    public ArrayList<ThingDay> getThingDays(String title, int year, int month, int date, int numDays){
        ArrayList<ThingDay> thingDays = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date);

        for (int i = 0; i < numDays; i++){
            int theYear = cal.get(Calendar.YEAR);
            int theMonth = cal.get(Calendar.MONTH);
            int theDate = cal.get(Calendar.DATE);
            Cursor cursor = null;
            ThingDay thingDay = new ThingDay(title, theYear, theMonth, theDate);
            int thingMonthID = getThingMonthID(title, theYear, theMonth);
            try {
                if (mReadableDB == null) {
                    mReadableDB = getReadableDatabase();
                }
                cursor = mReadableDB.rawQuery(
                        "SELECT * FROM " + TABLE_THINGSET + " WHERE " + COLUMN_THINGSET_MONTH_ID
                                + " = " + thingMonthID + " AND " + COLUMN_THINGSET_DATE + " = " + theDate, null); //don't put a semicolon in this query
                if (cursor.moveToFirst()) {
                    do {
                        ThingSet thingSet = new ThingSet(
                                theYear, theMonth, theDate);
                        thingSet.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                        thingSet.setReps(cursor.getInt(cursor.getColumnIndex(COLUMN_THINGSET_REPS)));
                        thingDay.addThingSet(thingSet);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.d(TAG, "EXCEPTION! " + e.getMessage());
            } finally {
                cursor.close();
            }
            // set Calendar back a day
            thingDays.add(thingDay);
            cal.add(Calendar.DATE, -1);
        }
        return thingDays;
    }


    public ArrayList<ThingSet> getThingSets(String title, int year, int month, int date){
        ArrayList<ThingSet> thingSets = new ArrayList<>();

        title = title.replace("'", "''").trim();
        int thingMonthID = getThingMonthID(title, year, month);


        if (thingMonthID != -1) {
            Cursor cursor = null;

            try {
                if (mReadableDB == null) {
                    mReadableDB = getReadableDatabase();
                }
                cursor = mReadableDB.rawQuery(
                        "SELECT * FROM " + TABLE_THINGSET + " WHERE " + COLUMN_THINGSET_MONTH_ID
                                + " = " + thingMonthID + " AND " + COLUMN_THINGSET_DATE + " = " + date
                                + " ORDER BY " + COLUMN_THINGSET_ORDINAL_POSITION, null); //don't put a semicolon in this query
                if (cursor.moveToFirst()) {
                    do {
                        ThingSet thingSet = new ThingSet(
                                year, month, date);
                        thingSet.setId(cursor.getInt(0)); //_id
                        thingSet.setReps(cursor.getInt(cursor.getColumnIndex(COLUMN_THINGSET_REPS)));
                        thingSet.setOrdinalPosition(cursor.getInt(cursor.getColumnIndex(COLUMN_THINGSET_ORDINAL_POSITION)));
                        thingSet.setThingMonthId(cursor.getInt(cursor.getColumnIndex(COLUMN_THINGSET_MONTH_ID)));
                        thingSets.add(thingSet);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.d(TAG, "EXCEPTION! " + e.getMessage());
            } finally {
                cursor.close();
            }
        }
        return thingSets;
    }

    public int deleteThingSet(int theId) {
        int numRowsDeleted = -1;
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            numRowsDeleted = mWritableDB.delete(TABLE_THINGSET, "_id = " + theId, null);
        } catch (Exception e) {
            Log.d (TAG, "DELETE EXCEPTION! " + e.getMessage());
        }
        return numRowsDeleted;
    }



    public void deleteAll(String tableName){
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            mWritableDB.delete(tableName, null, null);
        } catch (Exception e) {
            Log.d (TAG, "DELETE EXCEPTION! " + e.getMessage());
        }
    }




    private void fillDatabaseWithData() {
        String[] titles = {"Add Water to Fish Tank", "Practice Hindemith", "Pushups", "Take Vitamin C" };

        /*deleteAll(TABLE_THING);
        deleteAll(TABLE_THINGMONTH);*/
        //deleteAll(TABLE_THINGSET);

        try {
            for (String title : titles) {
                insertThing(title);
            }

            //insertThing("Practice Hindemith");

            /*insertThingSet(titles[0], 2018, 3, 22, 11, 0);
            insertThingSet(titles[0], 2018, 3, 17, 15, 0);
            insertThingSet(titles[0], 2018, 3, 17, 9, 1);
            insertThingSet(titles[0], 2018, 3, 17, 3, 2);
            insertThingSet(titles[1], 2018, 1, 2, 9, 0);
            insertThingSet(titles[1], 2018, 2, 1, 6, 0);
            insertThingSet(titles[1], 2018, 2, 3, 2, 0);
            insertThingSet(titles[1], 2018, 2, 5, 2, 0);
            insertThingSet(titles[1], 2018, 4, 7, 13, 0);
            insertThingSet(titles[1], 2018, 4, 7, 100, 1);
            insertThingSet(titles[1], 2018, 2, 24, 6, 0);
            insertThingSet(titles[1], 2018, 2, 24, 12, 1);
            insertThingSet(titles[2], 2018, 2, 23, 10000, 0);
            insertThingSet(titles[2], 2018, 2, 23, 10500, 1);*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
