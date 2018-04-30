package com.example.sixgeese.itcounts;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.sixgeese.itcounts.ui.SetLabelsAdapter;

import java.util.ArrayList;


//https://stackoverflow.com/questions/6867076/getactionbar-returns-null
//https://stackoverflow.com/questions/27654703/put-edittext-to-action-bar-instead-of-title
//https://stackoverflow.com/questions/12276027/how-can-i-return-to-a-parent-activity-correctly

public class SetLabelsActivity extends AppCompatActivity {

    SharedPreferences prefs;
    RecyclerView setLabelsRecyclerView;
    SetLabelsAdapter adapter;
    ArrayList<String> labels;
    EditText etxSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_labels);
        setupActionBar();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        labels = getIntent().getStringArrayListExtra(DayDetailActivity.KEY_STRING_ARRAYLIST_EXTRA_LABELS);

        setLabelsRecyclerView = findViewById(R.id.set_labels_recyclerview);
        setLabelsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SetLabelsAdapter(this, labels, etxSearch);
        setLabelsRecyclerView.setAdapter(adapter);
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
