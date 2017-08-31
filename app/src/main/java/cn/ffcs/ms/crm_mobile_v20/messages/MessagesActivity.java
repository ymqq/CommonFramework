package cn.ffcs.ms.crm_mobile_v20.messages;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ffcs.itbg.itpd.core.Base.BaseAppCompatActivity;
import cn.ffcs.ms.crm_mobile_v20.R;

/**
 * Created by chenqq on 16-12-15.
 */

public class MessagesActivity extends BaseAppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private List<String> list;
    private SampleAdapter adapter;
    private LinearLayoutManager mLayouManager;
    private int lastVisibleItem;

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_messages);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        mLayouManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayouManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new SampleAdapter();
        recyclerView.setAdapter(adapter);
    }
    @Override
    protected void initEvents() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                try {
                    Thread.sleep(2000);
                    for (int i = 0; i < 15; i++) {
                        list.add(0, "下拉刷新测试：" + i);
                    }
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount()) {
                    swipeRefreshLayout.setRefreshing(true);
                    try {
                        Thread.sleep(2000);
                        for (int i = 0; i < 15; i++) {
                            list.add("上拉刷新测试：" + i);
                        }
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayouManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    protected void start() {
        initData();

        adapter.setList(list);
    }

    private void initData() {
        list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add("下拉刷新测试：" + i);
        }
    }

    public class SampleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<String> list;

        private static final int TYPE_ITEM = 0;
        private static final int TYPE_FOOTER = 1;

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> l) {
            list = l;
        }

        public SampleAdapter() {
            list = new ArrayList<>();
            notifyDataSetChanged();
        }

        // RecyclerView的count设置为数据总条数+ 1（footerView）
        @Override
        public int getItemCount() {
            return list.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            // 最后一个item设置为footerView
            if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof ItemViewHolder) {
                ((ItemViewHolder) holder).textView.setText(String.valueOf(list
                        .get(position)));
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM) {
                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.list_item_text, null);
                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                return new ItemViewHolder(view);
            }
            // type == TYPE_FOOTER 返回footerView
            else if (viewType == TYPE_FOOTER) {
                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.footerview, null);
                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                return new FooterViewHolder(view);
            }

            return null;
        }

        class FooterViewHolder extends RecyclerView.ViewHolder {

            public FooterViewHolder(View view) {
                super(view);
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
}
