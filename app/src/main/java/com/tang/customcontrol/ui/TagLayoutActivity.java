package com.tang.customcontrol.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tang.customcontrol.R;
import com.tang.customcontrol.adapter.TagAdapter;
import com.tang.customcontrol.control.TagLayout;

/**
 * author：Tang
 * 创建时间：2018/8/12
 * Description：TagLayout测试
 */
public class TagLayoutActivity extends AppCompatActivity {
    private TagLayout mTagLayout;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taglayout);
        this.mContext = this;
        mTagLayout = (TagLayout) findViewById(R.id.tagLayout);
        initData();
    }

    private void initData() {
        String[] tags = getResources().getStringArray(R.array.tag);
        mTagLayout.setAdapter(new MyTagAdapter(tags));
    }

    private class MyTagAdapter extends TagAdapter<String> {

        public MyTagAdapter(String[] datas) {
            super(datas);
        }

        @Override
        public View getView(TagLayout parent, int position, String s) {
            TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_tag, parent, false);
            textView.setText(s);
            return textView;
        }
    }

}