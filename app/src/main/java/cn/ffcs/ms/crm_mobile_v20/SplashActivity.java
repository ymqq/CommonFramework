package cn.ffcs.ms.crm_mobile_v20;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.ffcs.itbg.itpd.core.Base.BaseAppCompatActivity;
import cn.ffcs.ms.crm_mobile_v20.login.LoginActivity;

/**
 * Created by chenqq on 16-12-15.
 * 用于优化启动APP，同时消除APP启动时，出现的空白页面，影像用户体验。
 *
 * 这里参考方案：Android App优化之提升你的App启动速度之实例挑战（http://www.jianshu.com/p/4f10c9a10ac9）
 */

public class SplashActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, LoginActivity.class));

        finish();
    }

    @Override
    protected void initViews() {
        // null
    }

    @Override
    protected void initEvents() {
        // null
    }

    @Override
    protected void start() {

    }
}
