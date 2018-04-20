package com.example.sixgeese.itcounts;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

    private int thingMonthId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detail);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String title = getIntent().getStringExtra(KEY_DAY_DETAIL_TITLE);
        int year = getIntent().getIntExtra(KEY_DAY_DETAIL_YEAR, -1);
        int month = getIntent().getIntExtra(KEY_DAY_DETAIL_MONTH, -1);
        int date = getIntent().getIntExtra(KEY_DAY_DETAIL_DATE, -1);

        thingMonthId = getThingMonthId(title, year, month);

        thingSets = new ArrayList<>();

        //init_thingSets(); // load with dummy data

        dayDetailTitle = findViewById(R.id.dayDetailTitle);
        dayDetailTitle.setText(title);

        dayDetailDate = findViewById(R.id.dayDetailDate);
        dayDetailDate.setText(getIntent().getStringExtra(KEY_DAY_DETAIL_DATE_STRING));

        dayDetailRecyclerView = findViewById(R.id.dayDetailRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        dayDetailRecyclerView.setLayoutManager(linearLayoutManager);

        Bundle bundle = new Bundle();
        bundle.putString(KEY_DAY_DETAIL_TITLE, title);
        bundle.putInt(KEY_DAY_DETAIL_YEAR, year);
        bundle.putInt(KEY_DAY_DETAIL_MONTH, month);
        bundle.putInt(KEY_DAY_DETAIL_DATE, date);
        getSupportLoaderManager().initLoader(KEY_ID_LOADER_THINGSET,bundle,this); // titles and adapter are set up here


        saveSetsButton = findViewById(R.id.btn_saveSets);
        saveSetsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveThingSetsInDatabase();
            }
        });

    }



    private void saveThingSetsInDatabase() {
        Log.d(TAG, "saveThingSetsInDatabase: size of ArrayList: " + thingSets.size());
        // send arraylist of sets to database

        // walk though the ArrayList
        // \__update or delete database records
        // another walk through the ArrayList
        // \__remove zero-rep sets
        // \__clear preferences
        // \__notifyDataSetChanged

        // delete existing sets if their reps are now zero
        // \__ also remove zero-rep sets from ArrayList
        // update existing sets whose reps are greater than zero
        // insert sets if they don't yet exist
        DatabaseHelper db = new DatabaseHelper(this);


        //TODO:  fix this logic...
        for (ThingSet theSet : thingSets) {
            //if the set has a record in the database, update it if nonzero reps, delete if zero
            if (theSet.getId() > -1){  //exists in the database
                if (theSet.getReps() == 0) {
                    Log.d(TAG, "saveThingSetsInDatabase: set _id:" + theSet.getId());
                    db.deleteThingSet(theSet.getId());
                } else {
                    db.updateThingSet(theSet);
                }
            } else { // there is not yet a record for this set in the database
                try {
                    db.insertThingSet(theSet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }



        // remove zero-rep sets from ArrayList, clear preferences, and notify adapter
        for (int i = 0; i < thingSets.size(); i++) {
            if (thingSets.get(i).getReps() <= 0) {
                thingSets.remove(i);
            }
        }
        prefs.edit().clear().apply(); //TODO:  clear only the relevant preferences, not ALL preferences
        adapter.notifyDataSetChanged();
    }

    private void init_thingSets(){
        thingSets.add(new ThingSet(2018, 3, 17, 10));
        thingSets.add(new ThingSet(2018, 3, 17, 11));
        thingSets.add(new ThingSet(2018, 3, 17, 12));

        thingSets.get(0).setReps(10);
        thingSets.get(1).setReps(5);
        thingSets.get(2).setReps(8);
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
        int date = getIntent().getIntExtra(KEY_DAY_DETAIL_DATE, -1);
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
}
