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

import com.example.sixgeese.itcounts.ui.SetLabelsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


//https://stackoverflow.com/questions/6867076/getactionbar-returns-null
//https://stackoverflow.com/questions/27654703/put-edittext-to-action-bar-instead-of-title
//https://stackoverflow.com/questions/12276027/how-can-i-return-to-a-parent-activity-correctly

public class SetLabelsActivity extends AppCompatActivity {
    public static final String TAG = SetLabelsActivity.class.getSimpleName();
    
    SharedPreferences prefs;
    RecyclerView setLabelsRecyclerView;
    SetLabelsAdapter adapter;
    ArrayList<String> labels;
    EditText etxSearch;
    int thingId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_labels);
        setupActionBar();

        thingId = getIntent().getIntExtra(DayDetailActivity.KEY_THING_ID, -1);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        setLabelsRecyclerView = findViewById(R.id.set_labels_recyclerview);
        setLabelsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        etxSearch.setText("");
        getSupportActionBar().getCustomView().findViewById(R.id.mainLayout).requestFocus();
        Set labelSet = prefs.getStringSet(DayDetailActivity.KEY_SETLABELS, null);
        labels = new ArrayList(labelSet);
        Collections.sort(labels);
        adapter = new SetLabelsAdapter(this, labels, etxSearch);
        setLabelsRecyclerView.setAdapter(adapter);
    }

    public void selectSetLabel(String selection) {
        prefs.edit().putString(DayDetailActivity.KEY_SETSLABEL_THIS_THING + thingId, selection).apply();
        startActivity(getIntent().setClass(this, DayDetailActivity.class));
    }

    public void createSetLabel(String creation) {
        Set setLabels = prefs.getStringSet(DayDetailActivity.KEY_SETLABELS, new HashSet());
        setLabels.add(creation);
        prefs.edit().putStringSet(DayDetailActivity.KEY_SETLABELS, setLabels).apply();
        prefs.edit().putString(DayDetailActivity.KEY_SETSLABEL_THIS_THING + thingId, creation).apply();
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
                    .putExtra(EditLabelsActivity.KEY_EXTRA_LABEL_TYPE, EditLabelsActivity.KEY_EXTRA_LABEL_TYPE_SET)
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



/* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == EditLabelsActivity.RESULT_SET) {
            if(resultCode == Activity.RESULT_OK){
                labels = data.getStringArrayListExtra(DayDetailActivity.KEY_STRING_ARRAYLIST_EXTRA_LABELS);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult*/