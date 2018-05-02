package com.example.sixgeese.itcounts.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sixgeese.itcounts.EditLabelsActivity;
import com.example.sixgeese.itcounts.R;
import com.example.sixgeese.itcounts.SetLabelsActivity;

import java.util.ArrayList;

/**
 * Created by sixge on 4/29/2018.
 */

//https://www.simplifiedcoding.net/search-functionality-recyclerview/#Implementing-Search-Functionality-in-RecyclerView

public class EditLabelsAdapter extends RecyclerView.Adapter<EditLabelsAdapter.EditLabelsViewHolder> {
    private static final String TAG = EditLabelsAdapter.class.getSimpleName();

    Context context;
    ArrayList<String> labels;


    public EditLabelsAdapter(EditLabelsActivity context, ArrayList<String> labels) {
        this.context = context;
        this.labels = labels;

    }



    @Override
    public EditLabelsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View labelView = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_label_item, parent, false);

        EditLabelsViewHolder viewHolder = new EditLabelsViewHolder(labelView, context);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.edit_label_item;
    }


    @Override
    public void onBindViewHolder(EditLabelsAdapter.EditLabelsViewHolder holder, int position) {
        holder.etxLabel.setText(labels.get(position));
    }

    @Override
    public int getItemCount() {
        return labels.size();
    }



    public class EditLabelsViewHolder extends RecyclerView.ViewHolder{

        private View topLine, bottomLine;
        private EditText etxLabel;
        private ImageView imgvDelete, imgvOk, imgvEdit;
        private ConstraintLayout mainLayout;

        public EditLabelsViewHolder(View itemView, final Context context) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            topLine = itemView.findViewById(R.id.edit_label_top_line);
            bottomLine = itemView.findViewById(R.id.edit_label_bottom_line);
            etxLabel = itemView.findViewById(R.id.an_edit_label);
            imgvDelete = itemView.findViewById(R.id.delete_label_icon);
            imgvEdit = itemView.findViewById(R.id.edit_label_icon);
            imgvOk = itemView.findViewById(R.id.ok_label_icon);

            imgvOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideKeyboard();
                    mainLayout.requestFocus();
                    updatePrefs(etxLabel.getText().toString(), EditLabelsActivity.ACTION_SAVE);
                }
            });

            imgvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideKeyboard();
                    mainLayout.requestFocus();
                    updatePrefs(etxLabel.getText().toString(), EditLabelsActivity.ACTION_DELETE);
                }
            });

            imgvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    etxLabel.requestFocus();
                }
            });

            etxLabel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (etxLabel.hasFocus()){
                        imgvDelete.setVisibility(View.VISIBLE);
                        imgvEdit.setVisibility(View.INVISIBLE);
                        imgvOk.setVisibility(View.VISIBLE);
                        topLine.setVisibility(View.VISIBLE);
                        bottomLine.setVisibility(View.VISIBLE);
                        mainLayout.setBackgroundColor(context.getResources().getColor(android.R.color.white));
                    } else {
                        imgvDelete.setVisibility(View.INVISIBLE);
                        imgvEdit.setVisibility(View.VISIBLE);
                        imgvOk.setVisibility(View.INVISIBLE);
                        topLine.setVisibility(View.INVISIBLE);
                        bottomLine.setVisibility(View.INVISIBLE);
                        //TODO:  make sure to set the initial background color of activity
                        mainLayout.setBackgroundColor(Color.parseColor("#FAFAFA"));
                    }
                }
            });
        }

        private void updatePrefs(String labelText, int action) {
            ((EditLabelsActivity)context).updatePrefs(getAdapterPosition(), labelText, action);
        }

        private void hideKeyboard() {
            InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(etxLabel.getWindowToken(), 0);
        }

    }
}
