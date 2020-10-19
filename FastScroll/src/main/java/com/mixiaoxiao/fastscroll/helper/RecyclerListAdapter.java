package com.mixiaoxiao.fastscroll.helper;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mixiaoxiao.fastscroll.FastScrollRecyclerView;
import com.mixiaoxiao.fastscroll.R;

import java.util.ArrayList;
import java.util.Collections;

public class RecyclerListAdapter<T> extends FastScrollRecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {
    protected Context mContext;
    private int mViewID;
    public FastScrollRecyclerView mView;
    public int mLayout= 0;
    public GridLayoutManagerEx mGridLayoutManager;
    public LinearLayoutManagerEx mLinearLayoutManager;
    private OnStartDragListener mDragStartListener;
    private Binder mBinder;
    private ViewHolderListener mViewHolderListener;
    private ItemTouchHelper ithContacts;
    public ArrayList<T> mItems;
    public int handlerID= 0;
    public DividerItemDecoration mDividerItemDecoration;
    public static class GridLayoutManagerEx extends GridLayoutManager {
        public boolean mAutoMeasure= false;
        public GridLayoutManagerEx(Context context, int spanCount) {
            super(context, spanCount);
        }

        @Override
        public void setAutoMeasureEnabled(boolean enabled) {
            mAutoMeasure= enabled;
            super.setAutoMeasureEnabled(enabled);
        }

        @Override
        public boolean isAutoMeasureEnabled() {
            return super.isAutoMeasureEnabled();
        }
    }
    public static class LinearLayoutManagerEx extends LinearLayoutManager {
        public boolean mAutoMeasure= false;
        public LinearLayoutManagerEx(Context context) {
            super(context);
        }

        @Override
        public void setAutoMeasureEnabled(boolean enabled) {
            mAutoMeasure= enabled;
            super.setAutoMeasureEnabled(enabled);
        }

        @Override
        public boolean isAutoMeasureEnabled() {
            return super.isAutoMeasureEnabled();
        }
    }


    public RecyclerListAdapter() {}
    public RecyclerListAdapter(Context context, FastScrollRecyclerView view, int viewID, ArrayList<T> alItems,
                               SimpleItemTouchHelperCallback.Validator validator, Binder binder) {
        init(context, view, viewID, alItems, 0, 1, validator, binder, null);
    }
    public RecyclerListAdapter(Context context, FastScrollRecyclerView view, int viewID, ArrayList<T> alItems,
                               SimpleItemTouchHelperCallback.Validator validator, Binder binder,
                               ViewHolderListener viewHolderListener) {
        init(context, view, viewID, alItems, 0, 1, validator, binder, viewHolderListener);
    }

    public RecyclerListAdapter(Context context, FastScrollRecyclerView view, int viewID, ArrayList<T> alItems,
                               int noOfColumns, SimpleItemTouchHelperCallback.Validator validator, Binder binder) {
        init(context, view, viewID, alItems, 0, noOfColumns, validator, binder, null);
    }
    public RecyclerListAdapter(Context context, FastScrollRecyclerView view, int viewID, ArrayList<T> alItems,
                               int noOfColumns, SimpleItemTouchHelperCallback.Validator validator, Binder binder,
                               ViewHolderListener viewHolderListener) {
        init(context, view, viewID, alItems, 0, noOfColumns, validator, binder, viewHolderListener);
    }

    public RecyclerListAdapter(Context context, FastScrollRecyclerView view, int viewID, ArrayList<T> alItems,
                               int manager, int noOfColumns, SimpleItemTouchHelperCallback.Validator validator,
                               Binder binder) {
        init(context, view, viewID, alItems, manager, noOfColumns, validator, binder, null);
    }
    public RecyclerListAdapter(Context context, FastScrollRecyclerView view, int viewID, ArrayList<T> alItems,
                               int manager, int noOfColumns, SimpleItemTouchHelperCallback.Validator validator,
                               Binder binder, ViewHolderListener viewHolderListener) {
        init(context, view, viewID, alItems, manager, noOfColumns, validator, binder, viewHolderListener);
    }

    private void init(Context context, FastScrollRecyclerView view, int viewID, ArrayList<T> alItems,
                      int layoutManager, int noOfColumns, SimpleItemTouchHelperCallback.Validator validator,
                      Binder binder, ViewHolderListener viewHolderListener) {
        mContext= context;
        mViewID= viewID;
        mItems= alItems;
        mDragStartListener= new OnStartDragListener() {
            @Override
            public void onStartDrag(FastScrollRecyclerView.ViewHolder viewHolder) {
                ithContacts.startDrag(viewHolder);
            }
        };
        mBinder= binder;
        mViewHolderListener= viewHolderListener;
        mView= view;
        view.setHasFixedSize(false);
        view.setAdapter(this);
        mLayout= layoutManager;
        if(mLayout==0) {
            mGridLayoutManager = new GridLayoutManagerEx(context, noOfColumns);
            mGridLayoutManager.setAutoMeasureEnabled(false);
            view.setLayoutManager(mGridLayoutManager);
            /*if(noOfColumns > 1) {
                mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        int lastCols= alItems.size() % noOfColumns;
                        if(lastCols==0) {
                            return 1;
                        }
                        int rows= (alItems.size() / noOfColumns) + 1;
                        int row= position / noOfColumns;
                        int column= position % noOfColumns;
                        if (row == rows - 1 && (column==0 || column==lastCols-1)) {//last row, first-last cols
                            int span= (noOfColumns-lastCols) / 2;
                            return span<=1?2:span; // the item in position now takes up 4 spans
                        }
                        return 1;
                    }
                });
            }*/
        } else {
            //FixedGridLayoutManager fixedGridLayoutManager= new FixedGridLayoutManager();
            //fixedGridLayoutManager.setTotalColumnCount(1);
            //view.setLayoutManager(fixedGridLayoutManager);
            mLinearLayoutManager = new LinearLayoutManagerEx(context);
            mLinearLayoutManager.setAutoMeasureEnabled(false);
            view.setLayoutManager(mLinearLayoutManager);
        }

        if(mLayout==1 || noOfColumns == 1) {
            mDividerItemDecoration = new DividerItemDecoration(
                    mView.getContext(),
                    LinearLayoutManager.VERTICAL
            );
            mDividerItemDecoration.setDrawable(context.getResources().getDrawable(R.drawable.__divider_transparent_6dp));
            mView.addItemDecoration(mDividerItemDecoration);
        }
        ItemTouchHelper.Callback callback= new SimpleItemTouchHelperCallback(this, validator);
        ithContacts = new ItemTouchHelper(callback);
        ithContacts.attachToRecyclerView(view);
    }

    public interface ViewHolderListener {
        int getItemViewType(int position);
        View onCreateViewHolder(ViewGroup parent, int viewType);
        ItemViewHolder onCreateItemHolder(RecyclerListAdapter adapter, View view, int handlerID);
    }

    @Override
    public int getItemViewType(int position) {
        return mViewHolderListener==null?super.getItemViewType(position):mViewHolderListener.getItemViewType(position);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mViewHolderListener==null?null:mViewHolderListener.onCreateViewHolder(parent, viewType);
        if(view==null) {
            view= LayoutInflater.from(parent.getContext()).inflate(mViewID, parent, false);
        }
        FastScrollRecyclerView.LayoutParams lp = new FastScrollRecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        ItemViewHolder holder= mViewHolderListener==null?null:mViewHolderListener.onCreateItemHolder(this, view, handlerID);
        return holder==null?new ItemViewHolder(this, view, handlerID):holder;
    }

    public interface Binder {
        void onBindView(final RecyclerListAdapter<?> mAdapter, final ItemViewHolder holder, int mPosition);
        void onClick(final RecyclerListAdapter<?> mAdapter, final View view, final int position);
        boolean onLongClick(final RecyclerListAdapter<?> mAdapter, final View view, final int position);

        boolean onItemDismiss(final RecyclerListAdapter<?> mAdapter, final int position);
        boolean onItemMove(final RecyclerListAdapter<?> mAdapter, final int fromPosition, final int toPosition);
    }
    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        if(mBinder!=null) {
            mBinder.onBindView(this, holder, position);
        }
        if(holder.handleView!=null) {
            holder.handleView.setOnTouchListener((v, event) -> {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            });
        }
    }

    @Override
    public void onItemDismiss(int position) {
        if(mBinder!=null) {
            if(mBinder.onItemDismiss(this, position)) {
                mItems.remove(position);
                notifyItemRemoved(position);
            } else {
                notifyItemChanged(position);
            }
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if(mBinder!=null) {
            if(mBinder.onItemMove(this, fromPosition, toPosition)) {
                Collections.swap(mItems, fromPosition, toPosition);
                notifyItemMoved(fromPosition, toPosition);
            }
        }
        return true;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public int findFirstVisibleItem() {
        return mLayout==0?mGridLayoutManager.findFirstVisibleItemPosition():mLinearLayoutManager.findFirstVisibleItemPosition();
    }

    public boolean isScrolling() {
        if(mLayout==0) {
            return getItemCount()>0 &&
                    (mGridLayoutManager.findFirstCompletelyVisibleItemPosition() > 0 ||
                            mGridLayoutManager.findLastCompletelyVisibleItemPosition() < getItemCount() - 1);
        } else {
            return getItemCount()>0 &&
                    (mLinearLayoutManager.findFirstCompletelyVisibleItemPosition() > 0 ||
                            mLinearLayoutManager.findLastCompletelyVisibleItemPosition() < getItemCount() - 1);
        }
    }

    public static class ItemViewHolder extends FastScrollRecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder,
                        View.OnClickListener,
                        View.OnLongClickListener{
        public final View mItemView;
        public final ImageView handleView;
        public final RecyclerListAdapter<?> mAdapter;
        private final Drawable initialBG;
        public ItemViewHolder(RecyclerListAdapter<?> adapter, View itemView, int handlerID) {
            super(itemView);
            mAdapter= adapter;
            mItemView = itemView;
            handleView = handlerID>0?(ImageView) itemView.findViewById(handlerID):null;
            mItemView.setOnClickListener(this);
            mItemView.setOnLongClickListener(this);
            initialBG= mItemView.getBackground();
        }
        @Override
        public void onItemSelected() {
            //itemView.setBackgroundResource(R.drawable.list_item_grey_light);
        }
        @Override
        public void onItemClear() {
            //itemView.setBackground(initialBG);
        }

        @Override
        public void onClick(View view) {
            if(mAdapter.mBinder!=null) {
                mAdapter.mBinder.onClick(mAdapter, view, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if(mAdapter.mBinder!=null) {
                return mAdapter.mBinder.onLongClick(mAdapter, view, getAdapterPosition());
            }
            return false;
        }
    }

    public void mergeLists(ArrayList<T> from, ArrayList<T> to) {
        if(from==null) {
            return;
        }
        if(to==null) {
            to= new ArrayList<>();
        }
        to.addAll(from);
    }

}
