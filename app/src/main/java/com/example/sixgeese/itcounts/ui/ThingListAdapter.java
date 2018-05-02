package com.example.sixgeese.itcounts.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sixgeese.itcounts.CalendarActivity;
import com.example.sixgeese.itcounts.DayDetailActivity;
import com.example.sixgeese.itcounts.MainActivity;
import com.example.sixgeese.itcounts.R;
import com.example.sixgeese.itcounts.model.ThingDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by sixge on 3/22/2018.
 */

public class ThingListAdapter extends RecyclerView.Adapter<ThingListAdapter.ThingListViewHolder> {

    private static final String TAG = ThingListAdapter.class.getSimpleName();

    private ArrayList<ArrayList<ThingDay>> thingsWithDays;
    Context context;

    public ThingListAdapter(ArrayList<ArrayList<ThingDay>> thingsWithDays, Context context){
        this.thingsWithDays= thingsWithDays;
        this.context = context;
    }

    @Override
    public ThingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View thinglistView = LayoutInflater.from(parent.getContext()).inflate(R.layout.thing_list_item, parent, false);
        ThingListViewHolder viewHolder = new ThingListViewHolder(thinglistView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ThingListViewHolder holder, int position) {
        ArrayList<ThingDay> thingDays = thingsWithDays.get(position);
        final String title = thingDays.get(0).getTitle();

        holder.thingListTitle.setText(title);


        for (ThingDay thingDay : thingDays) {

            if (!thingDay.getThingSets().isEmpty()){
                createTextView(holder, thingDay);
            } else {
                createImageView(holder, thingDay);
            }
        }

        holder.thingListTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CalendarActivity.class);
                intent.putExtra(MainActivity.KEY_THING_TITLE, title);
                context.startActivity(intent);
            }
        });
    }

    private void createImageView(ThingListViewHolder holder, final ThingDay thingDay) {
        //ImageView newImageView = new ImageView(context);
        ImageButton newImageView = new ImageButton(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        params.gravity = Gravity.CENTER;
        newImageView.setLayoutParams(params);
        newImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_black_12dp));
        newImageView.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        holder.root.addView(newImageView);

        newImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                goToDayDetailActivity(thingDay);
                return true;
            }
        });
    }



    private void createTextView(ThingListViewHolder holder, final ThingDay thingDay) {
        TextView newTextView = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        newTextView.setLayoutParams(params);
        newTextView.setGravity(Gravity.CENTER);
        newTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        newTextView.setSingleLine(true);
        newTextView.setEllipsize(TextUtils.TruncateAt.END);
        newTextView.setText(String.valueOf(thingDay.getTotalReps()));
        holder.root.addView(newTextView);

        newTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                goToDayDetailActivity(thingDay);
                return true;
            }
        });
    }

    private void goToDayDetailActivity(ThingDay thingDay) {
        Calendar cal = Calendar.getInstance();
        cal.set(thingDay.getYear(), thingDay.getMonth(), thingDay.getDate());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);

        String dateString =
                cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
                        + ", "
                        + cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                        + " " + date
                        + ", " + year;

        Intent intent = new Intent(context, DayDetailActivity.class);
        intent.putExtra(DayDetailActivity.KEY_DAY_DETAIL_TITLE, thingDay.getTitle());
        intent.putExtra(DayDetailActivity.KEY_DAY_DETAIL_DATE_STRING, dateString);
        intent.putExtra(DayDetailActivity.KEY_DAY_DETAIL_YEAR, year);
        intent.putExtra(DayDetailActivity.KEY_DAY_DETAIL_MONTH, month);
        intent.putExtra(DayDetailActivity.KEY_DAY_DETAIL_DATE, date);
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return thingsWithDays.size();
    }

    public class ThingListViewHolder extends RecyclerView.ViewHolder{

        private TextView thingListTitle;
        private LinearLayout root;

        public ThingListViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.list_thing_item);
            thingListTitle = itemView.findViewById(R.id.list_thing_title);
        }
    }
}


