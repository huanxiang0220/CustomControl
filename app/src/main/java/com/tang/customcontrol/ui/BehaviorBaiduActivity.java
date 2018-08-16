package com.tang.customcontrol.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.tang.customcontrol.R;
import com.tang.customcontrol.holder.ItemBehaviorHolder;
import com.tang.customcontrol.net.MeiZhi;
import com.tang.customcontrol.net.MyOkHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Behavior仿百度
 */
public class BehaviorBaiduActivity extends AppCompatActivity {

    public static final String MainUrl = "https://gank.io/api/data/福利/10/";

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private List<MeiZhi> mList = new ArrayList<>();
    @Bind(R.id.bottom_sheet_iv)
    ImageView mIv;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior_baidu);
        ButterKnife.bind(this);
        initData();
    }

    private StaggeredGridLayoutManager layoutManager;
    private int page = 1;
    private ItemAdapter itemAdapter;
    private int lastVisibleItemPosition;
    private BottomSheetBehavior behavior;

    private void initData() {
//        refreshLayout.setColorSchemeColors();
//        refreshLayout.setProgressViewOffset();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                new Task().execute(MainUrl + page);
            }
        });

        itemAdapter = new ItemAdapter();
        layoutManager = new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] lastPosition = layoutManager.findLastVisibleItemPositions(null);
                lastVisibleItemPosition = Math.max(lastPosition[0], lastPosition[1]);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 2 >= layoutManager.getItemCount()) {
                    page++;
                    new Task().execute(MainUrl + page);
                }
            }
        });

        new Task().execute(MainUrl + page);

        //add
        behavior = BottomSheetBehavior.from(mIv);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        mIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);//折叠状态
            }
        });
    }

    private class Task extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            refreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {
            return MyOkHttp.get(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (TextUtils.isEmpty(result)) return;
            try {
                String jsonData = new JSONObject(result).getString("results");
                List<MeiZhi> meiZhis = parse(jsonData);
                if (meiZhis != null) {
                    refreshLayout.setRefreshing(false);
                    mList.addAll(meiZhis);
                    itemAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private List<MeiZhi> parse(String jsonData) {
            return new Gson().fromJson(jsonData, new TypeToken<List<MeiZhi>>() {
            }.getType());
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemBehaviorHolder> {

        @Override
        public ItemBehaviorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemBehaviorHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ItemBehaviorHolder holder, final int position) {
            Picasso.with(BehaviorBaiduActivity.this)
                    .load(getItem(position).getUrl())
                    .into(holder.getTvIv());

            holder.getTvId().setText(String.valueOf(position));
            //add
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(getWindow().getDecorView(), "position:" + position, Snackbar.LENGTH_INDEFINITE).show();
                    Picasso.with(BehaviorBaiduActivity.this)
                            .load(getItem(position).getUrl())
                            .into(mIv);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);//折叠状态
                }
            });
        }

        private MeiZhi getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    private boolean isSetBottomSheetHeight = false;

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //修改SetBottomSheet的高度 留出顶部工具栏的位置
        if (!isSetBottomSheetHeight) {
            CoordinatorLayout.LayoutParams linearParams = (CoordinatorLayout.LayoutParams) mIv.getLayoutParams();
            linearParams.height = coordinatorLayout.getHeight() - mIv.getHeight();
            mIv.setLayoutParams(linearParams);
            isSetBottomSheetHeight = true;
        }
    }

}