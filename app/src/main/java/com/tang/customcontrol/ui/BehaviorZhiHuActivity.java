package com.tang.customcontrol.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

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
 * Behavior仿知乎
 */
public class BehaviorZhiHuActivity extends AppCompatActivity {

    public static final String MainUrl = "https://gank.io/api/data/福利/10/";

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private List<MeiZhi> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior_zhihu);
        ButterKnife.bind(this);
        initData();
    }

    private StaggeredGridLayoutManager layoutManager;
    private int page = 1;
    private ItemAdapter itemAdapter;
    private int lastVisibleItemPosition;

    private void initData() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        public void onBindViewHolder(ItemBehaviorHolder holder, int position) {
            Picasso.with(BehaviorZhiHuActivity.this)
                    .load(getItem(position).getUrl())
                    .into(holder.getTvIv());

            holder.getTvId().setText(String.valueOf(position));
        }

        private MeiZhi getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

}