package cn.ffcs.itbg.itpd.core.Base;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.view.Window;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;

import cn.ffcs.itbg.itpd.core.R;

/**
 * @Desc:
 * @Author: Tyras on 2017/4/13 22:59.
 */

public class LoadMultidexActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.null_anim, R.anim.null_anim);
        setContentView(R.layout.activity_load_multidex);
        new LoadDexTask().execute();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void start() {

    }

    class LoadDexTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                MultiDex.install(getApplication());
                Logger.d("loadDex install finish");
                BaseApplication.INSTANCE.installFinish(getApplication());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            Logger.d("get install finish");
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onBackPressed() {
        // 禁止返回关闭
    }
}
