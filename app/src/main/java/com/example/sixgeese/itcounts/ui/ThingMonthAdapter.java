package com.example.sixgeese.itcounts.ui;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sixgeese.itcounts.R;
import com.example.sixgeese.itcounts.model.ThingMonth;
import com.example.sixgeese.itcounts.model.ThingSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by sixge on 3/22/2018.
 */

public class ThingMonthAdapter extends RecyclerView.Adapter<ThingMonthAdapter.ThingMonthViewHolder> {

    private ArrayList<ThingMonth> thingMonths;
    Context context;

    public ThingMonthAdapter(ArrayList<ThingMonth> thingMonths, Context context){
        this.thingMonths= thingMonths;
        this.context = context;
    }

    @Override
    public ThingMonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View monthView = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_month, parent, false);
        ThingMonthViewHolder viewHolder = new ThingMonthViewHolder(monthView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ThingMonthViewHolder holder, int position) {
        ThingMonth thingMonth = thingMonths.get(position);
        int monthNum = thingMonth.getMonth();
        int year = thingMonth.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthNum, 1);
        String monthName = new SimpleDateFormat("MMMM").format(calendar.getTime());

        holder.monthTxv.setText(monthName);
        holder.yearTxv.setText(String.valueOf(year));

        int theDate = 1;
        int numDaysInMonth = calendar.getActualMaximum(Calendar.DATE) - 1;
        int firstDayIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int lastDayIndex = firstDayIndex + numDaysInMonth;

        for (int squareIndex = 0; squareIndex < 42; squareIndex++){
            CardView repsCard = holder.days[squareIndex].findViewById(R.id.cardView);
            repsCard.setVisibility(View.INVISIBLE);
            if (squareIndex < firstDayIndex || squareIndex > lastDayIndex){
                holder.days[squareIndex].setVisibility(View.INVISIBLE);
            }else{
                holder.days[squareIndex].setVisibility(View.VISIBLE);
                TextView day = holder.days[squareIndex].findViewById(R.id.date);
                day.setText(String.valueOf(theDate));
                if (thingMonth.getTotalReps(theDate) > 0) {
                    repsCard.setVisibility(View.VISIBLE);
                    TextView totalReps = repsCard.findViewById(R.id.totalReps);
                    totalReps.setText(String.valueOf(thingMonth.getTotalReps(theDate)));
                }
                theDate++;
            }
        }
    }


    @Override
    public int getItemCount() {
        return thingMonths.size();
    }

    public class ThingMonthViewHolder extends RecyclerView.ViewHolder{

        private TextView titleTxv, monthTxv, yearTxv;
        private LinearLayout week_1, week_2, week_3, week_4, week_5, week_6;
        private LinearLayout[] weeks;
        private ConstraintLayout[] days;

        public ThingMonthViewHolder(View itemView) {
            super(itemView);
            monthTxv = itemView.findViewById(R.id.theMonth);
            yearTxv = itemView.findViewById(R.id.theYear);
            initWeeksAndDays(itemView);

        }

        private void initWeeksAndDays(View view) {
            weeks = new LinearLayout[]{
                    view.findViewById(R.id.week_1),
                    view.findViewById(R.id.week_2),
                    view.findViewById(R.id.week_3),
                    view.findViewById(R.id.week_4),
                    view.findViewById(R.id.week_5),
                    view.findViewById(R.id.week_6)
            };
            days = new ConstraintLayout[42];
            int squareIndex = 0;
            while(squareIndex < 42){
                for(int weekIndex = 0; weekIndex < 6; weekIndex++){
                    for(int weekdayIndex = 0; weekdayIndex < 7; weekdayIndex++){
                        days[squareIndex++] =
                                (ConstraintLayout)weeks[weekIndex].getChildAt(weekdayIndex);
                    }
                }
            }


        }
    }
}


