package com.mixiaoxiao.fastscroll.helper;

import android.support.v7.widget.RecyclerView;

/**
 * Listener for manual initiation of a drag.
 */
public interface OnStartDragListener {


    void onStartDrag(RecyclerView.ViewHolder viewHolder);

}
