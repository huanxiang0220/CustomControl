package com.tang.customcontrol.control.hao;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tang.customcontrol.R;

/**
 * author：Tang
 * 创建时间：2018/8/16
 * Description：加载更多View
 */
public class LoadingMoreView extends LinearLayout {

    public LoadingMoreView(Context context) {
        this(context, null);
    }

    public LoadingMoreView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingMoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        View view = View.inflate(context, R.layout.layout_loadingmore, null);

        addView(view);
    }

}