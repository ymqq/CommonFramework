package cn.ffcs.ms.crm_mobile_v20.main.query;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.ffcs.itbg.itpd.core.Base.BaseFragment;
import cn.ffcs.ms.crm_mobile_v20.MyApplication;
import cn.ffcs.ms.crm_mobile_v20.R;
import cn.ffcs.ms.crm_mobile_v20.entities.OrderModel;
import cn.ffcs.ms.crm_mobile_v20.greendao.gen.OrderDao;

/**
 * Created by chenqq on 16-12-15.
 */

public class MainTabQueryFragment extends BaseFragment implements MainTabQueryConstract.View, View.OnClickListener {

    private MainTabQueryConstract.Presenter mPresenter;

    private TextView query_tab_today_tv, query_tab_lastWeek_tv, query_tab_selectDate_tv;
    private View query_datepicker_container;
    private TextView query_startDate_tv, query_endDate_tv;
    private TextView query_successedRegister, query_failedRegister, query_preRegister;

    private Calendar mStartDate, mEndDate;

    @Override
    protected int setLayout() {
        return R.layout.fragment_main_query;
    }

    @Override
    protected void initViews() {
        query_tab_today_tv = (TextView) findViewById(R.id.query_tab_today_tv);
        query_tab_lastWeek_tv = (TextView) findViewById(R.id.query_tab_lastWeek_tv);
        query_tab_selectDate_tv = (TextView) findViewById(R.id.query_tab_selectDate_tv);

        query_datepicker_container = findViewById(R.id.query_datepicker_container);

        query_startDate_tv = (TextView) findViewById(R.id.query_startDate_tv);
        query_endDate_tv = (TextView) findViewById(R.id.query_endDate_tv);

        query_successedRegister = (TextView) findViewById(R.id.query_successedRegister);
        query_failedRegister = (TextView) findViewById(R.id.query_failedRegister);
        query_preRegister = (TextView) findViewById(R.id.query_preRegister);

        toggleDateSelectView(View.GONE);
    }

    @Override
    protected void initEvents() {
        query_tab_today_tv.setOnClickListener(this);
        query_tab_lastWeek_tv.setOnClickListener(this);
        query_tab_selectDate_tv.setOnClickListener(this);

        query_startDate_tv.setOnClickListener(this);
        query_endDate_tv.setOnClickListener(this);

        query_successedRegister.setOnClickListener(this);
        query_failedRegister.setOnClickListener(this);
        query_preRegister.setOnClickListener(this);

        findViewById(R.id.query_dateQuery_btn).setOnClickListener(this);
    }

    @Override
    protected void start() {
        initPresenter();
        initData();

        loadOrdersCount();
    }

    @Override
    public void showOrdersByStatus(int status) {
        showSnackbar("打开：" + status + " 状态数据页面");
    }

    @Override
    public void showOrdersCount(int successedCount, int failedCount, int preCount) {
        query_successedRegister.setText("有效登记： " + successedCount + " 次");
        query_failedRegister.setText("无效登记： " + failedCount + " 次");
        query_preRegister.setText("预登记： " + preCount + " 次");
    }

    @Override
    public void showMessage(String message) {
        showSnackbar(message);
    }

    @Override
    public void initPresenter() {
        OrderDao orderDao = MyApplication.INSTANCE.getDaoSession().getOrderDao();
        mPresenter = new MainTabQueryPresenter(this, new OrderModel(orderDao));
        mPresenter.start();
    }

    private void initData() {
        mStartDate = Calendar.getInstance();
        mEndDate = Calendar.getInstance();

        query_startDate_tv.setText(formatDate("yyyy/MM/dd", mStartDate.getTime()));
        query_endDate_tv.setText(formatDate("yyyy/MM/dd", mEndDate.getTime()));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.query_tab_today_tv:
                selectTab(id);
                loadOrdersCount();
                break;
            case R.id.query_tab_lastWeek_tv:
                selectTab(id);
                loadOrdersCount();
                break;
            case R.id.query_tab_selectDate_tv:
                selectTab(id);
                break;
            case R.id.query_startDate_tv:
                selectDate(true);
                break;
            case R.id.query_endDate_tv:
                selectDate(false);
                break;
            case R.id.query_successedRegister:
                showOrdersByStatus(200);
                break;
            case R.id.query_failedRegister:
                showOrdersByStatus(400);
                break;
            case R.id.query_preRegister:
                showOrdersByStatus(300);
                break;
            case R.id.query_dateQuery_btn:
                loadOrdersCount();
                break;
        }
    }

    private void toggleDateSelectView(int visiable) {
        query_datepicker_container.setVisibility(visiable);
    }

    private void selectDate(final boolean isStartDate) {
        // 显示当前选择的日期位置
        Calendar initDate = isStartDate ? mStartDate : mEndDate;
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                if (isStartDate) {
                    mStartDate.set(year, monthOfYear, dayOfMonth);
                    query_startDate_tv.setText(formatDate("yyyy/MM/dd", mStartDate.getTime()));

                    // 如果开始时间在结束时间之后，则将结束时间重置为与开始时间一致，保证时间选择的正确性。
                    if (mStartDate.getTimeInMillis() > mEndDate.getTimeInMillis()) {
                        mEndDate.set(year, monthOfYear, dayOfMonth);
                        query_endDate_tv.setText(formatDate("yyyy/MM/dd", mEndDate.getTime()));
                    }
                } else {
                    mEndDate.set(year, monthOfYear, dayOfMonth);
                    query_endDate_tv.setText(formatDate("yyyy/MM/dd", mEndDate.getTime()));
                }
            }
        }, initDate.get(Calendar.YEAR), initDate.get(Calendar.MONTH), initDate.get(Calendar.DAY_OF_MONTH));

        // 设置时间选择范围：最近6个月，如果是开始时间则以当天往回六个月，如果是结束时间则以开始时间到今天为止。
        Calendar today = Calendar.getInstance();
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMaxDate(today.getTimeInMillis());
        if (isStartDate) {
            Calendar beforeSixMonth = Calendar.getInstance();
            beforeSixMonth.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH) - 6, today.get(Calendar.DAY_OF_MONTH));
            datePicker.setMinDate(beforeSixMonth.getTimeInMillis());
        } else {
            datePicker.setMinDate(mStartDate.getTimeInMillis());
        }
        // 取消标题
        datePickerDialog.setTitle(null);
        datePickerDialog.show();
    }

    private String formatDate(String pattern, Date date) {
        return new SimpleDateFormat(pattern).format(date);
    }

    private void selectTab(int id) {
        resetTabs();
        switch (id) {
            case R.id.query_tab_today_tv:
                query_tab_today_tv.setTextColor(getColor(R.color.colorAccent));

                toggleDateSelectView(View.GONE);
                mEndDate = Calendar.getInstance();
                mStartDate = Calendar.getInstance();
                break;
            case R.id.query_tab_lastWeek_tv:
                query_tab_lastWeek_tv.setTextColor(getColor(R.color.colorAccent));

                toggleDateSelectView(View.GONE);
                // 近一周，以今天往回算6天
                mEndDate = Calendar.getInstance();
                mStartDate.set(mEndDate.get(Calendar.YEAR), mEndDate.get(Calendar.MONTH), mEndDate.get(Calendar.DAY_OF_MONTH) - 6);
                break;
            case R.id.query_tab_selectDate_tv:
                query_tab_selectDate_tv.setTextColor(getColor(R.color.colorAccent));

                toggleDateSelectView(View.VISIBLE);
                break;
        }
    }

    private void resetTabs() {
        query_tab_today_tv.setTextColor(getColor(R.color.colorPrimary));
        query_tab_lastWeek_tv.setTextColor(getColor(R.color.colorPrimary));
        query_tab_selectDate_tv.setTextColor(getColor(R.color.colorPrimary));
    }

    private void loadOrdersCount() {
        mPresenter.loadOrdersCount(mStartDate.getTime(), mEndDate.getTime());
    }
}
