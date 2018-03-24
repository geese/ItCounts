package com.example.sixgeese.itcounts.ui;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.sixgeese.itcounts.MainActivity;
import com.example.sixgeese.itcounts.database.DatabaseHelper;
import com.example.sixgeese.itcounts.model.Thing;

import java.util.List;


/**
 * Created by sixge on 3/23/2018.
 */

public class ThingLoader extends AsyncTaskLoader<List<Thing>> {

    private static final String TAG = ThingLoader.class.getSimpleName();


    private Context context;

    public ThingLoader(Context context) {
        super(context);
        Log.d(TAG, "ThingLoader created");

        this.context = context;
    }

    @Override
    public List<Thing> loadInBackground() {
        DatabaseHelper db = new DatabaseHelper(context);
        List<Thing> allThings = db.getAllThings();
        Log.d(TAG, "Number of Things from database: " + allThings.size());
        return allThings;
    }

    /**
     * This method is invoked by the LoaderManager whenever the loader is started
     */
    @Override
    protected void onStartLoading() {
        forceLoad(); // Starts the loadInBackground method
    }
}
