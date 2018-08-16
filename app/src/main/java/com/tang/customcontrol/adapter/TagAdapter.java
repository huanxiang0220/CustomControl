package com.tang.customcontrol.adapter;

import android.view.View;

import com.tang.customcontrol.control.TagLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author：Tang
 * 创建时间：2018/8/12
 * Description：TagLayout
 */
public abstract class TagAdapter<T> {
    private List<T> mTagDatas;
    private onDataChangedListener mOnDataChangedListener;

    public TagAdapter(List<T> mTagDatas) {
        this.mTagDatas = mTagDatas;
    }

    public TagAdapter(T[] datas) {
        this.mTagDatas = new ArrayList<>(Arrays.asList(datas));
    }

    public interface onDataChangedListener {
        void onChange();
    }

    public void setOnDataChangedListener(onDataChangedListener mOnDataChangedListener) {
        this.mOnDataChangedListener = mOnDataChangedListener;
    }

    public int getCount() {
        return mTagDatas != null ? mTagDatas.size() : 0;
    }

    public void notifyDataChanged() {
        if (mOnDataChangedListener != null)
            mOnDataChangedListener.onChange();
    }

    public T getItem(int position) {
        return mTagDatas.get(position);
    }

    public abstract View getView(TagLayout parent, int position, T t);

}