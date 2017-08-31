package cn.ffcs.ms.crm_mobile_v20.main.query;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.ffcs.ms.crm_mobile_v20.entities.IOrderModel;
import cn.ffcs.ms.crm_mobile_v20.entities.Order;
import cn.ffcs.ms.crm_mobile_v20.entities.OrderModel;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

/**
 * Created by Vic on 16/12/23.
 */

public class MainTabQueryPresenter implements MainTabQueryConstract.Presenter {

    private MainTabQueryConstract.View mView;
    private OrderModel mOrderModel;

    public MainTabQueryPresenter(MainTabQueryConstract.View view, OrderModel orderModel) {
        mView = view;
        mOrderModel = orderModel;
    }

    @Override
    public void loadOrdersCount(Date startDate, Date endDate) {
        mOrderModel.getOrdersCountWithDateZone(startDate, endDate, new IOrderModel.GetOrdersCallback() {
            @Override
            public void onSuccessed(List<Order> orders) {
                HashMap<Integer, Integer> map = new HashMap<>();
                for (Order order : orders) {
                    int status = order.getStatus();
                    map.put(status, map.containsKey(status) ? map.get(status) + 1 : 1);
                }

                Integer successedCount = map.get(200);
                Integer failedCount = map.get(400);
                Integer preCount = map.get(300);
                successedCount = successedCount == null ? 0 : successedCount;
                failedCount = failedCount == null ? 0 : failedCount;
                preCount = preCount == null ? 0 : preCount;
                mView.showOrdersCount(successedCount.intValue(), failedCount.intValue(), preCount.intValue());
            }

            @Override
            public void onFailed(String err) {
                mView.showMessage(err);
            }
        });
    }

    @Override
    public void start() {

    }
}
