package com.example.sixgeese.itcounts;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sixgeese.itcounts.database.DatabaseHelper;
import com.example.sixgeese.itcounts.model.ThingSet;
import com.example.sixgeese.itcounts.ui.ThingLoader;
import com.example.sixgeese.itcounts.ui.ThingMonthAdapter;
import com.example.sixgeese.itcounts.ui.ThingMonthLoader;
import com.example.sixgeese.itcounts.ui.ThingSetAdapter;
import com.example.sixgeese.itcounts.ui.ThingSetLoader;

import java.util.ArrayList;

public class DayDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<ThingSet>>{
    private static final String TAG = DayDetailActivity.class.getSimpleName();

    public static final int KEY_ID_LOADER_THINGSET = 2;

    public static final String KEY_DAY_DETAIL_TITLE = "day_detail_title";
    public static final String KEY_DAY_DETAIL_YEAR = "day_detail_year";
    public static final String KEY_DAY_DETAIL_MONTH = "day_detail_month";
    public static final String KEY_DAY_DETAIL_DATE = "day_detail_date";
    public static final String KEY_DAY_DETAIL_DATE_STRING = "day_detail_date_string";

    SharedPreferences prefs;
    TextView dayDetailTitle, dayDetailDate;
    Button saveSetsButton;
    RecyclerView dayDetailRecyclerView;
    ArrayList<ThingSet> thingSets;
    ThingSetAdapter adapter;

    String title;
    int year;
    int month;
    int date;

    private int thingMonthId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detail);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        title = getIntent().getStringExtra(KEY_DAY_DETAIL_TITLE);
        year = getIntent().getIntExtra(KEY_DAY_DETAIL_YEAR, -1);
        month = getIntent().getIntExtra(KEY_DAY_DETAIL_MONTH, -1);
        date = getIntent().getIntExtra(KEY_DAY_DETAIL_DATE, -1);
        thingMonthId = getThingMonthId(title, year, month);

        dayDetailTitle = findViewById(R.id.dayDetailTitle);
        dayDetailTitle.setText(title);

        dayDetailDate = findViewById(R.id.dayDetailDate);
        dayDetailDate.setText(getIntent().getStringExtra(KEY_DAY_DETAIL_DATE_STRING));

        dayDetailRecyclerView = findViewById(R.id.dayDetailRecyclerView);
        dayDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        saveSetsButton = findViewById(R.id.btn_saveSets);
        saveSetsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveThingSetsInDatabase();
            }
        });

        if (getSupportLoaderManager().getLoader(KEY_ID_LOADER_THINGSET) == null){
            getSupportLoaderManager().initLoader(KEY_ID_LOADER_THINGSET,getLoaderBundle(),this); // titles and adapter are set up here
        }
    }


    private void saveThingSetsInDatabase() {
        DatabaseHelper db = new DatabaseHelper(this);

        for (ThingSet theSet : thingSets) {
            //if the set has a record in the database, update it if nonzero reps, delete if zero
            if (theSet.getId() > -1){  //exists in the database
                if (theSet.getReps() == 0) {
                    db.deleteThingSet(theSet.getId());
                } else {
                    long setId = db.updateThingSet(theSet);
                }
            } else { // there is not yet a record for this set in the database
                if (theSet.getReps() > 0){
                    try {
                        long setId = db.insertThingSet(theSet);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // get the refreshed list of ThingSets from the database now that sets have been deleted/updated/inserted
        getSupportLoaderManager().restartLoader(KEY_ID_LOADER_THINGSET, getLoaderBundle(), this);

        prefs.edit().clear().commit(); //TODO:  clear only the relevant preferences, not ALL preferences

    }



    @Override
    public Loader<ArrayList<ThingSet>> onCreateLoader(int id, Bundle args) {
        return new ThingSetLoader(DayDetailActivity.this,
                args.getString(KEY_DAY_DETAIL_TITLE),
                args.getInt(KEY_DAY_DETAIL_YEAR),
                args.getInt(KEY_DAY_DETAIL_MONTH),
                args.getInt(KEY_DAY_DETAIL_DATE));
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ThingSet>> loader, ArrayList<ThingSet> theSets) {
        thingSets = theSets;
        adapter = new ThingSetAdapter(thingSets, this, thingMonthId, date);
        dayDetailRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ThingSet>> loader) {

    }

    private int getThingMonthId(String title, int year, int month) {
        DatabaseHelper db = new DatabaseHelper(this);
        return db.getThingMonthID(title, year, month);
    }

    @NonNull
    Bundle getLoaderBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_DAY_DETAIL_TITLE, title);
        bundle.putInt(KEY_DAY_DETAIL_YEAR, year);
        bundle.putInt(KEY_DAY_DETAIL_MONTH, month);
        bundle.putInt(KEY_DAY_DETAIL_DATE, date);
        return bundle;
    }


    private void init_thingSets(){
        thingSets.add(new ThingSet(2018, 3, 17, 10));
        thingSets.add(new ThingSet(2018, 3, 17, 11));
        thingSets.add(new ThingSet(2018, 3, 17, 12));

        thingSets.get(0).setReps(10);
        thingSets.get(1).setReps(5);
        thingSets.get(2).setReps(8);
    }



}
