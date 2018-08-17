package com.tang.customcontrol.control;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import com.tang.customcontrol.R;

/**
 * 作者:  tang
 * 时间： 2018/8/17 0017 下午 3:48
 * 邮箱： 3349913147@qq.com
 * 描述： 实现阴影
 */

public class ShadowLayout extends RelativeLayout {

    public static final int ALL = 0x1111;

    public static final int LEFT = 0x0001;

    public static final int TOP = 0x0010;

    public static final int RIGHT = 0x0100;

    public static final int BOTTOM = 0x1000;


    private Paint mPaint;
    /**
     * 阴影的大小范围
     */
    private float radius;
    /**
     * 阴影x轴的偏移量
     */
    private float dx;
    /**
     * 阴影y轴的偏移量
     */
    private float dy;
    /**
     * 阴影颜色
     */
    private int shadowColor;
    private RectF mRectF = new RectF();

    public ShadowLayout(Context context) {
        this(context, null);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);//保证onDraw()执行
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关闭硬件加速
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        if (typedArray != null) {
            radius = typedArray.getDimension(R.styleable.ShadowLayout_ShadowLayout_Radius, dip2px(5));
            dx = typedArray.getDimension(R.styleable.ShadowLayout_ShadowLayout_Dx, dip2px(0));
            dy = typedArray.getDimension(R.styleable.ShadowLayout_ShadowLayout_Dy, dip2px(0));
            shadowColor = typedArray.getColor(R.styleable.ShadowLayout_ShadowLayout_ShadowColor,
                    ContextCompat.getColor(getContext(), android.R.color.black));
            mShadowSide = typedArray.getInt(R.styleable.ShadowLayout_ShadowLayout_Side, ALL);
            typedArray.recycle();
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//mPaint.setAntiAlias(true)
        mPaint.setColor(Color.TRANSPARENT);//透明
        mPaint.setShadowLayer(radius, dx, dy, shadowColor);
    }

    /**
     * dip2px dp 值转 px 值
     *
     * @param dpValue dp 值
     * @return px 值
     */
    private float dip2px(float dpValue) {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        float scale = dm.density;
        return (dpValue * scale + 0.5F);
    }

    private int mShadowSide = ALL;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        float effect = radius + dip2px(5);
        float rectLeft = 0;
        float rectTop = 0;
        float rectRight = this.getWidth();
        float rectBottom = this.getHeight();
        int paddingLeft = 0;
        int paddingTop = 0;
        int paddingRight = 0;
        int paddingBottom = 0;

        if (((mShadowSide & LEFT) == LEFT)) {
            rectLeft = effect;
            paddingLeft = (int) effect;
        }
        if (((mShadowSide & TOP) == TOP)) {
            rectTop = effect;
            paddingTop = (int) effect;
        }
        if (((mShadowSide & RIGHT) == RIGHT)) {
            rectRight = this.getWidth() - effect;
            paddingRight = (int) effect;
        }
        if (((mShadowSide & BOTTOM) == BOTTOM)) {
            rectBottom = this.getHeight() - effect;
            paddingBottom = (int) effect;
        }
        if (dy != 0.0f) {
            rectBottom = rectBottom - dy;
            paddingBottom = paddingBottom + (int) dy;
        }
        if (dx != 0.0f) {
            rectRight = rectRight - dx;
            paddingRight = paddingRight + (int) dx;
        }
        mRectF.left = rectLeft;
        mRectF.top = rectTop;
        mRectF.right = rectRight;
        mRectF.bottom = rectBottom;
        this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(mRectF, mPaint);
    }

}