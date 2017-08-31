package cn.ffcs.itbg.itpd.androidbase;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.io.File;
import java.lang.ref.SoftReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenqq on 17/2/28.
 * <p>
 * 实现APP异常奔溃信息收集的Application，需要用到可以进行继承，并且可对Crash日志文件进行上传处理（需要实现）
 */

public abstract class BaseApplication<T extends BaseApplication> extends Application {

    protected static final String TAG = "Application";

    public static BaseApplication INSTANCE;

    // 新增管理Activities列表，主要是为了完全退出APP
    protected List<SoftReference<BaseAppCompatActivity>> mActivities;

    // 内存数据缓存
    protected Map<String, String> mDataCacheMap;

    // Gson实例
    protected Gson mGson;

    // 崩溃日志处理实例
    protected CrashCaptureHandler mCrashCaptureHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = (T) this;

        mActivities = new ArrayList<>();

        mGson = new Gson();

    }

    /**
     * 添加Activity
     *
     * @param activity
     */
    public void addActivity(BaseAppCompatActivity activity) {
        mActivities.add(new SoftReference<>(activity));
    }

    /**
     * 删除Activity
     *
     * @param activity
     */
    public void removeActivity(BaseAppCompatActivity activity) {
        for (SoftReference<BaseAppCompatActivity> reference : mActivities) {
            BaseAppCompatActivity act = reference.get();
            if (act != null && act == activity) {
                mActivities.remove(reference);
                return;
            }
        }
    }

    /**
     * 完全退出应用程序
     */
    public void exitApp() {
        for (SoftReference<BaseAppCompatActivity> reference : mActivities) {
            Activity activity = reference.get();
            if (activity != null) {
                activity.finish();
            }
            mActivities.remove(reference);
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
        System.gc();
    }

    /**
     * 获取用于展示PopView的rootview
     *
     * @return
     * @throws Exception
     */
    public View getRootView() throws Exception {
        Activity activity = mActivities.get(mActivities.size() - 1).get();
        if (activity != null) {
            return activity.getWindow().getDecorView();
        }
        throw new Exception("获取最顶层Acitivity失败！");
    }

    /**
     * 获取用于展示PopView的rootview
     *
     * @param activity
     * @return
     */
    public View getRootView(Activity activity) {
        return activity.getWindow().getDecorView();
    }


    /**
     * 上传Crash日志文件的具体实现，需要继承者实现。
     *
     * @param files
     */
    protected abstract void uploadCrashLog(File[] files);

    /**
     * 采用protected方式定义，继承时再显示的控制，减少Base类中不必要的属性声明。
     */
    protected void setupCrashCaptureHandler() {
        mCrashCaptureHandler = CrashCaptureHandler.getInstance();
        mCrashCaptureHandler.init(INSTANCE);

        // 判断是否需要上传Crash日志文件
        File file = mCrashCaptureHandler.getCrashLogsDir(INSTANCE);
        if (file.listFiles().length > 0) {
            uploadCrashLog(file.listFiles());
        }
    }

    protected void setupCacheManager() {
        mDataCacheMap = new HashMap<>();
    }
    public String getCacheData(String key) {
        Log.i(TAG, "getCacheData by key: " + key);
        return mDataCacheMap.get(key);
    }

    public <T> List<T> getCacheDataAsList(String key, Type type) {
        String temp = getCacheData(key);
        if (TextUtils.isEmpty(temp)) {
            Log.i(TAG, "CacheList is null!");
            return null;
        } else {
            List<T> cacheList = mGson.fromJson(temp, type);
            Log.i(TAG, "CacheList size: " + cacheList.size());
            return cacheList;
        }
    }

    public <T> T getCacheDataAsObject(String key, Class<T> clazz) {
        String temp = getCacheData(key);
        if (TextUtils.isEmpty(temp)) {
            Log.i(TAG, "CacheObject is null!");
            return null;
        }else {
            T cacheObj = mGson.fromJson(getCacheData(key), clazz);
            Log.i(TAG, "CacheObject: " + cacheObj.toString());
            return cacheObj;
        }
    }

    public void putCacheData(String key, Object data) {
        Log.i(TAG, "putCacheData by key: " + key);
        if (data == null) {
            Log.i(TAG, "Null data cannot add to cache!");
        } else {
            mDataCacheMap.put(key, mGson.toJson(data));
        }
        Log.i(TAG, "putCacheData Successed!");
    }

    public <T> void putCacheDataByList(String key, List<T> list) {
        int size = list.size();
        List<T> newList = list.subList(0, size);
        if (size > 0) {
            putCacheData(key, newList);
            Log.i(TAG, "List size: " + size);
        } else {
            Log.i(TAG, "Empty list data cannot add to cache!");
        }
    }

    public Gson getGson() {
        return mGson;
    }
}
