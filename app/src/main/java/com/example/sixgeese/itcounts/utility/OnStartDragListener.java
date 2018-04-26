package com.example.sixgeese.itcounts.utility;

import android.support.v7.widget.RecyclerView;

/**
 * Created by sixge on 4/25/2018.
 */

public interface OnStartDragListener {
    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
