package com.example.sixgeese.itcounts.utility;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sixgeese.itcounts.DayDetailActivity;
import com.example.sixgeese.itcounts.model.ThingSet;

/**
 * Created by sixge on 4/20/2018.
 */

public class ThingSetTextWatcher implements TextWatcher{

    private ThingSet theSet;
    private EditText numRepsEditText;
    private SharedPreferences prefs;
    private int position;
    private int thingMonthId;


    public ThingSetTextWatcher(EditText editText, ThingSet thingSet, int thingMonthId, int position, SharedPreferences prefs){
        this.numRepsEditText = editText;
        this.theSet = thingSet;
        this.prefs = prefs;
        this.thingMonthId = thingMonthId;
        this.position = position;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        //within s, the count characters beginning at start have just replaced old text that had length before.
        if (charSequence.length() > 0) {
            //prefs.edit().putInt(thingMonthId + "position_" + position, Integer.valueOf(charSequence.toString())).apply();
            theSet.setReps(Integer.valueOf(charSequence.toString()));
        } else {
            //prefs.edit().putInt(thingMonthId + "position_" + position, 0).apply();
            theSet.setReps(0);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {}
}
