package com.example.sixgeese.itcounts;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

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

    TextView dayDetailTitle, dayDetailDate;
    RecyclerView dayDetailRecyclerView;
    ArrayList<ThingSet> thingSets;
    ThingSetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detail);

        String title = getIntent().getStringExtra(KEY_DAY_DETAIL_TITLE);
        int year = getIntent().getIntExtra(KEY_DAY_DETAIL_YEAR, -1);
        int month = getIntent().getIntExtra(KEY_DAY_DETAIL_MONTH, -1);
        int date = getIntent().getIntExtra(KEY_DAY_DETAIL_DATE, -1);

        thingSets = new ArrayList<>();
        //init_thingSets(); // load with dummy data

        dayDetailTitle = findViewById(R.id.dayDetailTitle);
        dayDetailTitle.setText(title);

        dayDetailDate = findViewById(R.id.dayDetailDate);
        dayDetailDate.setText(getIntent().getStringExtra(KEY_DAY_DETAIL_DATE_STRING));

        dayDetailRecyclerView = findViewById(R.id.dayDetailRecyclerView);
        dayDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        Bundle bundle = new Bundle();
        bundle.putString(KEY_DAY_DETAIL_TITLE, title);
        bundle.putInt(KEY_DAY_DETAIL_YEAR, year);
        bundle.putInt(KEY_DAY_DETAIL_MONTH, month);
        bundle.putInt(KEY_DAY_DETAIL_DATE, date);
        getSupportLoaderManager().initLoader(KEY_ID_LOADER_THINGSET,bundle,this); // titles and adapter are set up here

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
        thingSets = theSets;

        adapter = new ThingSetAdapter(thingSets, this);
        dayDetailRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ThingSet>> loader) {

    }
}
