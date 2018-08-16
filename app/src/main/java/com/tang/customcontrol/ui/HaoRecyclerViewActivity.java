package com.tang.customcontrol.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tang.customcontrol.R;
import com.tang.customcontrol.control.hao.HaoRecyclerView;
import com.tang.customcontrol.control.hao.LoadMoreListener;
import com.tang.customcontrol.holder.ItemSplashHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：Tang
 * 创建时间：2018/8/16
 * Description：HaoRecyclerView测试
 */
public class HaoRecyclerViewActivity extends AppCompatActivity implements LoadMoreListener {

    @Bind(R.id.recyclerView)
    HaoRecyclerView recyclerView;
    private List<ItemBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haorecyclerview);
        ButterKnife.bind(this);

        for (int i = 1; i <= 20; i++) {
            mList.add(new ItemBean("item", "item:" + i));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ItemAdapter());
        recyclerView.setLoadMoreListener(this);
    }

    @Override
    public void onLoadMore() {

    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemSplashHolder> {
        @Override
        public ItemSplashHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemSplashHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ItemSplashHolder holder, int position) {
            holder.getTvTitle().setText(getItem(position).title);
            holder.getTvDesc().setText(getItem(position).desc);
        }

        private ItemBean getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    private static class ItemBean {
        private String title;
        private String desc;

        ItemBean(String title, String desc) {
            this.title = title;
            this.desc = desc;
        }
    }

}
