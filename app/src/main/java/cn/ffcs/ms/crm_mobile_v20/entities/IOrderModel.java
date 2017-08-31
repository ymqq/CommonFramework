package cn.ffcs.ms.crm_mobile_v20.entities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;

/**
 * Created by Vic on 16/12/28.
 */

public interface IOrderModel {
    interface GetOrdersCallback {
        void onSuccessed(List<Order> orders);
        void onFailed(String err);
    }

    interface GetOrderCallback {
        void onSuccessed(Order order);
        void onFailed(String err);
    }

    void getOrderById(@NonNull Long orderId, @NonNull GetOrderCallback callback);
    void getOrdersWithDateZone(@NonNull Date startDate, @NonNull Date endDate, @NonNull GetOrdersCallback callback);
    void getOrdersCountWithDateZone(@NonNull Date startDate, @NonNull Date endDate, @NonNull GetOrdersCallback callback);
}
