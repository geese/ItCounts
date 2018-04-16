package com.example.sixgeese.itcounts;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sixgeese.itcounts.model.Thing;
import com.example.sixgeese.itcounts.model.ThingDay;
import com.example.sixgeese.itcounts.model.ThingMonth;
import com.example.sixgeese.itcounts.ui.ThingListAdapter;
import com.example.sixgeese.itcounts.ui.ThingLoader;
import com.example.sixgeese.itcounts.ui.ThingMonthAdapter;
import com.example.sixgeese.itcounts.ui.ThingsWithDaysLoader;
import com.facebook.stetho.Stetho;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<ArrayList<ArrayList<ThingDay>>>{

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String KEY_THING_TITLE = "thing_title";


    RecyclerView titlesRecyclerView;
    ArrayList<ArrayList<ThingDay>> thingsWithDays;
    ThingListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this); // so I can look at the database contents in the browser
        setContentView(R.layout.activity_main);

        Calendar cal = Calendar.getInstance();
        LinearLayout header = findViewById(R.id.dates_header);
        LinearLayout[] headerDates = new LinearLayout[]{
                (LinearLayout)header.getChildAt(1),
                (LinearLayout)header.getChildAt(2),
                (LinearLayout)header.getChildAt(3),
                (LinearLayout)header.getChildAt(4)
        };
        for (LinearLayout headerDate : headerDates) {
            TextView day = (TextView)headerDate.getChildAt(0);
            TextView date = (TextView)headerDate.getChildAt(1);
            day.setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()).toUpperCase());
            date.setText(String.valueOf(cal.get(Calendar.DATE)));
            cal.add(Calendar.DATE, -1);
        }



        thingsWithDays = new ArrayList<>();
        titlesRecyclerView = findViewById(R.id.thingTitlesRecyclerView);
        titlesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getSupportLoaderManager().initLoader(0,null,this); // titles and adapter are set up here
    }




    @Override
    public Loader<ArrayList<ArrayList<ThingDay>>> onCreateLoader(int id, Bundle args) {
        return new ThingsWithDaysLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ArrayList<ThingDay>>> loader, ArrayList<ArrayList<ThingDay>> thingsWDays) {
        Log.d(TAG, "ThingsWDays.size(): " + thingsWDays.size());
        thingsWithDays.clear();
        for (ArrayList<ThingDay> thingWithDays : thingsWDays) {
            thingsWithDays.add(thingWithDays);
        }
        adapter = new ThingListAdapter(thingsWithDays, this);
        titlesRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ArrayList<ThingDay>>> loader) {}
}
