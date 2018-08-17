package com.tang.customcontrol.ui;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.tang.customcontrol.R;
import com.tang.customcontrol.adapter.TestAdapter;
import com.tang.customcontrol.bean.ItemBean;
import com.tang.customcontrol.control.hao.HaoRecyclerView;
import com.tang.customcontrol.control.hao.LoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：Tang
 * 创建时间：2018/8/16
 * Description：LayoutAnimation应用在List列表中
 */
public class LayoutAnimationListActivity extends AppCompatActivity implements LoadMoreListener {

    @Bind(R.id.recyclerView)
    HaoRecyclerView recyclerView;
    private TestAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layoutanimation);
        ButterKnife.bind(this);

        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this,
                R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(controller);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        adapter = new TestAdapter();
        adapter.addAll(getList(20));
        recyclerView.setAdapter(adapter);
        recyclerView.setLoadMoreListener(this);
        final int itemSpacing = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(itemSpacing, itemSpacing, itemSpacing, 0);
            }
        });
    }

    private List<ItemBean> getList(int count) {
        List<ItemBean> itemBeanList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            itemBeanList.add(new ItemBean("item", "item:" + i));
        }
        return itemBeanList;
    }

    @Override
    public void onLoadMore() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.addAll(getList(6));
                adapter.notifyDataSetChanged();
                recyclerView.completeLoadMore();
            }
        }, 2000);
    }

    public void FollDown(View view) {
        LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    public void right(View view) {
        LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_slide_right);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    public void bottom(View view) {
        LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

}