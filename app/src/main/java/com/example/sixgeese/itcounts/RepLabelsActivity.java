package com.example.sixgeese.itcounts;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.sixgeese.itcounts.ui.RepLabelsAdapter;
import com.example.sixgeese.itcounts.ui.SetLabelsAdapter;

import java.util.ArrayList;
import java.util.Collections;
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

        repLabelsRecyclerView = findViewById(R.id.rep_labels_recyclerview);
        repLabelsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        etxSearch.setText("");
        getSupportActionBar().getCustomView().findViewById(R.id.mainLayout).requestFocus();
        Set labelSet = prefs.getStringSet(DayDetailActivity.KEY_REPLABELS, null);
        labels = new ArrayList(labelSet);
        Collections.sort(labels);
        adapter = new RepLabelsAdapter(this, labels, etxSearch);
        repLabelsRecyclerView.setAdapter(adapter);
    }

    public void selectRepLabel(String selection) {
        prefs.edit().putString(DayDetailActivity.KEY_REPSLABEL_THIS_THING + thingId, selection).apply();
        startActivity(getIntent().setClass(this, DayDetailActivity.class));

    }
    public void createRepLabel(String creation) {
        Set repLabels = prefs.getStringSet(DayDetailActivity.KEY_REPLABELS, new HashSet());
        repLabels.add(creation);
        prefs.edit().putStringSet(DayDetailActivity.KEY_REPLABELS, repLabels).apply();
        prefs.edit().putString(DayDetailActivity.KEY_REPSLABEL_THIS_THING + thingId, creation).apply();
        startActivity(getIntent().setClass(this, DayDetailActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                //startActivity(getIntent().setClass(this, DayDetailActivity.class));
                return true;
            case R.id.edit_menu_item:
                startActivity(getIntent().setClass(this, EditLabelsActivity.class)
                        .putExtra(EditLabelsActivity.KEY_EXTRA_LABEL_TYPE, EditLabelsActivity.KEY_EXTRA_LABEL_TYPE_REP)
                        .putExtra(DayDetailActivity.KEY_STRING_ARRAYLIST_EXTRA_LABELS, labels));
                return true;
        }
        return (true);
    }


    private void setupActionBar() {
        // Inflate your custom layout
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.search_actionbar, null);

        // Set up ActionBar
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);

        etxSearch = actionBarLayout.findViewById(R.id.action_bar_edittext);
    }
}
