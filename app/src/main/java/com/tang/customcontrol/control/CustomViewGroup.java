package com.tang.customcontrol.control;

import android.content.Context;
import android.util.AttributeSet;
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 对其所有的childView进行定位（设置childView的绘制区域）
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    /**
     * 自定义控件默认设置MarginLayoutParams：属性支支持margin
     */
    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(getContext(), null);
    }

}
