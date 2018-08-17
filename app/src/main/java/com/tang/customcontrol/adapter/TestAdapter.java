package com.tang.customcontrol.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tang.customcontrol.bean.ItemBean;
import com.tang.customcontrol.holder.ItemSplashHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者:  tang
 * 时间： 2018/8/17 0017 下午 2:22
 * 邮箱： 3349913147@qq.com
 * 描述：
 */

public class TestAdapter extends RecyclerView.Adapter<ItemSplashHolder> {

    private List<ItemBean> mList = new ArrayList<>();

    public void addAll(List<ItemBean> list) {
        mList.addAll(list);
    }

    public void replaceAll(List<ItemBean> list) {
        mList.clear();
        mList.addAll(list);
    }

    @Override
    public ItemSplashHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemSplashHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(ItemSplashHolder holder, int position) {
        holder.getTvTitle().setText(getItem(position).getTitle());
        holder.getTvDesc().setText(getItem(position).getDesc());
    }

    private ItemBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getItemCount() {

        return mList.size();
    }

}