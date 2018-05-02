package com.example.sixgeese.itcounts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.sixgeese.itcounts.ui.EditLabelsAdapter;
import com.example.sixgeese.itcounts.ui.SetLabelsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



public class EditLabelsActivity extends AppCompatActivity {
    private static final String TAG = EditLabelsActivity.class.getSimpleName();

    public static final int ACTION_SAVE = 0;
    public static final int ACTION_DELETE = -1;

    public static final String KEY_EXTRA_LABEL_TYPE = "label_type";
    public static final String KEY_EXTRA_LABEL_TYPE_SET = "label_type_set";
    public static final String KEY_EXTRA_LABEL_TYPE_REP = "label_type_rep";

    SharedPreferences prefs;
    RecyclerView editLabelsRecyclerView;
    EditLabelsAdapter adapter;
    ArrayList<String> labels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_labels);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        labels = getIntent().getStringArrayListExtra(DayDetailActivity.KEY_STRING_ARRAYLIST_EXTRA_LABELS);

        editLabelsRecyclerView = findViewById(R.id.edit_labels_recyclerview);
        editLabelsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EditLabelsAdapter(this, labels);
        editLabelsRecyclerView.setAdapter(adapter);
    }

    public void updatePrefs(int position, String labelText, int action){
        switch(action){
            case ACTION_SAVE:
                labels.set(position, labelText);
                break;
            case ACTION_DELETE:
                labels.remove(position);
                int thingId = getIntent().getIntExtra(DayDetailActivity.KEY_THING_ID, -1);
                String typeOfLabelThisThing = (getIntent().getStringExtra(KEY_EXTRA_LABEL_TYPE).equals(KEY_EXTRA_LABEL_TYPE_SET)) ?
                        DayDetailActivity.KEY_SETSLABEL_THIS_THING : DayDetailActivity.KEY_REPSLABEL_THIS_THING;
                if (prefs.contains(typeOfLabelThisThing + thingId)){
                    Map<String,?> allEntries = prefs.getAll();
                    for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                        if (entry.getKey().startsWith(typeOfLabelThisThing)){
                            prefs.edit().remove(entry.getKey()).apply();
                        }
                    }
                }
                break;
        }
        Collections.sort(labels);
        adapter.notifyDataSetChanged();

        Set labelsSet = new HashSet(labels);

        switch(getIntent().getStringExtra(KEY_EXTRA_LABEL_TYPE)){
            case KEY_EXTRA_LABEL_TYPE_SET:
                prefs.edit().putStringSet(DayDetailActivity.KEY_SETLABELS, labelsSet).apply();
                break;
            case KEY_EXTRA_LABEL_TYPE_REP:
                prefs.edit().putStringSet(DayDetailActivity.KEY_REPLABELS, labelsSet).apply();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (true);
    }


}
