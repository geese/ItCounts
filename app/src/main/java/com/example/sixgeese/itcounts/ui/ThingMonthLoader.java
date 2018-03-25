package com.example.sixgeese.itcounts.ui;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.sixgeese.itcounts.database.DatabaseHelper;
import com.example.sixgeese.itcounts.model.Thing;
import com.example.sixgeese.itcounts.model.ThingMonth;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sixge on 3/23/2018.
 */

public class ThingMonthLoader extends AsyncTaskLoader<ArrayList<ThingMonth>> {

    private static final String TAG = ThingMonthLoader.class.getSimpleName();

    private String theTitle;
    private Context context;

    public ThingMonthLoader(Context context, String title) {
        super(context);
        Log.d(TAG, "ThingMonthLoader created");

        this.context = context;
        this.theTitle = title;
    }

    @Override
    public ArrayList<ThingMonth> loadInBackground() {
        DatabaseHelper db = new DatabaseHelper(context);
        ArrayList<ThingMonth> thingMonths = db.getThingMonthsByTitle(theTitle);
        Log.d(TAG, "Number of Months from database: " + thingMonths.size());
        return thingMonths;
    }

    /**
     * This method is invoked by the LoaderManager whenever the loader is started
     */
    @Override
    protected void onStartLoading() {
        forceLoad(); // Starts the loadInBackground method
    }
}
