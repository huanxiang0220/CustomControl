package com.tang.customcontrol.holder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tang.customcontrol.R;
/**
 * author：${USER}
 * 创建时间：${DATE}
 * Description：
 */
public class ItemSplashHolder extends RecyclerView.ViewHolder {
    private TextView tvTitle;
    private TextView tvDesc;

    public ItemSplashHolder(LayoutInflater inflater, ViewGroup parent) {
        this(inflater.inflate(R.layout.item_splash, parent, false));
    }

    public ItemSplashHolder(View view) {
        super(view);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvDesc = (TextView) view.findViewById(R.id.tv_desc);
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public TextView getTvDesc() {
        return tvDesc;
    }
}
