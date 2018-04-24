package com.example.sixgeese.itcounts.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

    private Context context;
    private SharedPreferences prefs;

    private int thingMonthId;
    private int date;

    private ArrayList<ThingSet> thingSets;
    private ArrayList<ThingSet> selectedSets;

    private boolean multiSelect = false;

    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            multiSelect = true;
            menu.add("Delete");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {return false;}

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            for (ThingSet thingSet : selectedSets) {
                selectedSets.remove(thingSet);
            }
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
            selectedSets.clear();
            notifyDataSetChanged();
        }
    };

    public ThingSetAdapter(ArrayList<ThingSet> thingSets, Context context, int thingMonthId, int date){
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        selectedSets = new ArrayList<>();

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
            holder.update(theSet);
            holder.txvSetNumber.setText(context.getString(R.string.set_number, new Object[]{position + 1}));
            //holder.txvSetNumber.setTag(theSet.getId());
            holder.etxNumReps.setTag(position);
            holder.etxNumReps.setText(String.valueOf(theSet.getReps()));
            holder.etxNumReps.addTextChangedListener(new ThingSetTextWatcher(
                    holder.etxNumReps, theSet, thingMonthId, position));
        }

    }

    @Override
    public int getItemCount() {
        return thingSets.size() + 1;  //plus 1 is because there is an extra view at the end for the addSet button
    }

    public class ThingSetViewHolder extends RecyclerView.ViewHolder {

        private ThingSetAdapter parentAdapter;
        LinearLayout linearLayout;
        CardView cardView;
        Button addSetButton;
        ImageButton subRepsButton, addRepsButton, deleteSetButton;
        TextView txvSetNumber;
        EditText etxNumReps;


        public ThingSetViewHolder(View itemView, ThingSetAdapter adapter) {
            super(itemView);
            parentAdapter = adapter;
            addSetButton = itemView.findViewById(R.id.btn_addSet);
            cardView = itemView.findViewById(R.id.dayDetailSetLayout);
            linearLayout = itemView.findViewById(R.id.dayDetailSetItem_LinearLayout);
            txvSetNumber = itemView.findViewById(R.id.set_number);
            etxNumReps = itemView.findViewById(R.id.etx_numReps);
            subRepsButton = itemView.findViewById(R.id.btn_subtractReps);
            addRepsButton = itemView.findViewById(R.id.btn_addReps);
            deleteSetButton = itemView.findViewById(R.id.btn_deleteSet);
        }

        void update(final ThingSet thingSet) {

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ((AppCompatActivity)view.getContext()).startSupportActionMode(actionModeCallbacks);
                    selectItem(thingSet);
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectItem(thingSet);
                }
            });

            if (selectedSets.contains(thingSet)) {
                cardView.setBackgroundColor(Color.LTGRAY);
            } else {
                cardView.setBackgroundColor(Color.WHITE);
            }
        }

        void selectItem(ThingSet item) {
            if (multiSelect) {
                if (selectedSets.contains(item)) {
                    selectedSets.remove(item);
                    cardView.setBackgroundColor(Color.WHITE);
                } else {
                    selectedSets.add(item);
                    cardView.setBackgroundColor(Color.LTGRAY);
                }
            }
        }
    }
}
