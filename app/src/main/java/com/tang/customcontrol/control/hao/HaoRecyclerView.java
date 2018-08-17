package com.tang.customcontrol.control.hao;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.tang.customcontrol.control.hao.adapter.FooterAdapter;

/**
 * author：Tang
 * 创建时间：2018/8/16
 * Description：具有加载更多的RecyclerView
 */
public class HaoRecyclerView extends RecyclerView {

    private LoadingMoreView moreView;
    private FooterAdapter footerAdapter;
    /**
     * 是否正在加载中
     */
    private boolean isLoading = false;
    /**
     * 是否可以加载更多
     */
    private boolean isCanLoadMore = true;
    /**
     * 加载更多监听
     */
    private LoadMoreListener loadMoreListener;

    public HaoRecyclerView(Context context) {
        this(context, null);
    }

    public HaoRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HaoRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        moreView = new LoadingMoreView(getContext());
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isCanLoadMore() {
        return isCanLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        isCanLoadMore = canLoadMore;
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void completeLoadMore() {
        isLoading = false;
    }

    //点击监听
    public void setOnItemClickListener(FooterAdapter.OnItemClickListener onItemClickListener) {
        if (footerAdapter != null) {
            footerAdapter.setOnItemClickListener(onItemClickListener);
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE && loadMoreListener != null && !isLoading && isCanLoadMore) {
            int lastVisibleItem;
            LayoutManager manager = getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                lastVisibleItem = ((GridLayoutManager) manager).findLastVisibleItemPosition();
            } else if (manager instanceof StaggeredGridLayoutManager) {
                int[] position = ((StaggeredGridLayoutManager) manager).findLastVisibleItemPositions(null);
                lastVisibleItem = lastPositions(position);
            } else {
                lastVisibleItem = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
            }

            if (manager.getChildCount() > 0 && lastVisibleItem >= manager.getChildCount() - 1) {
                isLoading = true;
                loadMoreListener.onLoadMore();
            }
        }
    }

    private int lastPositions(int[] positions) {
        int last = positions[0];
        for (int position : positions) {
            if (position > last)
                last = position;
        }
        return last;
    }

    public void setAdapter(Adapter adapter) {
        footerAdapter = new FooterAdapter(this, moreView, adapter);
        super.setAdapter(footerAdapter);
        adapter.registerAdapterDataObserver(observer);
    }

    private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            footerAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            footerAdapter.notifyItemChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            footerAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            footerAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            footerAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            footerAdapter.notifyItemRangeRemoved(fromPosition, toPosition);
        }
    };

}