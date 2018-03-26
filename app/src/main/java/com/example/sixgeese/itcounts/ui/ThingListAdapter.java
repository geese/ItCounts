package com.example.sixgeese.itcounts.ui;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sixgeese.itcounts.CalendarActivity;
import com.example.sixgeese.itcounts.MainActivity;
import com.example.sixgeese.itcounts.R;
import com.example.sixgeese.itcounts.model.ThingDay;
import com.example.sixgeese.itcounts.model.ThingMonth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
        /*for (int i = 0; i < holder.imageViews.length; i++) {
            holder.imageViews[i].setVisibility(View.GONE);
            //holder.textViews[i].setVisibility(View.GONE);
        }*/

        for (ThingDay thingDay : thingDays) {

            if (!thingDay.getThingSets().isEmpty()){
                TextView newTextView = new TextView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.weight = 1;
                params.gravity = Gravity.CENTER;
                newTextView.setLayoutParams(params);
                newTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                newTextView.setText(String.valueOf(thingDay.getTotalReps()));
                holder.root.addView(newTextView);
            } else {
                ImageView newImageView = new ImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.weight = 1;
                params.gravity = Gravity.CENTER;
                newImageView.setLayoutParams(params);
                newImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_black_12dp));
                holder.root.addView(newImageView);
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


    @Override
    public int getItemCount() {
        return thingsWithDays.size();
    }

    public class ThingListViewHolder extends RecyclerView.ViewHolder{

        private TextView thingListTitle;
        private LinearLayout root;
        /*private ImageView imageView_0, imageView_1, imageView_2, imageView_3;
        private TextView textView_0, textView_1, textView_2, textView_3;
        private ImageView[] imageViews = new ImageView[4];
        private TextView[] textViews = new TextView[4];*/

        public ThingListViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.list_thing_item);
            thingListTitle = itemView.findViewById(R.id.list_thing_title);

            /*imageView_0 = itemView.findViewById(R.id.imageview_0);
            imageView_1 = itemView.findViewById(R.id.imageview_1);
            imageView_2 = itemView.findViewById(R.id.imageview_2);
            imageView_3 = itemView.findViewById(R.id.imageview_3);

            textView_0 = itemView.findViewById(R.id.textview_0);
            textView_1 = itemView.findViewById(R.id.textview_1);
            textView_2 = itemView.findViewById(R.id.textview_2);
            textView_3 = itemView.findViewById(R.id.textview_3);

            imageViews[0] = imageView_0;
            imageViews[1] = imageView_1;
            imageViews[2] = imageView_2;
            imageViews[3] = imageView_3;

            textViews[0] = textView_0;
            textViews[1] = textView_1;
            textViews[2] = textView_2;
            textViews[3] = textView_3;*/
        }
    }
}


