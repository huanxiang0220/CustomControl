package com.tang.customcontrol.net;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * 作者:  tang
 * 时间： 2018/8/16 0016 下午 3:39
 * 邮箱： 3349913147@qq.com
 * 描述：
 */

public class MyOkHttp {

    private static String TAG = "MyOkHttp";

    public static String get(String url) {
        OkHttpClient client = new OkHttpClient();
//        client.setConnectTimeout(5, TimeUnit.MINUTES);

        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful())
                return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        client.newCall(request)
//                .enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Request request, IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Response response) throws IOException {
//                        String data = response.body().string();
//                        Log.e(TAG, "data:" + data);
//                    }
//                });


//        Log.e(TAG, "开始");执行1
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                Log.e(TAG, "回调失败");执行3
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                Log.e(TAG, "回调成功");执行3
//            }
//        });
//        Log.e(TAG, "结束");执行1
        return null;
    }

}