package com.example.sixgeese.itcounts.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sixgeese.itcounts.R;
import com.example.sixgeese.itcounts.model.ThingSet;

import java.util.ArrayList;

/**
 * Created by sixge on 4/17/2018.
 */

public class ThingSetAdapter extends RecyclerView.Adapter<ThingSetAdapter.ThingSetViewHolder> {
    private static final String TAG = ThingSetAdapter.class.getSimpleName();

    private ArrayList<ThingSet> thingSets;
    protected Context context;

    public ThingSetAdapter(ArrayList<ThingSet> thingSets, Context context){
        this.thingSets= thingSets;
        this.context = context;
    }


    @Override
    public ThingSetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View thingSetView = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_detail_set, parent, false);
        ThingSetViewHolder viewHolder = new ThingSetViewHolder(thingSetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ThingSetViewHolder holder, int position) {
        ThingSet theSet = thingSets.get(position);
        holder.txvSetNumber.setText(context.getString(R.string.set_number, new Object[]{position + 1}));
        holder.etxNumReps.setText(String.valueOf(theSet.getReps()));
        holder.txvSetNumber.setTag(theSet.getId());
        Log.d(TAG, holder.txvSetNumber.getTag()+"");
    }

    @Override
    public int getItemCount() {
        return thingSets.size();
    }

    public class ThingSetViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        ImageButton subRepsButton, addRepsButton, deleteSetButton;
        TextView txvSetNumber;
        EditText etxNumReps;


        public ThingSetViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.dayDetailSetItem_LinearLayout);
            txvSetNumber = itemView.findViewById(R.id.set_number);
            etxNumReps = itemView.findViewById(R.id.etx_numReps);
            subRepsButton = itemView.findViewById(R.id.btn_subtractReps);
            addRepsButton = itemView.findViewById(R.id.btn_addReps);
            deleteSetButton = itemView.findViewById(R.id.btn_deleteSet);
        }
    }
}
