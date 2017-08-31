package cn.ffcs.itbg.itpd.androidbase;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * Created by chenqq on 16/12/30.
 *
 * Activity需要使用套的一些通用方法工具类
 */

public class ActivityUtils {

    /**
     * 启动Activity
     * @param activity
     * @param clazz
     */
    public static void launchActivity(Activity activity, Class clazz) {
        activity.startActivity(new Intent(activity, clazz));
    }

    /**
     * 带返回的启动Activity
     * @param activity
     * @param clazz
     * @param requestCode
     */
    public static void launchActivityForResult(Activity activity, Class clazz, int requestCode) {
        Intent intent = new Intent(activity, clazz);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 获取用于展示PopView的rootview
     * @param activity
     * @return
     */
    public static View getRootView(Activity activity) {
        return activity.getWindow().getDecorView();
    }


    /**
     * 完全退出应用程序
     */
    public static void exitApp(List<SoftReference<BaseAppCompatActivity>> activities) {
        for (SoftReference<BaseAppCompatActivity> reference : activities) {
            Activity activity = reference.get();
            if (activity != null) {
                activity.finish();
            }
            activities.remove(reference);
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
        System.gc();
    }

}
