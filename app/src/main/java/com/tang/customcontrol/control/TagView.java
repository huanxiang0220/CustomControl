package com.tang.customcontrol.control;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

/**
 * author：Tang
 * 创建时间：2018/8/12
 * Description：tag包装一层，以使得tag拥有checked状态
 */
public class TagView extends FrameLayout {

    private boolean isChecked;

    private static final int[] CHECK_STATE = new int[]{
            android.R.attr.state_checked
    };

    public TagView(@NonNull Context context) {
        super(context);
    }

    public View getView() {
        return getChildAt(0);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] status = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked) {
            mergeDrawableStates(status, CHECK_STATE);
        }
        return status;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        if (isChecked != checked) {
            isChecked = checked;
            refreshDrawableState();
        }
    }

    public void toggle() {
        setChecked(!isChecked);
    }

}
