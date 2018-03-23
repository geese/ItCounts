package com.example.sixgeese.itcounts.areTheseNeeded;

import com.example.sixgeese.itcounts.model.ThingSet;

import java.util.ArrayList;

/**
 * Created by sixge on 3/22/2018.
 */

public class ThingItCounts {

    private String title;
    private ArrayList<ThingSet> sets;

    public ThingItCounts(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
