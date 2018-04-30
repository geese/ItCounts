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

import java.util.ArrayList;

/**
 * Created by sixge on 4/29/2018.
 */

//https://www.simplifiedcoding.net/search-functionality-recyclerview/#Implementing-Search-Functionality-in-RecyclerView

public class SetLabelsAdapter extends RecyclerView.Adapter<SetLabelsAdapter.SetLabelsViewHolder> {
    private static final String TAG = SetLabelsAdapter.class.getSimpleName();

    Context context;
    ArrayList<String> labels;
    ArrayList<String> origLabels;
    ArrayList<String> origLabelsLower;
    EditText etxSearch;
    String createLabelText = "";
    ViewGroup.LayoutParams regularLayoutParams;

    //https://stackoverflow.com/questions/40754174/android-implementing-search-filter-to-a-recyclerview
    public SetLabelsAdapter(Context context, ArrayList<String> labels, EditText etxSearch) {
        this.context = context;
        this.etxSearch = etxSearch;
        this.labels = labels;
        this.origLabels = labels;
        this.origLabelsLower = new ArrayList<>();
        for (String label : labels) {
            origLabelsLower.add(label.toLowerCase());
        }

        etxSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // filter your list from your input
                filter(editable.toString());
                doCreateLabel(editable.toString());
            }
        });
    }

    private void doCreateLabel(String searchText) {
        createLabelText = searchText;
        if (!origLabelsLower.contains(searchText.toLowerCase())) {
            Log.d(TAG, "doCreateLabel: nope");
            notifyItemChanged(0);
        } else {
            Log.d(TAG, "doCreateLabel: yep");
        }
    }

    void filter(String searchText){
        ArrayList<String> temp = new ArrayList();
        for(String label: origLabels){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(label.toLowerCase().contains(searchText.toLowerCase())){
                temp.add(label);
            }
        }
        //update recyclerview
        updateList(temp);
    }

    public void updateList(ArrayList<String> filteredLabels){
        this.labels = filteredLabels;
        notifyDataSetChanged();
    }


    @Override
    public SetLabelsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View labelView;

        if(viewType == R.layout.create_set_label_item){
            labelView = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_set_label_item, parent, false);
        } else {
            labelView = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_label_item, parent, false);
        }

        SetLabelsViewHolder viewHolder = new SetLabelsViewHolder(labelView);
        return viewHolder;
    }

    //https://stackoverflow.com/questions/29106484/how-to-add-a-button-at-the-end-of-recyclerview
    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? R.layout.create_set_label_item : R.layout.set_label_item;
    }

    /*@Override
    public void onBindViewHolder(SetLabelsViewHolder holder, int position, List<Object> payloads) {
        if(!payloads.isEmpty()) {

            holder.txvCreateSetLabel.setText(context.getString(R.string.create_label));

        }else {
            super.onBindViewHolder(holder,position, payloads);
        }
    }*/

    @Override
    public void onBindViewHolder(SetLabelsAdapter.SetLabelsViewHolder holder, int position) {
        if (position > 0){
            regularLayoutParams = holder.itemContainer.getLayoutParams();
            holder.txvSetLabel.setText(labels.get(position - 1)); // minus one is an offset because of Create Label at position zero.
        } else {
            if (!createLabelText.isEmpty()){
                holder.itemContainer_create.setLayoutParams(regularLayoutParams);
            } else {
                holder.itemContainer_create.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
            holder.txvCreateSetLabel.setText(context.getString(R.string.create_label, createLabelText));
        }
    }

    @Override
    public int getItemCount() {
        return labels.size() + 1;  // plus 1 is for the extra item "Create Label" at position 0
    }



    public class SetLabelsViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout itemContainer_create;
        private LinearLayout itemContainer;
        private TextView txvSetLabel, txvCreateSetLabel;
        private ImageView imgvAddSetLabel;

        public SetLabelsViewHolder(View itemView) {
            super(itemView);
            itemContainer = itemView.findViewById(R.id.set_label_item_container);
            itemContainer_create = itemView.findViewById(R.id.create_set_label_container);
            txvSetLabel = itemView.findViewById(R.id.a_set_label);
            txvCreateSetLabel = itemView.findViewById(R.id.create_set_label);
            imgvAddSetLabel = itemView.findViewById(R.id.add_set_label);
        }
    }
}
