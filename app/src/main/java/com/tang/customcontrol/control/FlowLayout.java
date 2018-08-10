package com.tang.customcontrol.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者:  tang
 * 时间： 2018/8/10 0010 上午 11:53
 * 邮箱： 3349913147@qq.com
 * 描述： 流式布局
 */

public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //测量左右的childView
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //记录wrap_content最大的宽高
        int width = getPaddingStart() + getPaddingEnd(), height = getPaddingTop() + getPaddingBottom();

        int lineWidth = 0, lineHeight = 0;

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
            //childView需要的宽度
            int childWidth = childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int childHeight = childView.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            if (lineWidth + childWidth + getPaddingStart() + getPaddingEnd() > getMeasuredWidth()) {//另起一行
                lineHeight = childHeight;
                lineWidth = childWidth;
                width = Math.max(Math.max(lineWidth, childWidth), width);//取出最大的宽度
                height += Math.max(lineHeight, childHeight);//取出最大的高度累加;

            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            if (i == count - 1) {//取出最大
                height = Math.max(height, lineHeight);
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }

        }

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width,
                heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    /**
     * 所有的view
     */
    private List<List<View>> mAllViews = new ArrayList<>();
    /**
     * 存储每一行容纳的View
     */
    private List<View> lineViews = new ArrayList<>();
    /**
     * 所有行的行高
     */
    private List<Integer> mHeights = new ArrayList<>();
    private int lineWidth, lineHeight;
    private int left, top;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            //childView需要的宽度
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            if (lineWidth + childWidth + params.leftMargin + params.rightMargin + getPaddingStart() + getPaddingEnd() > getMeasuredWidth()) {
                //需要换行
                mAllViews.add(lineViews);
                lineViews = new ArrayList<>();
                mHeights.add(lineHeight);
                lineWidth = getPaddingStart();// 重置行宽
            }

            //累计宽度
            lineWidth += childWidth + params.leftMargin + params.rightMargin;
            //当前行的最大行高
            lineHeight = Math.max(lineHeight, childHeight + params.topMargin + params.bottomMargin);
            lineViews.add(child);
        }

        //记录最后一行
        mHeights.add(lineHeight);
        mAllViews.add(lineViews);

        int left = getPaddingLeft(), top = getPaddingTop();
        //所有行
        for (int i = 0; i < mAllViews.size(); i++) {
            //每一行
            List<View> lineViews = mAllViews.get(i);
            //当前行的最大的高度
            int lineHeight = mHeights.get(i);

            for (int i1 = 0; i1 < lineViews.size(); i1++) {
                View child = lineViews.get(i1);
                MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
                if (child.getVisibility() == GONE) {
                    continue;
                }

                int cl = left + params.leftMargin;
                int ct = top + params.topMargin;
                int cr = cl + child.getMeasuredWidth();
                int cb = ct + child.getMeasuredHeight();

                child.layout(cl, ct, cr, cb);
                left += params.leftMargin + child.getMeasuredWidth() + params.rightMargin;
            }

            left = getPaddingStart();//重置宽度
            top += lineHeight;
        }

    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

}