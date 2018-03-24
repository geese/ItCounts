package com.example.sixgeese.itcounts;

import android.content.Intent;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.sixgeese.itcounts.model.Thing;
import com.example.sixgeese.itcounts.model.ThingMonth;
import com.example.sixgeese.itcounts.ui.ThingLoader;
import com.example.sixgeese.itcounts.ui.ThingMonthAdapter;
import com.facebook.stetho.Stetho;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<List<Thing>>{

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String KEY_THING_TITLE = "thing_title";

    ListView titlesListView;
    ArrayList<String> titles;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);

        titles = new ArrayList<>();
        titlesListView = findViewById(R.id.thingTitles);
        getSupportLoaderManager().initLoader(0,null,this);


        //titles = new String[]{"Take Vitamin C", "Practice Hindemith", "Add Water to Fish Tanks", "Pushups"};
        /*adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        titlesListView.setAdapter(adapter);*/
    }


    public void goToCalendar(View view) {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }

    @Override
    public Loader<List<Thing>> onCreateLoader(int id, Bundle args) {
        return new ThingLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Thing>> loader, List<Thing> things) {
        Log.d(TAG, "Number of Things from Loader: " + things.size());
        titles.clear();
        for (Thing thing : things) {
            titles.add(thing.getTitle());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        titlesListView.setAdapter(adapter);
        titlesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                intent.putExtra(KEY_THING_TITLE, titles.get(i));
                MainActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<List<Thing>> loader) {}
}
