package com.tang.customcontrol.holder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tang.customcontrol.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ItemBehaviorHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.tv_iv)
    ImageView tvIv;
    @Bind(R.id.tv_title)
    TextView tvId;

    public ItemBehaviorHolder(LayoutInflater inflater, ViewGroup parent) {
        this(inflater.inflate(R.layout.item_behavior, parent, false));
    }

    public ItemBehaviorHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public ImageView getTvIv() {
        return tvIv;
    }

    public TextView getTvId() {
        return tvId;
    }
}
