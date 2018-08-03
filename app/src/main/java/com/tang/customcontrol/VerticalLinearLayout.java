package com.tang.customcontrol;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Scroller;

/**
 * Created by Tang on 2018/8/2.
 */
public class VerticalLinearLayout extends ViewGroup {
    private static final String TAG = "VerticalLinearLayout";

    private int mScreenHeight;//屏幕高度
    private int mScrollStart, mScrollEnd;
    private int mLastY;//记录当前移动时的Y坐标

    public VerticalLinearLayout(Context context) {
        this(context, null);
    }

    public VerticalLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private Scroller mScroller;

    public VerticalLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics dm = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(dm);
//        mScreenHeight = dm.heightPixels;

        mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
//        getViewTreeObserver().addOnGlobalLayoutListener(this);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                MarginLayoutParams marginParams = (MarginLayoutParams) getLayoutParams();
                marginParams.height = 3 * mScreenHeight;
                setLayoutParams(marginParams);//不起作用
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, mScreenHeight);
        }
        setMeasuredDimension(widthMeasureSpec, mScreenHeight * count);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG, "执行onSizeChanged");
    }

    @Override
    protected void onLayout(boolean change, int l, int t, int r, int b) {
        if (change) {
            Log.e(TAG, "执行onLayout");
            int count = getChildCount();
//            MarginLayoutParams marginParams = (MarginLayoutParams) getLayoutParams();
//            marginParams.height = count * mScreenHeight;
//            setLayoutParams(marginParams);没有作用

            for (int i = 0; i < count; i++) {
                View childView = getChildAt(i);
                if (childView.getVisibility() != GONE)
                    childView.layout(l, i * mScreenHeight, r, (i + 1) * mScreenHeight);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScrollStart = getScrollY();
                Log.e(TAG, "ACTION_DOWN：mScrollStart ---- " + mScrollStart);
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = mLastY - y;
                //边界值检查
                int scrollY = getScrollY();
                Log.e(TAG, "ACTION_MOVE：scrollY ---- " + scrollY + "-------dy:" + dy);

                int height = getHeight();
                int measureHeight = getMeasuredHeight();
                if (dy < 0 && scrollY <= 0)//往下拉
                {
                    return true;
                }
                Log.e(TAG, "dy:" + dy + "------scrollY:" + scrollY);

//                if (dy > 0 && scrollY + dy > getMeasuredHeight() - mScreenHeight)//网上滑动,已经到达底端
//                {
//                    dy = getHeight() - mScreenHeight - scrollY;
////                    return super.onTouchEvent(event);
//                } else if (dy < 0 && scrollY + dy < 0) {//向下拉
//                    dy = -scrollY;
//                }
                scrollBy(0, dy);
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
//                mScrollEnd = getScrollY();
//
//                int dScrollY = mScrollEnd - mScrollStart;
//
//                if (wantScrollToNext())// 往上滑动
//                {
//                    if (shouldScrollToNext()) {
//                        mScroller.startScroll(0, getScrollY(), 0, mScreenHeight - dScrollY);
//
//                    } else {
//                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
//                    }
//
//                }
//
//                if (wantScrollToPre())// 往下滑动
//                {
//                    if (shouldScrollToPre()) {
//                        mScroller.startScroll(0, getScrollY(), 0, -mScreenHeight - dScrollY);
//
//                    } else {
//                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
//                    }
//                }
//                isScrolling = true;
//                postInvalidate();
//                recycleVelocity();
                break;
        }

//        return super.onTouchEvent(event);
        return true;//自己处理滑动事件
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
    }

//    @Override
//    public void onGlobalLayout() {
//        int height = getHeight();
//        getViewTreeObserver().removeGlobalOnLayoutListener(this);//一定要移除,因为onLayout()会执行多次,如果不移除的话 也会被回调多次,而且值还不一样
//    }

}
