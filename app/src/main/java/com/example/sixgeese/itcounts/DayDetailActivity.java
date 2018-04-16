package com.example.sixgeese.itcounts;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.zip.Inflater;

public class DayDetailActivity extends AppCompatActivity {

    public static final String KEY_DAY_DETAIL_TITLE = "day_detail_title";
    public static final String KEY_DAY_DETAIL_DATE = "day_detail_date";

    TextView dayDetailTitle, dayDetailDate;
    LinearLayout dayDetailSetHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detail);

        dayDetailTitle = findViewById(R.id.dayDetailTitle);
        dayDetailTitle.setText(getIntent().getStringExtra(KEY_DAY_DETAIL_TITLE));

        dayDetailDate = findViewById(R.id.dayDetailDate);
        dayDetailDate.setText(getIntent().getStringExtra(KEY_DAY_DETAIL_DATE));

        dayDetailSetHolder = findViewById(R.id.dayDetailSetHolder);

        LayoutInflater inflater = getLayoutInflater();
        View setRow = inflater.inflate(R.layout.day_detail_set, dayDetailSetHolder);



    }
}
