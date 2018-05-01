package com.example.sixgeese.itcounts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sixgeese.itcounts.database.DatabaseHelper;
import com.example.sixgeese.itcounts.model.ThingSet;
import com.example.sixgeese.itcounts.ui.ThingSetAdapter;
import com.example.sixgeese.itcounts.ui.ThingSetLoader;
import com.example.sixgeese.itcounts.utility.OnStartDragListener;
import com.example.sixgeese.itcounts.utility.ThingSetItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class DayDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<ThingSet>>, OnStartDragListener {
    private static final String TAG = DayDetailActivity.class.getSimpleName();

    public static final int KEY_ID_LOADER_THINGSET = 2;  // unique id for this activity's loader
    public static final String KEY_SAVED_THINGSETS = "ThingSetsArrayList";
    public static final String KEY_SETLABELS = "SetLabels_";
    public static final String KEY_REPLABELS = "RepLabels_";
    public static final String KEY_SETSLABEL_THIS_THING = "SetLabelThisThing_";
    public static final String KEY_REPSLABEL_THIS_THING = "RepsLabelThisThing_";
    public static final String KEY_STRING_ARRAYLIST_EXTRA_LABELS = "labels_arrayList";

    public static final String KEY_DAY_DETAIL_TITLE = "day_detail_title";
    public static final String KEY_DAY_DETAIL_YEAR = "day_detail_year";
    public static final String KEY_DAY_DETAIL_MONTH = "day_detail_month";
    public static final String KEY_DAY_DETAIL_DATE = "day_detail_date";
    public static final String KEY_DAY_DETAIL_DATE_STRING = "day_detail_date_string";
    public static final String KEY_THING_ID = "thing_id";

    SharedPreferences prefs;
    ItemTouchHelper itemTouchHelper;
    TextView dayDetailTitle, dayDetailDate, txvSetsLabel, txvRepsLabel;
    Button saveSetsButton;
    RecyclerView dayDetailRecyclerView;
    ArrayList<ThingSet> thingSets;
    ThingSetAdapter adapter;

    String title;
    int year, month, date, thingMonthId, thingId;

    public void startSetLabelsActivity(View view) {
        ArrayList<String> labels;

        if (prefs.contains(KEY_SETLABELS)) {
            labels = new ArrayList<>(prefs.getStringSet(KEY_SETLABELS, null));
            Collections.sort(labels);
        } else {
            labels = new ArrayList<String>();
            labels.add("");
        }

        Intent intent = getIntent();
        intent.setClass(this, SetLabelsActivity.class);
        intent.putExtra(KEY_THING_ID, thingId);
        intent.putStringArrayListExtra(KEY_STRING_ARRAYLIST_EXTRA_LABELS, labels);
        startActivity(intent);
    }

    public void startRepLabelsActivity(View view) {
        ArrayList<String> labels;

        if (prefs.contains(KEY_REPLABELS)) {
            labels = new ArrayList<>(prefs.getStringSet(KEY_REPLABELS, null));
            Collections.sort(labels);
        } else {
            labels = new ArrayList<String>();
            labels.add("");
        }

        Intent intent = getIntent();
        intent.setClass(this, RepLabelsActivity.class);
        intent.putExtra(KEY_THING_ID, thingId);
        intent.putStringArrayListExtra(KEY_STRING_ARRAYLIST_EXTRA_LABELS, labels);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detail);

        title = getIntent().getStringExtra(KEY_DAY_DETAIL_TITLE);
        year = getIntent().getIntExtra(KEY_DAY_DETAIL_YEAR, -1);
        month = getIntent().getIntExtra(KEY_DAY_DETAIL_MONTH, -1);
        date = getIntent().getIntExtra(KEY_DAY_DETAIL_DATE, -1);
        thingMonthId = getThingMonthId(title, year, month);
        thingId = getThingId(thingMonthId);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        saveSetsButton = findViewById(R.id.btn_saveSets);
        dayDetailTitle = findViewById(R.id.dayDetailTitle);
        dayDetailDate = findViewById(R.id.dayDetailDate);
        txvSetsLabel = findViewById(R.id.sets_label_textview);
        txvRepsLabel = findViewById(R.id.reps_label_textview);
        dayDetailRecyclerView = findViewById(R.id.dayDetailRecyclerView);

        dayDetailTitle.setText(title);
        dayDetailDate.setText(getIntent().getStringExtra(KEY_DAY_DETAIL_DATE_STRING));
        txvSetsLabel.setText(prefs.getString(KEY_SETSLABEL_THIS_THING + thingId, getString(R.string.sets)));
        txvRepsLabel.setText(prefs.getString(KEY_REPSLABEL_THIS_THING + thingId, getString(R.string.reps)));

        dayDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        saveSetsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: thingSets.size()=" + thingSets.size());
                clickSaveButton(thingSets);
            }
        });

        // here is where the adapter gets created and attached to the RecyclerView
        // also where the ArrayList<ThingSet> thingSets gets loaded from the database
        //if (getSupportLoaderManager().getLoader(KEY_ID_LOADER_THINGSET) == null){
        if (savedInstanceState == null) { //activity is being created for the first time
            getSupportLoaderManager().initLoader(KEY_ID_LOADER_THINGSET,getLoaderBundle(),this);
        }


        //https://stackoverflow.com/questions/29980561/add-edittext-to-toolbar
        if (!prefs.contains(KEY_SETLABELS)){
            Set<String> setLabels = new HashSet<>();
            setLabels.add("Sets");
            setLabels.add("Planks");
            setLabels.add("Sessions");

            prefs.edit().putStringSet(KEY_SETLABELS, setLabels).apply();
        }

        if (!prefs.contains(KEY_REPLABELS)){
            Set<String> repLabels = new HashSet<>();
            repLabels.add("Repetitions");
            repLabels.add("Seconds");
            repLabels.add("Ounces");

            prefs.edit().putStringSet(KEY_REPLABELS, repLabels).apply();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {return super.onCreateOptionsMenu(menu);}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {return super.onOptionsItemSelected(item);}

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(KEY_SAVED_THINGSETS, thingSets);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        thingSets = savedInstanceState.getParcelableArrayList(KEY_SAVED_THINGSETS);

        setupAdapter();
    }

    private void setupAdapter() {
        adapter = new ThingSetAdapter(thingSets, this, thingMonthId, date);
        dayDetailRecyclerView.setAdapter(adapter);

        //https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-b9456d2b1aaf
        //https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-6a6f0c422efd

        ItemTouchHelper.Callback callback =
                new ThingSetItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(dayDetailRecyclerView);
    }


    // disables EditTexts in the RecyclerView while it's in action mode
    // called from ThingSetAdapter.java
    public void noticeMultiSelect(boolean multiSelect) {
        int numItems = dayDetailRecyclerView.getChildCount() - 1; // minus the last one, which is the "add another set" button

        for (int i = 0; i < numItems; i++) {
            EditText et = (EditText)(dayDetailRecyclerView.getChildAt(i).findViewWithTag(ThingSetAdapter.THIS_IS_AN_EDIT_TEXT));
            if (multiSelect)
                et.setEnabled(false);
            else
                et.setEnabled(true);
        }
    }


    // currently this is only called from the Save button
    private void clickSaveButton(ArrayList<ThingSet> theSets) {
        DatabaseHelper db = new DatabaseHelper(this);

        for (ThingSet theSet : theSets) {
            //if the set has a record in the database, update it if nonzero reps, delete if zero
            if (theSet.getId() > -1){  //exists in the database
                if (theSet.getReps() == 0) {
                    db.deleteThingSet(theSet.getId()); // this puts gaps in ordinal positioning in database, but that gets reset in db.getThingSets
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

        prefs.edit().remove("ThingMonth" + thingMonthId).apply();

        // get the refreshed list of ThingSets from the database now that sets have been deleted/updated/inserted
        restartLoader();

    }

    private void restartLoader() {
        getSupportLoaderManager().restartLoader(KEY_ID_LOADER_THINGSET, getLoaderBundle(), this);
    }

    public void deleteThingSets(ArrayList<ThingSet> setsToDelete) {
        DatabaseHelper db = new DatabaseHelper(this);

        for (ThingSet theSet : setsToDelete) {
            //if the set has a record in the database, delete it
            if (theSet.getId() > -1) {  //exists in the database
                db.deleteThingSet(theSet.getId());
            }
        }
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
        thingSets = theSets;  // now it's an ArrayList loaded from the database
        setupAdapter();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ThingSet>> loader) {

    }

    private int getThingMonthId(String title, int year, int month) {
        DatabaseHelper db = new DatabaseHelper(this);
        return db.getThingMonthID(title, year, month);
    }

    private int getThingId(int thingMonthId) {
        DatabaseHelper db = new DatabaseHelper(this);
        return db.getThingId(thingMonthId);
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


    //this is for dummy data
    private void init_thingSets(){
        thingSets.add(new ThingSet(2018, 3, 17, 10));
        thingSets.add(new ThingSet(2018, 3, 17, 11));
        thingSets.add(new ThingSet(2018, 3, 17, 12));

        thingSets.get(0).setReps(10);
        thingSets.get(1).setReps(5);
        thingSets.get(2).setReps(8);
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    // MAY NOT NEED THIS
    private void sortThingSetStringsByOrdinalPosition(ArrayList<String> listOfThingSetStrings) {
    /*   -1 : o1 < o2,  0 : o1 == o2, +1 : o1 > o2   */
        Collections.sort(listOfThingSetStrings, new Comparator<String>() {
            @Override
            public int compare(String first, String second) {
                String[] firstSplit = first.split(":ordinal:");
                String[] secondSplit = second.split(":ordinal:");
                int firstInt = Integer.valueOf(firstSplit[firstSplit.length-1]);
                int secondInt = Integer.valueOf(secondSplit[secondSplit.length-1]);

                if (firstInt == secondInt) return 0;
                else return (firstInt < secondInt) ? -1 : 1;
            }
        });
    }



}
