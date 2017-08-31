package cn.ffcs.ms.crm_mobile_v20.main.earning;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.ffcs.itbg.itpd.core.Base.BaseFragment;
import cn.ffcs.ms.crm_mobile_v20.MyApplication;
import cn.ffcs.ms.crm_mobile_v20.R;
import cn.ffcs.ms.crm_mobile_v20.entities.OrderModel;
import cn.ffcs.ms.crm_mobile_v20.greendao.gen.OrderDao;

/**
 * Created by chenqq on 16-12-15.
 */

public class MainTabEarningFragment extends BaseFragment implements MainTabEarningConstract.View, View.OnClickListener {
    private MainTabEarningConstract.Presenter mPresenter;

    private TextView earning_lastMonthEarnings_tv;
    private RecyclerView mRecyclerView;

    @Override
    protected int setLayout() {
        return R.layout.fragment_main_earning;
    }

    @Override
    protected void initViews() {
        earning_lastMonthEarnings_tv = (TextView) findViewById(R.id.earning_lastMonthEarnings_tv);

        mRecyclerView = (RecyclerView) findViewById(R.id.earning_lastMonthEarnings_rv);
    }

    @Override
    protected void initEvents() {
        earning_lastMonthEarnings_tv.setOnClickListener(this);
    }

    @Override
    protected void start() {
        initPresenter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.earning_lastMonthEarnings_tv:
                openLastMonthEarningsDetailList();
                break;
        }
    }

    @Override
    public void showLastMonthEarnings(String earnings) {
        earning_lastMonthEarnings_tv.setText("最近一个月累计收益： ￥" + earnings);
    }

    @Override
    public void showDailyEarningsList(String[][] list) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setAdapter(new ListAdapter(list));
    }

    @Override
    public void openLastMonthEarningsDetailList() {
        showSnackbar("打开最近一个月收益列表");
    }

    @Override
    public void openDaliyEarningsDetailList(String date) {
        showSnackbar("打开指定日期的收益列表：" + date);
    }

    @Override
    public void showMessage(String message) {
        showSnackbar(message);
    }

    @Override
    public void initPresenter() {
        OrderDao orderDao = MyApplication.INSTANCE.getDaoSession().getOrderDao();
        mPresenter = new MainTabEarningPresenter(this, new OrderModel(orderDao));
        mPresenter.start();
    }


    class ListAdapter extends RecyclerView.Adapter<ViewHolder> {
        private String[][] mList;

        public ListAdapter(String[][] list) {
            mList = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mActivity).inflate(R.layout.recyclerview_tab_earning_listview_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.date.setText(mList[position][0]);
            holder.count.setText(mList[position][1]);
            holder.earnings.setText(mList[position][2]);
            holder.more.setText(">");
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDaliyEarningsDetailList(mList[position][0]);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.length;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView count;
        public TextView earnings;
        public TextView more;

        public ViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.earning_recyclerview_item_date_tv);
            count = (TextView) itemView.findViewById(R.id.earning_recyclerview_item_count_tv);
            earnings = (TextView) itemView.findViewById(R.id.earning_recyclerview_item_earnings_tv);
            more = (TextView) itemView.findViewById(R.id.earning_recyclerview_item_more_tv);
        }
    }
}
