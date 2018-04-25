package com.example.sixgeese.itcounts.utility;

import android.view.View;
import android.widget.EditText;

import com.example.sixgeese.itcounts.model.ThingSet;

/**
 * Created by sixge on 4/25/2018.
 */

public class IncrementRepsListener implements View.OnClickListener {
    public static final int INCREMENT_STEP = 1;

    EditText editText;
    ThingSet thingSet;

    public IncrementRepsListener(EditText editText, ThingSet thingSet) {
        this.editText = editText;
        this.thingSet = thingSet;
    }

    @Override
    public void onClick(View view) {
        String repsString = editText.getText().toString();
        int oldNum = repsString.isEmpty() ? 0 : Integer.valueOf(repsString);
        int newNum = oldNum + INCREMENT_STEP;
        editText.setText(String.valueOf(newNum));
        editText.clearFocus();
        thingSet.setReps(newNum);
    }
}
