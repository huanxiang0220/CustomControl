package com.tang.customcontrol.control.hao.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.tang.customcontrol.control.hao.HaoRecyclerView;
import com.tang.customcontrol.control.hao.LoadingMoreView;

/**
 * author：Tang
 * 创建时间：2018/8/16
 * Description：对用户设置的adapter进行装饰
 */
public class FooterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final int FOOTVIEW = -1;
    private HaoRecyclerView mHaoRecyclerView;
    private LoadingMoreView mMoreView;
    private RecyclerView.Adapter mAdapter;

    public FooterAdapter(HaoRecyclerView haoRecyclerView, LoadingMoreView moreView, RecyclerView.Adapter adapter) {
        this.mHaoRecyclerView = haoRecyclerView;
        this.mMoreView = moreView;
        this.mAdapter = adapter;
    }

    /**
     * 解决FootView在不同的Manager下独占一行
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final int spanCount = ((GridLayoutManager) manager).getSpanCount();
            ((GridLayoutManager) manager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    //验证(FOOTVIEW！=-1)
                    if (getItemViewType(position) == RecyclerView.INVALID_TYPE ||
                            getItemViewType(position) == RecyclerView.INVALID_TYPE - 1)
                        return spanCount;
                    return 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams
                && isFooter(holder.getLayoutPosition())) {
            StaggeredGridLayoutManager.LayoutParams lp =
                    (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            lp.setFullSpan(true);
        }
    }

    /**
     * 当前是否是footer
     */
    private boolean isFooter(int position) {
        return position < getItemCount() && position > getItemCount() - 1;
    }

    @Override
    public int getItemCount() {
        return mAdapter != null ? mAdapter.getItemCount() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooter(position))
            return FOOTVIEW;
        if (mAdapter != null)
            return mAdapter.getItemViewType(position);
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        if (isFooter(position))
            return FOOTVIEW;
        if (mAdapter != null) return mAdapter.getItemId(position);
        return super.getItemId(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (FOOTVIEW == viewType)
            return new FootHolder(mMoreView);
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (!(holder instanceof FootHolder)) {
//            mAdapter.onBindViewHolder(holder, position);
//        }
        int count = mAdapter.getItemCount();
        if (position < count) {
            mAdapter.onBindViewHolder(holder, position);
        }
    }

    //点击
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
        onItemClickListener.onItemClick(v, mHaoRecyclerView.getChildAdapterPosition(v));
    }

    private class FootHolder extends RecyclerView.ViewHolder {
        FootHolder(View itemView) {
            super(itemView);
        }
    }

}