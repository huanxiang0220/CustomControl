package com.tang.customcontrol.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * author：Tang
 * 创建时间：2018/8/7
 * Description：分别将0，1，2，3位置的childView依次设置到左上、右上、左下、右下的位置
 */
public class CustomViewGroup extends ViewGroup {

    public CustomViewGroup(Context context) {
        super(context);
    }

    public CustomViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 计算childView的测量值和模式，设置自己的宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //计算所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //记录放mode为wrap_content时的宽度和高度
        int width = 0, height = 0;
        //上下的宽度，左右的高度
        int tWidth = 0, bWidth = 0, lHeight = 0, rHeight = 0;

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            int mesWidth = childView.getMeasuredWidth();
            int mesHeight = childView.getMeasuredHeight();
            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
            // 上面两个childVie
            if (i == 0 || i == 1) {
                tWidth += mesWidth + layoutParams.leftMargin + layoutParams.rightMargin;
            }
            // 下面两个childVie
            if (i == 2 || i == 3) {
                bWidth += mesWidth + layoutParams.leftMargin + layoutParams.rightMargin;
            }
            // 左面两个childVie
            if (i == 0 || i == 2) {
                lHeight += mesHeight + layoutParams.topMargin + layoutParams.bottomMargin;
            }
            //右面两个childView
            if (i == 1 || i == 3) {
                rHeight += mesHeight + layoutParams.topMargin + layoutParams.bottomMargin;
            }
        }

        width = Math.max(tWidth, bWidth);
        height = Math.max(lHeight, rHeight);

        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? widthSize : width,
                heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    /**
     * 对其所有的childView进行定位（设置childView的绘制区域）
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            int mesWidth = childView.getMeasuredWidth();
            int mesHeight = childView.getMeasuredHeight();
            MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
            int cl = 0, ct = 0;
            switch (i) {
                case 0:
                    cl = params.leftMargin;
                    ct = params.topMargin;
                    break;
                case 1:
                    cl = getWidth() - params.rightMargin - mesWidth;
                    ct = params.topMargin;
                    break;
                case 2:
                    cl = params.leftMargin;
                    ct = getHeight() - params.bottomMargin - mesHeight;
                    break;
                case 3:
                    cl = getWidth() - params.rightMargin - mesWidth;
                    ct = getHeight() - mesHeight - params.bottomMargin;
                    break;
            }

            childView.layout(cl, ct, cl + mesWidth, ct + mesHeight);
        }
    }


//    @Override
//    protected LayoutParams generateLayoutParams(LayoutParams p) {
//        return new MarginLayoutParams(getContext(), null);
//    }

    /**
     * 自定义控件默认设置MarginLayoutParams：属性支支持margin
     */
    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

//    @Override
//    public LayoutParams generateLayoutParams(AttributeSet attrs) {
//        return new MarginLayoutParams(getContext(), attrs);
//    }LayoutParams前部不加上ViewGroup布局预览图将会失败

}
