package cn.ffcs.ms.crm_mobile_v20.main.earning;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import cn.ffcs.ms.crm_mobile_v20.entities.IOrderModel;
import cn.ffcs.ms.crm_mobile_v20.entities.Order;
import cn.ffcs.ms.crm_mobile_v20.entities.OrderModel;

/**
 * Created by Vic on 16/12/23.
 */

public class MainTabEarningPresenter implements MainTabEarningConstract.Presenter {

    private MainTabEarningConstract.View mView;
    private OrderModel mOrderModel;

    public MainTabEarningPresenter(MainTabEarningConstract.View view, OrderModel orderModel) {
        mView = view;
        mOrderModel = orderModel;
    }

    @Override
    public void loadLastMonthEarnings() {
        Calendar calendar = Calendar.getInstance();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        mOrderModel.getOrdersWithDateZone(startCalendar.getTime(), calendar.getTime(), new IOrderModel.GetOrdersCallback() {
            @Override
            public void onSuccessed(List<Order> orders) {
                int earnings = 0;
                for (Order order : orders) {
                    if (order.getStatus() == 200) {
                        earnings += order.getEarning();
                    }
                }
                mView.showLastMonthEarnings(new DecimalFormat("0.00").format(earnings / 100.0f));
            }

            @Override
            public void onFailed(String err) {
                mView.showMessage(err);
            }
        });
    }

    @Override
    public void loadDailyEarnings() {
        Calendar calendar = Calendar.getInstance();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 5);
        mOrderModel.getOrdersWithDateZone(startCalendar.getTime(), calendar.getTime(), new IOrderModel.GetOrdersCallback() {
            @Override
            public void onSuccessed(List<Order> orders) {
                Map<String, Integer> countMap = new TreeMap<String, Integer>();
                Map<String, Integer> earningsMap = new TreeMap<String, Integer>();
                for (Order order : orders) {
                    if (order.getStatus() == 200) {
                        String time = new SimpleDateFormat("yyyy-MM-dd").format(order.getTime());
                        countMap.put(time, countMap.containsKey(time) ? countMap.get(time) + 1 : 1);
                        earningsMap.put(time, earningsMap.containsKey(time) ? earningsMap.get(time) + order.getEarning() : order.getEarning());
                    }
                }

                int i = 0;
                int size = countMap.size();
                String[][] list = new String[size][3];
                for (Map.Entry<String, Integer> m : countMap.entrySet()) {
                    list[i][0] = m.getKey();
                    list[i][1] = m.getValue().toString();
                    list[i][2] = new DecimalFormat("0.00").format(earningsMap.get(m.getKey()) / 100.0f);
                    i++;
                }
                mView.showDailyEarningsList(list);
            }

            @Override
            public void onFailed(String err) {
                mView.showMessage(err);
            }
        });
    }

    @Override
    public void start() {
        loadLastMonthEarnings();
        loadDailyEarnings();
    }
}
