package cn.ffcs.ms.crm_mobile_v20.login;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import cn.ffcs.itbg.itpd.diskcache.DiskCacheHelper;
import cn.ffcs.itbg.itpd.http.OkHttpUtils;
import cn.ffcs.ms.crm_mobile_v20.MyApplication;
import cn.ffcs.ms.crm_mobile_v20.entities.Message;
import cn.ffcs.ms.crm_mobile_v20.entities.MessagePackage;
import cn.ffcs.ms.crm_mobile_v20.entities.PROMOT_BUSINESS_LOGIN_REQ;
import cn.ffcs.ms.crm_mobile_v20.entities.User;
import cn.ffcs.ms.crm_mobile_v20.httpUtils.Networks;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by Vic on 16/12/29.
 */

public class LoginPresenter implements LoginConstract.Presenter {

    private LoginConstract.View mView;

    public LoginPresenter(LoginConstract.View view) {
        mView = view;
    }

    @Override
    public void login(final User user, final LoginConstract.LoginCallback callback) {
        Message message = new Message();
        message.setKeyId("MKT_LOGIN");
        message.setKeyValue("MKT_LOGIN");

        PROMOT_BUSINESS_LOGIN_REQ req = new PROMOT_BUSINESS_LOGIN_REQ();
        req.setStaff_CODE("18906919080");
        req.setPassword("123456");

        message.setDetail(req);
        MessagePackage messagePackage = new MessagePackage();
        messagePackage.setMessage(message);

        String data = DiskCacheHelper.getInstance().readData("user");

        if (data != null) {
            mView.showMessage("从缓存读取到的数据： " + data);
            if (callback != null) {
                callback.onSuccessed(user);
            }
            return;
        }

        Networks.login(message, new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("LOGIN ERROR", e.getLocalizedMessage() + "\n" + e.getMessage());
                e.printStackTrace();
                mView.showMessage("登陆失败：" + e.getLocalizedMessage());
            }

            @Override
            public void onNext(String msg) {
                mView.showMessage("登陆成功：" + msg);

                DiskCacheHelper.getInstance().writeData("user", msg);

                if (callback != null) {
                    callback.onSuccessed(user);
                }
            }
        });

//        Networks.getTest(new Observer<Object>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                mView.showMessage("登陆失败：" + e.getLocalizedMessage());
//            }
//
//            @Override
//            public void onNext(Object o) {
//                mView.showMessage(o.toString());
//            }
//        });

//        Observable.create(new Observable.OnSubscribe<User>() {
//            @Override
//            public void call(Subscriber<? super User> subscriber) {
//                if (Objects.equals("test", user.getAccount())
//                        && Objects.equals("test", user.getPassword())) {
//                    subscriber.onNext(user);
//                    subscriber.onCompleted();
//                } else {
//                    subscriber.onError(new Throwable("用户名或密码错误！"));
//                }
//            }
//        }).delay(2, TimeUnit.SECONDS)
//                .subscribe(new Observer<User>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        callback.onFailed(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(User user) {
//                        user.setCertId("350121199909092011");
//                        user.setId(123456L);
//                        user.setName("Tyras");
//                        user.setPhone("18799089989");
//                        callback.onSuccessed(user);
//                    }
//                });
    }

    @Override
    public void start() {

    }
}
