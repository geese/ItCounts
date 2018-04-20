package com.example.sixgeese.itcounts.utility;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by sixge on 4/19/2018.
 */



public class InputFilterMinValue implements InputFilter {

    private int min;

    public InputFilterMinValue(int min) {
        this.min = min;
    }

    public InputFilterMinValue(String min, String max) {
        this.min = Integer.parseInt(min);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int destStart, int destEnd) {
        return null;
    }
}
