package cn.ffcs.ms.crm_mobile_v20.main.query;

import java.util.Date;

import cn.ffcs.itbg.itpd.core.Base.BaseMvpPresenter;
import cn.ffcs.itbg.itpd.core.Base.BaseMvpView;


/**
 * Created by Vic on 16/12/23.
 */

public interface MainTabQueryConstract {

    interface View extends BaseMvpView<Presenter> {
        void showOrdersByStatus(int status);
        void showOrdersCount(int successedCount, int failedCount, int preCount);
        void showMessage(String message);
    }

    interface Presenter extends BaseMvpPresenter {
        void loadOrdersCount(Date startDate, Date endDate);
    }
}
