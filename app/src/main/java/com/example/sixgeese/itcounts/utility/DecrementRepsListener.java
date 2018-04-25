package com.example.sixgeese.itcounts.utility;

import android.view.View;
import android.widget.EditText;

import com.example.sixgeese.itcounts.model.ThingSet;

/**
 * Created by sixge on 4/25/2018.
 */

public class DecrementRepsListener implements View.OnClickListener {
    public static final int DECREMENT_STEP = 1;

    EditText editText;
    ThingSet thingSet;

    public DecrementRepsListener(EditText editText, ThingSet thingSet) {
        this.editText = editText;
        this.thingSet = thingSet;
    }

    @Override
    public void onClick(View view) {
        String repsString = editText.getText().toString();
        int oldNum = repsString.isEmpty() ? 0 : Integer.valueOf(repsString);
        int newNum = (oldNum - DECREMENT_STEP) >= 0 ? oldNum - DECREMENT_STEP : 0;  //don't allow number to go negative.
        editText.setText(String.valueOf(newNum));
        thingSet.setReps(newNum);
    }
}
