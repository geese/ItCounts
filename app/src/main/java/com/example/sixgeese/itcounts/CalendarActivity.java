package com.example.sixgeese.itcounts;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.widget.TextView;

import com.example.sixgeese.itcounts.model.Thing;
import com.example.sixgeese.itcounts.model.ThingMonth;
import com.example.sixgeese.itcounts.ui.ThingMonthAdapter;
import com.example.sixgeese.itcounts.ui.ThingMonthLoader;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CalendarActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<ThingMonth>>{

    private static final String TAG = CalendarActivity.class.getSimpleName();

    public static final int KEY_ID_LOADER_THINGMONTH = 1;

    TextView thingTitle;
    ArrayList<ThingMonth> thingMonths;
    RecyclerView recyclerView;
    ThingMonthAdapter adapter;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        thingTitle = findViewById(R.id.theThingTitle);
        title = getIntent().getStringExtra(MainActivity.KEY_THING_TITLE);
        thingTitle.setText(title);

        //initThingMonths();


        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager horizLayoutMgr =
                new LinearLayoutManager(CalendarActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizLayoutMgr);


        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(recyclerView);


        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.KEY_THING_TITLE, title);
        getSupportLoaderManager().initLoader(1,bundle,this); // titles and adapter are set up here

    }

    private void initThingMonths() {
        thingMonths = new ArrayList<>();
        thingMonths.add(new ThingMonth("Take Vitamins", 2017, 10));
        thingMonths.add(new ThingMonth("Take Vitamins", 2017, 11));
        thingMonths.add(new ThingMonth("Take Vitamins", 2017, 12));
        thingMonths.add(new ThingMonth("Take Vitamins", 2018, 1));
        thingMonths.add(new ThingMonth("Take Vitamins", 2018, 2));
        thingMonths.add(new ThingMonth("Take Vitamins", 2018, 3));

        thingMonths.get(5).addThingSet(4).setReps(2);
        thingMonths.get(5).addThingSet(4).setReps(6);
        thingMonths.get(5).addThingSet(6).setReps(3);
        thingMonths.get(5).addThingSet(17).setReps(15);
    }

    @Override
    public Loader<ArrayList<ThingMonth>> onCreateLoader(int id, Bundle args) {
        return new ThingMonthLoader(CalendarActivity.this, args.getString(MainActivity.KEY_THING_TITLE));
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ThingMonth>> loader, ArrayList<ThingMonth> thMonths) {
        thingMonths = thMonths;

        adapter = new ThingMonthAdapter(thingMonths, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(getThisMonthPosition());

    }

    private int getThisMonthPosition() {
        Calendar cal = Calendar.getInstance();
        int thisMonthPosition = 0;
        for ( ThingMonth thingMonth : thingMonths ) {
            if (thingMonth.getYear() == cal.get(Calendar.YEAR) && thingMonth.getMonth() == cal.get(Calendar.MONTH)){
                thisMonthPosition = thingMonths.indexOf(thingMonth);
                break;
            }
        }
        return thisMonthPosition;
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ThingMonth>> loader) {

    }
}
