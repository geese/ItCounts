package com.example.sixgeese.itcounts.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sixgeese.itcounts.DayDetailActivity;
import com.example.sixgeese.itcounts.R;
import com.example.sixgeese.itcounts.model.ThingSet;
import com.example.sixgeese.itcounts.utility.ThingSetTextWatcher;

import java.util.ArrayList;
import java.util.function.ToDoubleBiFunction;

/**
 * Created by sixge on 4/17/2018.
 */



public class ThingSetAdapter extends RecyclerView.Adapter<ThingSetAdapter.ThingSetViewHolder> {
    private static final String TAG = ThingSetAdapter.class.getSimpleName();

    private ArrayList<ThingSet> thingSets;
    private int thingMonthId;
    private int date;

    Context context;
    SharedPreferences prefs;

    public ThingSetAdapter(ArrayList<ThingSet> thingSets, Context context, int thingMonthId, int date){
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
        this.thingSets= thingSets;
        this.thingMonthId = thingMonthId;
        this.date = date;

        // this way there's always one waiting to be edited
        if (thingSets.isEmpty()){
            ThingSet newSet = new ThingSet(-1, -1, date);
            newSet.setThingMonthId(thingMonthId);
            newSet.setOrdinalPosition(thingSets.size());
            thingSets.add(newSet);
        }
    }


    //https://stackoverflow.com/questions/29106484/how-to-add-a-button-at-the-end-of-recyclerview
    @Override
    public int getItemViewType(int position) {
        return (position == thingSets.size()) ? R.layout.add_set_button : R.layout.day_detail_set;
    }


    @Override
    public ThingSetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View thingSetView;

        if(viewType == R.layout.add_set_button){
            thingSetView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_set_button, parent, false);
        } else {
            thingSetView = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_detail_set, parent, false);
        }

        ThingSetViewHolder viewHolder = new ThingSetViewHolder(thingSetView, this);
        return viewHolder;
    }


    //https://stackoverflow.com/questions/37915677/how-to-get-the-edit-text-position-from-recycler-view-adapter-using-text-watcher
    @Override
    public void onBindViewHolder(ThingSetViewHolder holder, int position) {
        Log.d("dash", "onBindViewHolder: ");
        if (position == thingSets.size()){
            holder.addSetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ThingSet newSet = new ThingSet(-1, -1, date);
                    newSet.setThingMonthId(thingMonthId);
                    newSet.setOrdinalPosition(thingSets.size());
                    thingSets.add(newSet);
                    notifyItemInserted(thingSets.size()+1);
                }
            });
        } else {
            ThingSet theSet = thingSets.get(position);
            holder.txvSetNumber.setText(context.getString(R.string.set_number, new Object[]{position + 1}));
            //holder.txvSetNumber.setTag(theSet.getId());
            holder.etxNumReps.setTag(position);
            holder.etxNumReps.setText(String.valueOf(theSet.getReps()));
            holder.etxNumReps.addTextChangedListener(new ThingSetTextWatcher(
                    holder.etxNumReps, theSet, thingMonthId, position));
            Log.d(TAG, "onBindViewHolder: Ordinal Position: " + theSet.getOrdinalPosition());
        }
    }

    @Override
    public int getItemCount() {
        return thingSets.size() + 1;  //plus 1 is because there is an extra view at the end for the addSet button
    }

    public class ThingSetViewHolder extends RecyclerView.ViewHolder {

        private ThingSetAdapter parentAdapter;
        LinearLayout linearLayout;
        Button addSetButton;
        ImageButton subRepsButton, addRepsButton, deleteSetButton;
        TextView txvSetNumber;
        EditText etxNumReps;


        public ThingSetViewHolder(View itemView, ThingSetAdapter adapter) {
            super(itemView);
            parentAdapter = adapter;
            addSetButton = itemView.findViewById(R.id.btn_addSet);
            linearLayout = itemView.findViewById(R.id.dayDetailSetItem_LinearLayout);
            txvSetNumber = itemView.findViewById(R.id.set_number);
            etxNumReps = itemView.findViewById(R.id.etx_numReps);
            subRepsButton = itemView.findViewById(R.id.btn_subtractReps);
            addRepsButton = itemView.findViewById(R.id.btn_addReps);
            deleteSetButton = itemView.findViewById(R.id.btn_deleteSet);

            //etxNumReps.addTextChangedListener(new ThingSetTextWatcher(etxNumReps, txvSetNumber));
        }
    }
}
