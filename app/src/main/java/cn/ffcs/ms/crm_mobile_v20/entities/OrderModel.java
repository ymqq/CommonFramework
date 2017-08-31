package cn.ffcs.ms.crm_mobile_v20.entities;

import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.ffcs.ms.crm_mobile_v20.greendao.gen.OrderDao;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Vic on 16/12/28.
 */

public class OrderModel implements IOrderModel {

    private OrderDao mDao;

    public OrderModel(OrderDao dao) {
        mDao = dao;
    }

    public void addOrder(Order order) {
        if (mDao.load(order.getId()) == null) {
            mDao.insert(order);
        } else {
            mDao.update(order);
        }
    }

    public void addOrders(List<Order> orders) {
        for (Order order : orders) {
            addOrder(order);
        }
    }


    @Override
    public void getOrderById(@NonNull final Long orderId, @NonNull final GetOrderCallback callback) {
        mDao.rx()
                .load(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(5, TimeUnit.SECONDS)
                .subscribe(new Action1<Order>() {
                    @Override
                    public void call(Order order) {
                        if (order != null) {
                            callback.onSuccessed(order);
                        } else {
                            callback.onFailed("未查到OrderId【" + orderId + "】对应的数据！");
                        }
                    }
                });
    }

    @Override
    public void getOrdersWithDateZone(@NonNull final Date startDate, @NonNull Date endDate, @NonNull final GetOrdersCallback callback) {
        startDate.setHours(0);
        startDate.setMinutes(0);
        startDate.setMinutes(0);
        startDate.setSeconds(0);

        endDate.setHours(23);
        endDate.setMinutes(59);
        endDate.setMinutes(59);
        endDate.setSeconds(59);
        mDao.queryBuilder()
                .where(OrderDao.Properties.Time.ge(startDate), OrderDao.Properties.Time.le(endDate))
                .orderDesc(OrderDao.Properties.Time)
                .rxPlain()
                .list()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Order>>() {
                    @Override
                    public void call(List<Order> orders) {
                        if (orders != null && orders.size() > 0) {
                            callback.onSuccessed(orders);
                        } else {
                            callback.onFailed("无该时间区域内的Order数据！");
                        }
                    }
                });
    }

    @Override
    public void getOrdersCountWithDateZone(@NonNull Date startDate, @NonNull Date endDate, @NonNull final GetOrdersCallback callback) {
        startDate.setHours(0);
        startDate.setMinutes(0);
        startDate.setMinutes(0);
        startDate.setSeconds(0);

        endDate.setHours(23);
        endDate.setMinutes(59);
        endDate.setMinutes(59);
        endDate.setSeconds(59);
        mDao.queryBuilder()
                .where(OrderDao.Properties.Time.ge(startDate), OrderDao.Properties.Time.le(endDate))
                .orderDesc(OrderDao.Properties.Time)
                .rxPlain()
                .list()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Order>>() {
                    @Override
                    public void call(List<Order> orders) {
                        if (orders != null && orders.size() > 0) {
                            callback.onSuccessed(orders);
                        } else {
                            callback.onFailed("无该时间区域内的Order数据！");
                        }
                    }
                });
    }
}
