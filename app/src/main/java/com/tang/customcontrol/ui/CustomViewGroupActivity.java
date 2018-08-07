package com.tang.customcontrol.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tang.customcontrol.R;

/**
 * 手把手教您自定义ViewGroup：分别将0，1，2，3位置的childView依次设置到左上、右上、左下、右下的位置
 */
public class CustomViewGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_viewgroup);
    }

}
