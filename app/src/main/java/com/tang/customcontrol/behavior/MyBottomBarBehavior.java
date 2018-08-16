package com.tang.customcontrol.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者:  tang
 * 时间： 2018/8/16 0016 下午 5:04
 * 邮箱： 3349913147@qq.com
 * 描述：
 */

public class MyBottomBarBehavior extends CoordinatorLayout.Behavior<View> {

    public MyBottomBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
//        return super.layoutDependsOn(parent, child, dependency);
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        child.setTranslationY(Math.abs(dependency.getTop()));
//        return super.onDependentViewChanged(parent, child, dependency);
        return true;
    }

}