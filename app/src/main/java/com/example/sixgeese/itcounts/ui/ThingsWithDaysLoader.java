package com.example.sixgeese.itcounts.ui;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.sixgeese.itcounts.database.DatabaseHelper;
import com.example.sixgeese.itcounts.model.Thing;
import com.example.sixgeese.itcounts.model.ThingDay;
import com.example.sixgeese.itcounts.model.ThingMonth;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by sixge on 3/23/2018.
 */

public class ThingsWithDaysLoader extends AsyncTaskLoader<ArrayList<ArrayList<ThingDay>>> {

    private static final String TAG = ThingsWithDaysLoader.class.getSimpleName();

    private Context context;

    public ThingsWithDaysLoader(Context context) {
        super(context);
        Log.d(TAG, "ThingsWithDaysLoader created");

        this.context = context;
    }

    @Override
    public ArrayList<ArrayList<ThingDay>> loadInBackground() {

        // so far this is set up for Portrait orientation
        DatabaseHelper db = new DatabaseHelper(context);
        ArrayList<ArrayList<ThingDay>> thingsWithDays = new ArrayList<>();
        ArrayList<Thing> things = db.getAllThings();
        Calendar cal = Calendar.getInstance();

        for (Thing thing : things) {
            thingsWithDays.add(db.getThingDays(thing.getTitle(),
                    cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 4));
        }

        return thingsWithDays;
    }

    /**
     * This method is invoked by the LoaderManager whenever the loader is started
     */
    @Override
    protected void onStartLoading() {
        forceLoad(); // Starts the loadInBackground method
    }
}
