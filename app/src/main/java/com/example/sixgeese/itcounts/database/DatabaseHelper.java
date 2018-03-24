package com.example.sixgeese.itcounts.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sixgeese.itcounts.model.Thing;

import java.util.ArrayList;
import java.util.List;

import static com.example.sixgeese.itcounts.database.DatabaseConstantsAndStrings.*;

/**
 * Created by sixge on 3/23/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        fillDatabaseWithData();
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
           }
       } catch (Exception e) {
           Log.d(TAG, "INSERT EXCEPTION! " + e.getMessage());
       }
       return rowID;
   }

    public long insertThingMonth(int thingID, int year, int month)throws Exception {
        long rowID = -1;

        ContentValues newThingMonth = new ContentValues();
        newThingMonth.put(COLUMN_THINGMONTH_THING_ID, thingID);
        newThingMonth.put(COLUMN_THINGMONTH_YEAR, year);
        newThingMonth.put(COLUMN_THINGMONTH_MONTH, month);

        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            rowID = mWritableDB.insertOrThrow(TABLE_THINGMONTH, null, newThingMonth);
            close();
        } catch (Exception e) {
            Log.d(TAG, "INSERT EXCEPTION! " + e.getMessage());
        }
        return rowID;
    }

    public long insertThingMonth(String thingTitle, int year, int month)throws Exception {
        long rowID = -1;
        rowID = getThingMonthID(thingTitle, year, month);
        long thingID = getThingID(thingTitle);

        if (thingID == -1){
            try {
                thingID = insertThing(thingTitle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
        } catch (SQLException e) {
            Log.d(TAG, "INSERT EXCEPTION! " + e.getMessage());
        }
        return rowID;
    }

    public long insertThingSet(int thingMonthID, int date, int reps)throws Exception {
        long rowID = -1;

        ContentValues newThingSet = new ContentValues();
        newThingSet.put(COLUMN_THINGSET_MONTH_ID, thingMonthID);
        newThingSet.put(COLUMN_THINGSET_DATE, date);
        newThingSet.put(COLUMN_THINGSET_REPS, reps);

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

    public long insertThingSet(String title, int year, int month, int date, int reps)throws Exception {
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
        /*cursor = database.rawQuery("SELECT _id FROM " + TABLE_THING
                + " WHERE " + COLUMN_TITLE + " = ? ", new String[]{title});*/
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

    public int getThingMonthID(String title, int year, int month){
        Cursor cursor = null;
        int thingMonthID = -1;
        long thingID = -1;

        title = title.replace("'", "''").trim();
        thingID = getThingID(title);
        if (thingID == -1){
            try {
                thingID = insertThing(title);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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

    public List<Thing> getAllThings() {
        List<Thing> things = new ArrayList<Thing>();

        Thing thing = null;
        Cursor cursor = null;

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery("SELECT * FROM " + TABLE_THING, null); //don't put a semicolon in this query
            if (cursor.moveToFirst()) {
                do {
                    thing = new Thing(cursor.getString(1));
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
        String[] titles = {"Pushups", "Take Vitamin C", "Practice Hindemith", "Add Water to Fish Tank"};

        //deleteAll(TABLE_THING);
        deleteAll(TABLE_THINGSET);

        try {
            insertThingSet(titles[0], 2017, 10, 2, 11);
            insertThingSet(titles[0], 2017, 11, 9, 140);
            insertThingSet(titles[1], 2017, 11, 17, 9);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
