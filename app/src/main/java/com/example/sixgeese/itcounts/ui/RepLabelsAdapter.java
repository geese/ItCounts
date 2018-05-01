package com.example.sixgeese.itcounts.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sixgeese.itcounts.R;
import com.example.sixgeese.itcounts.RepLabelsActivity;

import java.util.ArrayList;

/**
 * Created by sixge on 4/29/2018.
 */

//https://www.simplifiedcoding.net/search-functionality-recyclerview/#Implementing-Search-Functionality-in-RecyclerView

public class RepLabelsAdapter extends RecyclerView.Adapter<RepLabelsAdapter.RepLabelsViewHolder> {
    private static final String TAG = RepLabelsAdapter.class.getSimpleName();

    Context context;
    ArrayList<String> labels;
    ArrayList<String> origLabels;
    ArrayList<String> origLabelsLower;
    EditText etxSearch;
    String createLabelText = "";
    Boolean create = false;


    //https://stackoverflow.com/questions/40754174/android-implementing-search-filter-to-a-recyclerview
    public RepLabelsAdapter(RepLabelsActivity context, ArrayList<String> labels, EditText etxSearch) {
        this.context = context;
        this.etxSearch = etxSearch;
        this.labels = labels;
        this.origLabels = labels;
        this.origLabelsLower = new ArrayList<>();
        Log.d(TAG, "RepLabelsAdapter: origLabels contains \"\" = " + origLabelsLower.contains(""));
        for (String label : labels) {
            origLabelsLower.add(label.toLowerCase());
        }

        etxSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                doCreateLabel(editable.toString());
                filter(editable.toString());
            }
        });
    }

    private void doCreateLabel(String searchText) {
        createLabelText = searchText;
        if (!searchText.isEmpty()) {
            if (!origLabelsLower.contains(searchText.toLowerCase())) { //yes, create
                create = true;
                notifyItemInserted(0);
            } else {  // now the search text spells out an entire existing label
                create = false;
                notifyItemRemoved(0);
            }
        }else { // plain search, no create
            if (create){
                notifyItemRemoved(0);
            }
            create = false;
        }
    }

    void filter(String searchText){
        ArrayList<String> temp = new ArrayList();
        Log.d(TAG, "filter: create = " + create);
        for(String label: origLabels){
            if(label.toLowerCase().contains(searchText.toLowerCase())){
                temp.add(label);
            }
        }
        updateList(temp);
    }

    public void updateList(ArrayList<String> filteredLabels){
        this.labels = filteredLabels;
        notifyDataSetChanged();
    }


    @Override
    public RepLabelsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View labelView;

        if(viewType == R.layout.create_rep_label_item){
            labelView = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_rep_label_item, parent, false);
        } else {
            labelView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rep_label_item, parent, false);
        }

        RepLabelsViewHolder viewHolder = new RepLabelsViewHolder(labelView, context);
        return viewHolder;
    }

    //https://stackoverflow.com/questions/29106484/how-to-add-a-button-at-the-end-of-recyclerview
    @Override
    public int getItemViewType(int position) {
        return (create && position == 0) ? R.layout.create_rep_label_item : R.layout.rep_label_item;
    }


    @Override
    public void onBindViewHolder(RepLabelsAdapter.RepLabelsViewHolder holder, int position) {
        if (!create){
            holder.txvRepLabel.setText(labels.get(position));
        } else {
            if (position == 0){
                holder.txvCreateRepLabel.setText(context.getString(R.string.create_label, createLabelText));
            } else {
                holder.txvRepLabel.setText(labels.get(position-1));// minus one is an offset because of Create Label at position zero.
            }
        }
    }

    @Override
    public int getItemCount() {
        if (create){
            return labels.size() + 1;  // plus 1 is for the extra item "Create Label" at position 0
        } else {
            return labels.size();
        }
    }



    public class RepLabelsViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout itemContainer_create;
        private LinearLayout itemContainer;
        private TextView txvRepLabel, txvCreateRepLabel;
        private ImageView imgvAddRepLabel;

        public RepLabelsViewHolder(View itemView, final Context context) {
            super(itemView);
            itemContainer = itemView.findViewById(R.id.rep_label_item_container);
            itemContainer_create = itemView.findViewById(R.id.create_rep_label_container);
            txvRepLabel = itemView.findViewById(R.id.a_rep_label);
            txvCreateRepLabel = itemView.findViewById(R.id.create_rep_label);
            imgvAddRepLabel = itemView.findViewById(R.id.add_rep_label);

            if (txvRepLabel != null) {
                txvRepLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((RepLabelsActivity) context).selectRepLabel(txvRepLabel.getText().toString());
                    }
                });
            }

            setCreateOnClickListener(txvCreateRepLabel);
            setCreateOnClickListener(imgvAddRepLabel);

        }

        private void setCreateOnClickListener(View view) {
            if (view != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createRepLabel((RepLabelsActivity) context);
                    }
                });
            }

        }

        //TODO: handle case where the new label contains quotation marks
        private void createRepLabel(RepLabelsActivity context) {
            String[] split = txvCreateRepLabel.getText().toString().split("\"");
            context.createRepLabel(split[split.length-1].trim());
        }
    }
}
