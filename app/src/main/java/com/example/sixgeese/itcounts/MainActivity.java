package com.example.sixgeese.itcounts;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;

import com.example.sixgeese.itcounts.model.ThingMonth;
import com.example.sixgeese.itcounts.ui.ThingMonthAdapter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ArrayList<ThingMonth> thingMonths;
    RecyclerView recyclerView;
    ThingMonthAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initThingMonths();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ThingMonthAdapter(thingMonths, getApplicationContext());
        LinearLayoutManager horizLayoutMgr =
                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizLayoutMgr);
        recyclerView.setAdapter(adapter);

        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(recyclerView);


        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 2, 1);
        SimpleDateFormat format = new SimpleDateFormat("MMMM");
        Date theDate = calendar.getTime();
        Log.d("calendar", format.format(theDate));
        Log.d("Calendar", calendar.getActualMaximum(Calendar.DATE)+"");
        Log.d("Calendar", calendar.get(Calendar.WEEK_OF_MONTH)+"");
        Log.d("Calendar", "First day of week: " + calendar.get(Calendar.DAY_OF_WEEK));
    }

    private void initThingMonths() {
        thingMonths = new ArrayList<>();
        thingMonths.add(new ThingMonth("Take Vitamins", 2017, 10));
        thingMonths.add(new ThingMonth("Take Vitamins", 2017, 11));
        thingMonths.add(new ThingMonth("Take Vitamins", 2017, 12));
        thingMonths.add(new ThingMonth("Take Vitamins", 2018, 1));
        thingMonths.add(new ThingMonth("Take Vitamins", 2018, 2));
        thingMonths.add(new ThingMonth("Take Vitamins", 2018, 3));

        thingMonths.get(0).addThingSet(4).setReps(2);
        thingMonths.get(0).addThingSet(4).setReps(6);
        thingMonths.get(0).addThingSet(6).setReps(3);
    }
}
