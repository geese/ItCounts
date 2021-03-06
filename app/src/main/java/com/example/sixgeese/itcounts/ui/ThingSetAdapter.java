package com.example.sixgeese.itcounts.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.sixgeese.itcounts.DayDetailActivity;
import com.example.sixgeese.itcounts.R;
import com.example.sixgeese.itcounts.model.ThingSet;
import com.example.sixgeese.itcounts.utility.DecrementRepsListener;
import com.example.sixgeese.itcounts.utility.IncrementRepsListener;
import com.example.sixgeese.itcounts.utility.ItemTouchHelperAdapter;
import com.example.sixgeese.itcounts.utility.OnStartDragListener;
import com.example.sixgeese.itcounts.utility.ThingSetTextWatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sixge on 4/17/2018.
 */



public class ThingSetAdapter extends RecyclerView.Adapter<ThingSetAdapter.ThingSetViewHolder>
                                                            implements ItemTouchHelperAdapter {

    private static final String TAG = ThingSetAdapter.class.getSimpleName();

    public static final String THIS_IS_AN_EDIT_TEXT = "this is an EditText";

    private Context context;
    private SharedPreferences prefs;
    private OnStartDragListener startDragListener;
    private ArrayList<ThingSet> thingSets;
    private ArrayList<ThingSet> selectedSets;

    private int date, thingMonthId;
    private boolean multiSelect = false;  // used for CAB (Contextual Action Bar)


    // for CAB
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
            thingSets.removeAll(selectedSets);
            ((DayDetailActivity)context).deleteThingSets(selectedSets);

            mode.finish();
            /*for(int i = 0; i < thingSets.size(); i++){
                thingSets.get(i).setOrdinalPosition(i);
            }*/
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
            ((DayDetailActivity)context).noticeMultiSelect(multiSelect); // sets EditTexts back to enabled=true
            selectedSets.clear();
            if (thingSets.isEmpty()){
                addBlankThingSet();
            }
            notifyDataSetChanged();
        }
    };



    public ThingSetAdapter(ArrayList<ThingSet> thingSets, Context context, int thingMonthId, int date
                           /*,OnStartDragListener startDragListener*/){
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        selectedSets = new ArrayList<>();
        this.startDragListener = (OnStartDragListener)context;

        this.context = context;
        this.thingSets= thingSets;
        this.thingMonthId = thingMonthId;
        this.date = date;

        // this way there's always one waiting to be edited
        if (thingSets.isEmpty()){
            addBlankThingSet();
        }
    }

    private void addBlankThingSet() {
        ThingSet newSet = new ThingSet(-1, -1, date); // needs to carry the date info
        newSet.setThingMonthId(thingMonthId);
        newSet.setOrdinalPosition(thingSets.size());
        //prefs.edit().putInt(thingMonthId + "position_0", 0).apply();
        thingSets.add(newSet);
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

        ThingSetViewHolder viewHolder = new ThingSetViewHolder(thingSetView);
        return viewHolder;
    }

    //https://stackoverflow.com/questions/33176336/need-an-example-about-recyclerview-adapter-notifyitemchangedint-position-objec/38796098
    // Update only part of ViewHolder that you are interested in
    // Invoked before onBindViewHolder(ViewHolder holder, int position)
    @Override
    public void onBindViewHolder(ThingSetViewHolder holder, int position, List<Object> payloads) {
        if(!payloads.isEmpty()) {
            if (payloads.get(0) instanceof Integer) {
                holder.txvSetNumber.setText(String.valueOf(position + 1) + ".");
                thingSets.get(position).setOrdinalPosition(position);
            }
        }else {
            super.onBindViewHolder(holder,position, payloads);
        }
    }

    //https://stackoverflow.com/questions/37915677/how-to-get-the-edit-text-position-from-recycler-view-adapter-using-text-watcher
    @Override
    public void onBindViewHolder(final ThingSetViewHolder holder, int position) {

        if (position == thingSets.size()){
            holder.addSetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addBlankThingSet();
                    notifyItemInserted(thingSets.size()+1);
                }
            });
        } else {
            ThingSet theSet = thingSets.get(position);
            theSet.setOrdinalPosition(position);
            holder.txvSetNumber.setText(context.getString(R.string.set_number, new Object[]{position + 1}));
            //holder.txvSetNumber.setTag(theSet.getId());
            holder.etxNumReps.setTag(THIS_IS_AN_EDIT_TEXT);
            holder.etxNumReps.setText(String.valueOf(theSet.getReps()));
            holder.etxNumReps.setSelectAllOnFocus(true);
            holder.etxNumReps.addTextChangedListener(new ThingSetTextWatcher(holder.etxNumReps, theSet, thingMonthId, position, prefs));

            setClearZeroTextOnFocus(holder);

            holder.addRepsButton.setOnClickListener(new IncrementRepsListener(holder.etxNumReps, theSet));
            holder.subRepsButton.setOnClickListener(new DecrementRepsListener(holder.etxNumReps, theSet));

            holder.update(theSet);

            holder.handle.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getActionMasked() ==
                            MotionEvent.ACTION_DOWN) {
                        startDragListener.onStartDrag(holder);
                    }
                    return false;
                }
            });
        }

    }

    private void setClearZeroTextOnFocus(ThingSetViewHolder holder) {
        holder.etxNumReps.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                String repsString = ((EditText)view).getText().toString();
                if (hasFocus){
                    if (repsString.isEmpty() || Integer.valueOf(repsString) == 0){
                        ((EditText)view).setText("");
                    }
                } else { //lost focus
                    if (repsString.isEmpty()){
                        ((EditText)view).setText("0");
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return thingSets.size() + 1;  //plus 1 is because there is an extra view at the end for the addSet button
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        //the very last position is off limits because it's the Add Another Set button
        if (toPosition == thingSets.size()){
            toPosition--;
        }

        ThingSet fromSet = thingSets.get(fromPosition);
        ThingSet toSet = thingSets.get(toPosition);

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(thingSets, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(thingSets, i, i - 1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);
        notifyItemChanged(fromPosition, new Integer(0));
        notifyItemChanged(toPosition, new Integer(0));
        return true;
    }



    public class ThingSetViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        CardView cardView;
        Button addSetButton;
        ImageButton subRepsButton, addRepsButton;
        ImageView handle;
        TextView txvSetNumber;
        EditText etxNumReps;


        public ThingSetViewHolder(View itemView) {
            super(itemView);
            handle = itemView.findViewById(R.id.thingSetDragHandle);
            addSetButton = itemView.findViewById(R.id.btn_addSet);
            cardView = itemView.findViewById(R.id.dayDetailSetLayout);
            linearLayout = itemView.findViewById(R.id.dayDetailSetItem_LinearLayout);
            txvSetNumber = itemView.findViewById(R.id.set_number);
            etxNumReps = itemView.findViewById(R.id.etx_numReps);
            subRepsButton = itemView.findViewById(R.id.btn_subtractReps);
            addRepsButton = itemView.findViewById(R.id.btn_addReps);
        }

        void update(final ThingSet thingSet) {

            Log.d(TAG, "update: " + "\nReps: " + thingSet.getReps() + "\nitemView class: " + itemView.getClass().getSimpleName());

            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ((AppCompatActivity)view.getContext()).startSupportActionMode(actionModeCallbacks);
                    ((DayDetailActivity)context).noticeMultiSelect(multiSelect);  // disables EditTexts while in ActionMode
                    selectItem(thingSet);
                    return true;
                }
            });
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: selectedSets contains item: " + (selectedSets.contains(thingSet)));
                    Log.d(TAG, "onClick: clicked");
                    selectItem(thingSet);
                }
            });

            // ensures the shading makes sense after leaving action mode
            if (selectedSets.contains(thingSet)) {
                cardView.setBackgroundColor(Color.LTGRAY);
            } else {
                cardView.setBackgroundColor(Color.WHITE);
            }
        }

        void selectItem(ThingSet item) {
            Log.d(TAG, "selectItem: ");
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
