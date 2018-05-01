package com.example.sixgeese.itcounts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.sixgeese.itcounts.ui.RepLabelsAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


//https://stackoverflow.com/questions/6867076/getactionbar-returns-null
//https://stackoverflow.com/questions/27654703/put-edittext-to-action-bar-instead-of-title
//https://stackoverflow.com/questions/12276027/how-can-i-return-to-a-parent-activity-correctly

public class RepLabelsActivity extends AppCompatActivity {

    SharedPreferences prefs;
    RecyclerView repLabelsRecyclerView;
    RepLabelsAdapter adapter;
    ArrayList<String> labels;
    EditText etxSearch;
    int thingId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_labels);
        setupActionBar();

        thingId = getIntent().getIntExtra(DayDetailActivity.KEY_THING_ID, -1);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        labels = getIntent().getStringArrayListExtra(DayDetailActivity.KEY_STRING_ARRAYLIST_EXTRA_LABELS);

        repLabelsRecyclerView = findViewById(R.id.rep_labels_recyclerview);
        repLabelsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RepLabelsAdapter(this, labels, etxSearch);
        repLabelsRecyclerView.setAdapter(adapter);
    }

    public void selectRepLabel(String selection) {
        prefs.edit().putString(DayDetailActivity.KEY_REPSLABEL_THIS_THING + thingId, selection).apply();
        Intent intent = getIntent();
        intent.setClass(this, DayDetailActivity.class);
        startActivity(intent);
    }

    public void createRepLabel(String creation) {
        Set repLabels = prefs.getStringSet(DayDetailActivity.KEY_REPLABELS, new HashSet());
        repLabels.add(creation);
        prefs.edit().putStringSet(DayDetailActivity.KEY_REPLABELS, repLabels).apply();
        prefs.edit().putString(DayDetailActivity.KEY_REPSLABEL_THIS_THING + thingId, creation).apply();
        Intent intent = getIntent();
        intent.setClass(this, DayDetailActivity.class);
        startActivity(intent);
    }


    private void setupActionBar() {
        // Inflate your custom layout
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.search_actionbar, null);

        // Set up ActionBar
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);

        etxSearch = actionBarLayout.findViewById(R.id.action_bar_edittext);
    }
}
