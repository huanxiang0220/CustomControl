package com.tang.customcontrol.control;

import android.content.Context;
import android.net.MailTo;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.tang.customcontrol.adapter.TagAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * author：Tang
 * 创建时间：2018/8/11
 * Description：tag可点击的流式布局；FLowLayout的升级版
 */
public class TagLayout extends FlowLayout implements TagAdapter.onDataChangedListener {

    public TagLayout(Context context) {
        this(context, null);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            TagView tagView = (TagView) getChildAt(i);
            if (tagView.getChildAt(0).getVisibility() == GONE) {
                tagView.setVisibility(GONE);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private TagAdapter mTagAdapter;

    public void setAdapter(TagAdapter adapter) {
        this.mTagAdapter = adapter;
        mTagAdapter.setOnDataChangedListener(this);
        changeAdapter();
    }

    @Override
    public void onChange() {
        changeAdapter();
    }

    private void changeAdapter() {
        removeAllViews();
        TagAdapter adapter = mTagAdapter;

        TagView tagViewContainer = null;
        for (int i = 0; i < adapter.getCount(); i++) {
            View child = adapter.getView(this, i, adapter.getItem(i));
//            child.setLayoutParams(getLayoutParams());
            child.setDuplicateParentStateEnabled(true);//这个 View 将从其父容器而非自身获取绘制状态（焦点、点击等）
            tagViewContainer = new TagView(getContext());
            if (child.getLayoutParams() != null) {
                tagViewContainer.setLayoutParams(child.getLayoutParams());
            } else {
                MarginLayoutParams lp = new MarginLayoutParams(generateDefaultLayoutParams());
                lp.setMargins(dip2px(getContext(), 5),
                        dip2px(getContext(), 5),
                        dip2px(getContext(), 5),
                        dip2px(getContext(), 5));
            }
            LayoutParams lp = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            child.setLayoutParams(lp);
            tagViewContainer.addView(child);
            addView(tagViewContainer);

            tagViewContainer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }

    public static int dip2px(Context context, float dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

}