package com.tang.customcontrol.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.tang.customcontrol.R;
import com.tang.customcontrol.control.VerticalLinearLayout;

/**
 * author：tang
 * 创建时间：2018/8/7
 * Description：ViewGroup实现竖向引导界面
 */
public class VerticalLinearLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verticallinearlayout);
        VerticalLinearLayout verticalLinearLayout = (VerticalLinearLayout) findViewById(R.id.vertical);
        verticalLinearLayout.setOnPageChangeListener(new VerticalLinearLayout.OnPageChangeListener() {
            @Override
            public void onPageChange(int currentPage) {
                Log.e("TAG", "currentPage: " + currentPage);
            }
        });
    }

}
