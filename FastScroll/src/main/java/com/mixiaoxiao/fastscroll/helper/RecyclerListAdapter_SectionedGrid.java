package com.mixiaoxiao.fastscroll.helper;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mixiaoxiao.fastscroll.FastScrollRecyclerView;

public class RecyclerListAdapter_SectionedGrid extends FastScrollRecyclerView.Adapter<FastScrollRecyclerView.ViewHolder> {
    private final Context mContext;
    private static final int SECTION_TYPE = 0;

    private boolean mValid = true;
    private int mSectionResourceId;
    private SectionBinder mSectionBinder;
    private LayoutInflater mLayoutInflater;
    private FastScrollRecyclerView.Adapter mBaseAdapter;
    public SparseArray<Section> mSections = new SparseArray<>();
    private FastScrollRecyclerView mRecyclerView;

    public interface SectionBinder {
        void onBindView(final RecyclerListAdapter_SectionedGrid mAdapter, final SectionViewHolder holder, int mPosition);
    }

    public RecyclerListAdapter_SectionedGrid(Context context, int sectionResourceId, FastScrollRecyclerView recyclerView,
                                             FastScrollRecyclerView.Adapter baseAdapter, SectionBinder sectionBinder) {

        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSectionResourceId = sectionResourceId;
        mBaseAdapter = baseAdapter;
        mContext = context;
        mRecyclerView = recyclerView;
        mSectionBinder= sectionBinder;
        mBaseAdapter.registerAdapterDataObserver(new FastScrollRecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                mValid = mBaseAdapter.getItemCount()>0;
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                mValid = mBaseAdapter.getItemCount()>0;
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mValid = mBaseAdapter.getItemCount()>0;
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                mValid = mBaseAdapter.getItemCount()>0;
                notifyItemRangeRemoved(positionStart, itemCount);
            }
        });

        final GridLayoutManager layoutManager = (GridLayoutManager) (mRecyclerView.getLayoutManager());
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (isSectionHeaderPosition(position))? layoutManager.getSpanCount() : 1 ;
            }
        });
        recyclerView.setAdapter(this);
    }

    @Override
    public FastScrollRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int typeView) {
        if (typeView == SECTION_TYPE) {
            final View view = mLayoutInflater.inflate(mSectionResourceId, parent, false);
            return new SectionViewHolder(view);
        } else {
            return mBaseAdapter.onCreateViewHolder(parent, typeView -1);
        }
    }

    @Override
    public void onBindViewHolder(FastScrollRecyclerView.ViewHolder sectionViewHolder, int position) {
        if (isSectionHeaderPosition(position)) {
            if(mSectionBinder!=null) {
                mSectionBinder.onBindView(this, (SectionViewHolder) sectionViewHolder, position);
            }
        } else {
            mBaseAdapter.onBindViewHolder(sectionViewHolder,sectionedPositionToPosition(position));
        }

    }

    @Override
    public int getItemViewType(int position) {
        return isSectionHeaderPosition(position)
                ? SECTION_TYPE
                : mBaseAdapter.getItemViewType(sectionedPositionToPosition(position)) +1 ;
    }


    public static class Section {
        int firstPosition;
        int sectionedPosition;
        CharSequence title;

        public Section(int firstPosition, CharSequence title) {
            this.firstPosition = firstPosition;
            this.title = title;
        }

        public CharSequence getTitle() {
            return title;
        }
    }

    public static class SectionViewHolder extends FastScrollRecyclerView.ViewHolder {
        public View mItemView;
        public SectionViewHolder(View view) {
            super(view);
            mItemView= view;
        }
    }

    public void setSections(Section[] sections) {
        mSections.clear();
        int offset = 0; // offset positions for the headers we're adding
        for (Section section : sections) {
            section.sectionedPosition = section.firstPosition + offset;
            mSections.append(section.sectionedPosition, section);
            ++offset;
        }

        notifyDataSetChanged();
    }

    public int positionToSectionedPosition(int position) {
        int offset = 0;
        for (int i = 0; i < mSections.size(); i++) {
            if (mSections.valueAt(i).firstPosition > position) {
                break;
            }
            ++offset;
        }
        return position + offset;
    }

    public int sectionedPositionToPosition(int sectionedPosition) {
        if (isSectionHeaderPosition(sectionedPosition)) {
            return FastScrollRecyclerView.NO_POSITION;
        }

        int offset = 0;
        for (int i = 0; i < mSections.size(); i++) {
            if (mSections.valueAt(i).sectionedPosition > sectionedPosition) {
                break;
            }
            --offset;
        }
        return sectionedPosition + offset;
    }

    public boolean isSectionHeaderPosition(int position) {
        return mSections.get(position) != null;
    }


    @Override
    public long getItemId(int position) {
        return isSectionHeaderPosition(position)
                ? Integer.MAX_VALUE - mSections.indexOfKey(position)
                : mBaseAdapter.getItemId(sectionedPositionToPosition(position));
    }

    @Override
    public int getItemCount() {
        return (mValid ? mBaseAdapter.getItemCount() + mSections.size() : 0);
    }

}