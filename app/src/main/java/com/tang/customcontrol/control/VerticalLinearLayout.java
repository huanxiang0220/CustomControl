package com.tang.customcontrol.control;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
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
    private VelocityTracker velocityTracker;//加速度

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
//                MarginLayoutParams marginParams = (MarginLayoutParams) getLayoutParams();
//                marginParams.height = 3 * mScreenHeight;
//                setLayoutParams(marginParams);//对修改控件高度不起作用
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        mScroller = new Scroller(context);
        velocityTracker = VelocityTracker.obtain();
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private boolean isScrolling;//是否可以滑动

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isScrolling)// 如果当前正在滚动，调用父类的onTouchEve
            return super.onTouchEvent(event);
        int action = event.getAction();
        int y = (int) event.getY();

        obtainVelocity(event);//绑定加速度检测器
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScrollStart = getScrollY();
                Log.e(TAG, "ACTION_DOWN：mScrollStart ---- " + mScrollStart);
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                //(偏移量是这次相对上次跑了多少，偏移的坐标与手指触动的方向相反，所以dy采用上次的坐标y-这次的y坐标的差值)
                int dy = mLastY - y;
                //边界值检查
                int scrollY = getScrollY();//当前的偏移量
                Log.e(TAG, "ACTION_MOVE：scrollY ---- " + scrollY + "-------dy:" + dy);

                if (dy < 0 && scrollY + dy <= 0)//往下拉,到达顶端，(scrollY + dy)总的需要偏移的距离
                {
                    dy = -scrollY;//在达到顶端的时候偏移多少就就反方向移动多少，以保持维持在总偏移量在0处
                } else if (dy > 0 && scrollY + dy > getMeasuredHeight() - mScreenHeight) {//往上滑动，到达底端
                    dy = getMeasuredHeight() - mScreenHeight - scrollY;
                    //在达到底部的时候偏移多少就就反方向移动多少，以保持维持在总偏移量在底部处
                }
                scrollBy(0, dy);
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                mScrollEnd = getScrollY();
                //取出开始滑动和结束滑动的差值
                int dScrollY = mScrollEnd - mScrollStart;
                if (wantScrollPre())//往上滑动的动作
                {
                    if (shouldScrollToNext()) {//往上滑动，是否可以移动到下一页
                        mScroller.startScroll(0, getScrollY(), 0, mScreenHeight - dScrollY);
                    } else {
                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                    }
                }

                if (wantScrollToNext())//往下滑动的动作
                {
                    if (shouldScrollToPre()) {//往下滑动，是否移动到上一个
                        mScroller.startScroll(0, getScrollY(), 0, -mScreenHeight - dScrollY);
                    } else {
                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                    }
                }

                isScrolling = true;//可以滑动
                postInvalidate();//触发onDraw(),继而onDraw()内部调用computeScroll()
                recycleVelocity();//回收加速器检测器
                break;
        }

//        return super.onTouchEvent(event);
        return true;//自己处理滑动事件
    }

    /**
     * 往上滑动
     */
    private boolean wantScrollPre() {
        return mScrollStart < mScrollEnd; //getScrollY()向上是正数
    }

    private boolean wantScrollToNext() {
        return mScrollEnd < mScrollStart;
    }

    /**
     * 是否可以滑倒下一个
     */
    private boolean shouldScrollToNext() {
        //getScrollY()向上是正数
        return mScrollEnd - mScrollStart > mScreenHeight / 2 || Math.abs(getVelocity()) > 600;
    }

    /**
     * 是否移动到上一个
     */
    private boolean shouldScrollToPre() {
        return mScrollStart - mScrollEnd > mScreenHeight / 2 || Math.abs(getVelocity()) >
                ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
    }

    /**
     * 获取y方向的加速度
     */
    private int getVelocity() {
        velocityTracker.computeCurrentVelocity(1000);//计算当前的速度
        return (int) velocityTracker.getYVelocity();
    }

    /**
     * 初始化加速度检测器
     */
    private void obtainVelocity(MotionEvent event) {
        if (velocityTracker == null)
            velocityTracker = VelocityTracker.obtain();
        velocityTracker.addMovement(event);
    }

    /**
     * 释放资源
     */
    private void recycleVelocity() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        //computeScrollOffset()：当你想知道新的位置的时候，调用此方法
        //return true,代表动画没有结束
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());//Scroller计算的新位置
            postInvalidate();
        } else {
            int position = getScrollY() / mScreenHeight;//得到第几个

            if (position != currentPage) {
                if (mOnPageChangeListener != null) {
                    currentPage = position;
                    mOnPageChangeListener.onPageChange(currentPage);
                }
            }
            isScrolling = false;
        }
    }

//    @Override
//    public void onGlobalLayout() {
//        int height = getHeight();
//        getViewTreeObserver().removeGlobalOnLayoutListener(this);
//       一定要移除,因为onLayout()会执行多次,如果不移除的话 也会被回调多次,而且值还不一样
//    }

    /**
     * 记录当前页
     */
    private int currentPage = 0;

    private OnPageChangeListener mOnPageChangeListener;

    /**
     * 设置回调接口
     */
    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    /**
     * 回调接口
     */
    public interface OnPageChangeListener {
        void onPageChange(int currentPage);
    }

}
