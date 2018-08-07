package com.tang.customcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tang.customcontrol.ui.CustomViewGroupActivity;
import com.tang.customcontrol.ui.VerticalLinearLayoutActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {

//    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;
//    private List<ItemBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.tv_VerticalLinearLayout)
    public void onTvVerticalLinearLayoutClicked() {
        startActivity(new Intent(this, VerticalLinearLayoutActivity.class));
    }

    @OnClick(R.id.tv_CustomViewGroup)
    public void onTvCustomViewGroupClicked() {
        startActivity(new Intent(this, CustomViewGroupActivity.class));
    }

//    private class ItemAdapter extends RecyclerView.Adapter<ItemSplashHolder> {
//        @Override
//        public ItemSplashHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return new ItemSplashHolder(LayoutInflater.from(parent.getContext()), parent);
//        }
//
//        @Override
//        public void onBindViewHolder(ItemSplashHolder holder, int position) {
//            holder.getTvTitle().setText(getItem(position).title);
//            holder.getTvDesc().setText(getItem(position).desc);
//        }
//
//        private ItemBean getItem(int position) {
//            return mList.get(position);
//        }
//
//        @Override
//        public int getItemCount() {
//            return mList.size();
//        }
//    }
//
//    private static class ItemBean {
//        private String title;
//        private String desc;
//
//        public ItemBean(String title, String desc) {
//            this.title = title;
//            this.desc = desc;
//        }
//    }

}