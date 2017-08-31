package cn.ffcs.ms.crm_mobile_v20.login;

import cn.ffcs.itbg.itpd.core.Base.BaseMvpPresenter;
import cn.ffcs.itbg.itpd.core.Base.BaseMvpView;
import cn.ffcs.ms.crm_mobile_v20.entities.User;

/**
 * Created by Vic on 16/12/29.
 */

public interface LoginConstract {

    interface LoginCallback {
        void onSuccessed(User user);
        void onFailed(String err);
    }

    interface View extends BaseMvpView<Presenter> {
        void login(User user);
        void showMessage(String message);
    }

    interface Presenter extends BaseMvpPresenter {
        void login(User user, LoginCallback callback);
    }
}
