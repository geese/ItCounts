package com.example.sixgeese.itcounts.database;

/**
 * Created by sixge on 3/23/2018.
 */

public class DatabaseConstantsAndStrings {
    /**
     * The current version of the app database, used to trigger upgrades by incrementing
     */
    protected static final int DATABASE_VERSION = 1; // has to be 1 first time or app will crash
    protected static final String DATABASE_NAME = "itcounts.db";



    // Table name constants and column name constants start here

    /**
     * Thing table name and associated column name constants
     */
    protected static final String TABLE_THING = "thing";
    //protected static final String COLUMN_THING_ID = "id";
    protected static final String COLUMN_TITLE = "title";

    /**
     * ThingMonth table name and associated column name constants
     */
    protected static final String TABLE_THINGMONTH = "thingmonth";
    //protected static final String COLUMN_THINGMONTH_ID = "id";
    protected static final String COLUMN_THINGMONTH_THING_ID = "thing_id";
    protected static final String COLUMN_THINGMONTH_YEAR = "year";
    protected static final String COLUMN_THINGMONTH_MONTH = "month";

    /**
     * ThingSet table name and associated column name constants
     */
    protected static final String TABLE_THINGSET = "thingset";
    //protected static final String COLUMN_THINGSET_ID = "id";
    protected static final String COLUMN_THINGSET_THING_ID = "thingset_thing_id";
    protected static final String COLUMN_THINGSET_MONTH_ID = "thingmonth_id";
    protected static final String COLUMN_THINGSET_DATE = "date";
    protected static final String COLUMN_THINGSET_REPS = "reps";
    protected static final String COLUMN_THINGSET_ORDINAL_POSITION = "ordinal_position";

    // end table name and column constants




    /**
     * Thing table creation SQL statement
     */
    protected static final String CREATE_THING_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_THING + "(" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " TEXT);";

    /**
     * ThingMonth table creation SQL statement
     */
    protected static final String CREATE_THINGMONTH_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_THINGMONTH + "(" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_THINGMONTH_THING_ID + " INTEGER REFERENCES " + TABLE_THING + "(_id) ON UPDATE CASCADE ON DELETE CASCADE, " +
            COLUMN_THINGMONTH_YEAR + " INTEGER, " +
            COLUMN_THINGMONTH_MONTH + " INTEGER);";

    /**
     * ThingSet table creation SQL statement
     */
    protected static final String CREATE_THINGSET_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_THINGSET + "(" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            //COLUMN_THINGSET_THING_ID + " INTEGER REFERENCES " + TABLE_THING + "(_id) ON UPDATE CASCADE ON DELETE CASCADE, " +
            COLUMN_THINGSET_MONTH_ID + " INTEGER REFERENCES " + TABLE_THINGMONTH + "(_id) ON UPDATE CASCADE ON DELETE CASCADE, " +
            COLUMN_THINGSET_DATE + " INTEGER, " +
            COLUMN_THINGSET_REPS + " INTEGER, " +
            COLUMN_THINGSET_ORDINAL_POSITION + " INTEGER);";
}
