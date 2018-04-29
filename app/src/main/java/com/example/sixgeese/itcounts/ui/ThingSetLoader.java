package com.example.sixgeese.itcounts.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.sixgeese.itcounts.database.DatabaseHelper;
import com.example.sixgeese.itcounts.model.ThingMonth;
import com.example.sixgeese.itcounts.model.ThingSet;

import java.util.ArrayList;


/**
 * Created by sixge on 3/23/2018.
 */

public class ThingSetLoader extends AsyncTaskLoader<ArrayList<ThingSet>> {

    private static final String TAG = ThingSetLoader.class.getSimpleName();

    private String title;
    private int year, month, date;
    private Context context;

    public ThingSetLoader(Context context, String title, int year, int month, int date) {
        super(context);
        Log.d(TAG, "ThingSetLoader created");

        this.context = context;
        this.title = title;
        this.year = year;
        this.month = month;
        this.date = date;
    }

    @Override
    public ArrayList<ThingSet> loadInBackground() {
        DatabaseHelper db = new DatabaseHelper(context);
        ArrayList<ThingSet> thingSets = db.getThingSets(title, year, month, date);

        return thingSets;
    }

    /**
     * This method is invoked by the LoaderManager whenever the loader is started
     */
    @Override
    protected void onStartLoading() {
        forceLoad(); // Starts the loadInBackground method
    }


}
