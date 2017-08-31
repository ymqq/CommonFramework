package cn.ffcs.ms.crm_mobile_v20.others;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.ffcs.itbg.itpd.core.Base.BaseAppCompatActivity;
import cn.ffcs.itbg.itpd.core.RecyclerView.RefreshRecyclerView;
import cn.ffcs.ms.crm_mobile_v20.R;


/**
 * Created by chenqq on 16-12-15.
 */

public class AppRecomendActivity extends BaseAppCompatActivity {

    private RefreshRecyclerView refreshRecyclerView;
    private LinearLayoutManager mLayouManager;
    private SampleAdapter adapter;
    private List<String> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_app_recomend);

        refreshRecyclerView = (RefreshRecyclerView) findViewById(R.id.refreshRecyclerView);
        mLayouManager = new LinearLayoutManager(this);
        refreshRecyclerView.setLayoutManager(mLayouManager);
        refreshRecyclerView.setItemAnimator(new DefaultItemAnimator());

        View headerView = LayoutInflater.from(this).inflate(R.layout.refresh_header_view, null);
        View footerView = LayoutInflater.from(this).inflate(R.layout.refresh_footer_view, null);
        // 必须在setAdapter方法之前设置
        refreshRecyclerView.setHeaderView(headerView);
        refreshRecyclerView.setFooterView(footerView);

        adapter = new SampleAdapter();
        refreshRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initEvents() {
        refreshRecyclerView.setOnPullActionListener(new RefreshRecyclerView.OnPullActionListener() {
            public void onPullDown(int state) {
                if (state == RefreshRecyclerView.REFRESH_STATE_REFRESHING) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        refreshRecyclerView.refreshed(true);
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
            public void onPullUp() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refreshRecyclerView.alreadyLoadedAll(false);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    protected void start() {
        initData();
    }

    private void initData() {
        list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add("下拉刷新测试：" + i);
        }

        adapter.setList(list);
    }

    public class SampleAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        private List<String> list;

        public void setList(List<String> l) {
            list = l;
            notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(AppRecomendActivity.this).inflate(R.layout.list_item_text, parent, false);
            return new ItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            holder.textView.setText(list.get(position));
            // 这里与下拉刷新事件冲突，需要处理，应该可以查考SwipeRefreshLayout
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(AppRecomendActivity.this, "Clicked: "+ list.get(position), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ItemViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.text);
        }
    }
}
