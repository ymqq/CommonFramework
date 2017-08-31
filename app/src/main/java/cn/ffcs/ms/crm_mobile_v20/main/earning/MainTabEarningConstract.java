package cn.ffcs.ms.crm_mobile_v20.main.earning;

import cn.ffcs.itbg.itpd.core.Base.BaseMvpPresenter;
import cn.ffcs.itbg.itpd.core.Base.BaseMvpView;


/**
 * Created by Vic on 16/12/23.
 */

public interface MainTabEarningConstract {

    interface View extends BaseMvpView<Presenter> {
        void showLastMonthEarnings(String earnings);
        void showDailyEarningsList(String[][] list);
        void openLastMonthEarningsDetailList();
        void openDaliyEarningsDetailList(String date);
        void showMessage(String message);
    }

    interface Presenter extends BaseMvpPresenter {
        void loadLastMonthEarnings();
        void loadDailyEarnings();
    }
}
