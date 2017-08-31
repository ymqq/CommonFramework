package cn.ffcs.itbg.itpd.core.Base;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

import java.io.File;
import java.lang.ref.SoftReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import cn.ffcs.itbg.itpd.core.Utils.AndroidUtils;
import cn.ffcs.itbg.itpd.core.Utils.StringUtils;

/**
 * 1、实现静态实例“INSTANCE”，并且转换为具体实现类的实例类型；
 * 2、实现Acitivity管理，主要时为了实现App退出时，能够清空所有启动的Activity，以便完全退出App；
 * 3、实现Application管理数据缓存，主要时内存数据缓存；
 * 4、提供Gson实例，全局可以使用该实例，减少重复的new操作，提高效率；
 * 5、实现APP异常奔溃信息收集，需要时可以启动该功能，并且实现“uploadCrashLog”抽象方法，对Crash日志文件进行上传处理；
 * 6、
 * <p>
 * Created by chenqq on 17/2/28.
 */

public abstract class BaseApplication extends Application {

    public static BaseApplication INSTANCE;

    // 新增管理Activities列表，主要是为了完全退出APP
    protected List<SoftReference<BaseAppCompatActivity>> mActivities;

    // 内存数据缓存
    protected Map<String, String> mDataCacheMap;
    // SharedPreferences存储
    protected SharedPreferences mSp;
    protected SharedPreferences.Editor mEditor;

    // Gson实例
    protected Gson mGson;

    // 崩溃日志处理实例
    protected CrashCaptureHandler mCrashCaptureHandler;

    // Multidex方案处理
    // 用于标记是否已经加载dex2包
    public static final String KEY_DEX2_SHA1 = "dex2-SHA1-Digest";


    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;

        mActivities = new ArrayList<>();

        mDataCacheMap = new HashMap<>();

        mGson = new Gson();

        Logger.init();
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
     * 上传Crash日志文件的具体实现，需要继承者实现
     *
     * @param files
     */
    protected abstract void uploadCrashLog(File[] files);

    /**
     * 采用protected方式定义，继承时再显示的控制，减少Base类中不必要的属性声明。
     */
    protected void setupCrashCaptureHandler() {
        mCrashCaptureHandler = CrashCaptureHandler.getInstance();
        mCrashCaptureHandler.init(this);

        // 判断是否需要上传Crash日志文件
        File file = mCrashCaptureHandler.getCrashLogsDir(this);
        if (file.listFiles().length > 0) {
            uploadCrashLog(file.listFiles());
        }
    }

    /**
     * 根据key获取缓存数据字符串
     *
     * @param key
     * @return
     */
    public String getCacheData(String key) {
        Logger.i("getCacheData by key: " + key);
        return mDataCacheMap.get(key);
    }

    /**
     * 根据key获取缓存数据，并转换为指定type的类型List数据
     *
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    public <T> List<T> getCacheData(String key, Type type) {
        String temp = getCacheData(key);
        if (TextUtils.isEmpty(temp)) {
            Logger.e("CacheList is null!");
            return null;
        } else {
            List<T> cacheList = mGson.fromJson(temp, type);
            Logger.i("CacheList size: " + cacheList.size());
            return cacheList;
        }
    }

    /**
     * 根据key获取缓存数据，并转换为指定clazz的类型对象数据
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getCacheData(String key, Class<T> clazz) {
        String temp = getCacheData(key);
        if (TextUtils.isEmpty(temp)) {
            Logger.e("CacheObject is null!");
            return null;
        } else {
            T cacheObj = mGson.fromJson(getCacheData(key), clazz);
            Logger.i("CacheObject: " + cacheObj.toString());
            return cacheObj;
        }
    }

    /**
     * 按照key值，保存字符串数据
     *
     * @param key
     * @param data
     */
    public void putCacheData(String key, String data) {
        Logger.i("putCacheData by key: " + key);
        if (TextUtils.isEmpty(data)) {
            Logger.e("Null/Empty String data cannot add to cache!");
        } else {
            mDataCacheMap.put(key, data);
        }
        Logger.i("putCacheData Successed!");
    }

    /**
     * 按照key值，保存对象数据
     *
     * @param key
     * @param data
     */
    public void putCacheData(String key, Object data) {
        Logger.i("putCacheData by key: " + key);
        if (data == null) {
            Logger.e("Null data cannot add to cache!");
        } else {
            mDataCacheMap.put(key, mGson.toJson(data));
        }
        Logger.i("putCacheData Successed!");
    }

    /**
     * 按照key值，保存List数据
     *
     * @param key
     * @param list
     * @param <T>
     */
    public <T> void putCacheData(String key, List<T> list) {
        int size = list.size();
        List<T> newList = list.subList(0, size);
        if (size > 0) {
            putCacheData(key, newList);
            Logger.i("List size: " + size);
        } else {
            Logger.i("Empty list data cannot add to cache!");
        }
    }

    /**
     * 启动全局使用的SharedPreferences
     *
     * @param name
     */
    public void setupSharedPreference(String name) {
        mSp = getSharedPreferences(name, MODE_PRIVATE);
        mEditor = mSp.edit();
    }

    /**
     * 获取全局使用的SharedPreferences实例
     *
     * @return
     */
    public SharedPreferences getSp() {
        return mSp;
    }

    /**
     * 获取全局使用的SharedPreferences.Editor实例
     *
     * @return
     */
    public SharedPreferences.Editor getEditor() {
        return mEditor;
    }

    /**
     * 启动定制的Logger，用于覆盖默认配置
     *
     * @param tag
     * @param methodCount
     * @param methodOffset
     * @param isOpen
     */
    public void setupLogger(String tag, int methodCount, int methodOffset, boolean isOpen) {
        LogLevel level = isOpen ? LogLevel.FULL : LogLevel.NONE;
        Logger
                .init(tag)                          // default PRETTYLOGGER or use just init()
                .methodCount(methodCount)           // default 2
                .logLevel(level)                    // default LogLevel.FULL
                .methodOffset(methodOffset);        // default 0
    }

    /**
     * 获取Gson实例
     *
     * @return
     */
    public Gson getGson() {
        return mGson;
    }


    /**
     * 5.0 以下手机需要处理
     * 5.0 以上使用ART机制，Android系统自己处理
     *
     * @param context
     */
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        Logger.i("attachBaseContext");
        if (!quickStart() && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (needWait(context)) {
                waitForDexOpt(context);
            }
            MultiDex.install(this);
        }
    }

    /**
     * 判断"mini"进程是否在运行
     *
     * @return
     */
    private boolean quickStart() {
        if (AndroidUtils.getProcessName(this).contains(":mini")) {
            Logger.i(":mini thread start!");
            return true;
        }
        return false;
    }

    /**
     * 判断是否已经加载过dex2
     *
     * @param context
     * @return
     */
    private boolean needWait(Context context) {
        String flag = get2thDexSHA1(context);
        Logger.i("dex2-sha1: %s ", flag);
        SharedPreferences sp = context.getSharedPreferences(AndroidUtils.getPackageInfo(context).versionName, MODE_MULTI_PROCESS);
        String saveValue = sp.getString(KEY_DEX2_SHA1, "");
        return !flag.equals(saveValue);
    }

    /**
     * 获取dex2文件的SHA1-Digest值
     *
     * @param context
     * @return
     */
    private String get2thDexSHA1(Context context) {
        ApplicationInfo info = context.getApplicationInfo();
        String source = info.sourceDir;
        try {
            JarFile jar = new JarFile(source);
            Manifest mf = jar.getManifest();
            Map<String, Attributes> map = mf.getEntries();
            Attributes a = map.get("classes2.dex");
            return a.getValue("SHA1-Digest");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * dex2安装完成，记录对应的SHA1-Digest值到SharedPrefecrences中
     * 这里需要对每个版本都记录相关的值，所以使用了versionName
     * 但是会又一个问题，原来记录的旧值，会造成死数据。
     *
     * @param context
     */
    public void installFinish(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AndroidUtils.getPackageInfo(context).versionName, MODE_MULTI_PROCESS);
        sp.edit().putString(KEY_DEX2_SHA1, get2thDexSHA1(context));
    }

    /**
     * 需要继承者设置具体的App包名
     * <p>
     * 原本想使用getPackageName，但是Context中定义了这个abstract
     *
     * @return
     */
    protected abstract String getAppPackageName();

    /**
     * 这里将启动一个“mini”进程的LoadMultidexActivity
     * 进而时当前的Application进入到后台进程，从而可以使用睡眠方式，堵塞线程，而不会造成
     * ANR，待LoadMultidexActivity启动子线程加载完dex2后，返回到Application，就可以
     * 继续后续的启动流程。
     *
     * @param context
     */
    private void waitForDexOpt(Context context) {
        Intent intent = new Intent();
        String packageName = getAppPackageName();
        packageName = StringUtils.isEmpty(packageName) ? getPackageName() : packageName;
        ComponentName componentName = new ComponentName(packageName, LoadMultidexActivity.class.getName());
        intent.setComponent(componentName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        long startWait = System.currentTimeMillis();
        long waitTime = 10 * 1000;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            waitTime = 20 * 1000;
        }
        while (needWait(context)) {
            try {
                long nowWait = System.currentTimeMillis() - startWait;
                Logger.i("wait ms: " + nowWait);
                if (nowWait >= waitTime) {
                    return;
                }
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 启动LeakCanary性能监测
     * 该方法应该尽早的调用，以便能够监测到更多的程序内容
     */
    protected void setupLeakCanary() {
        // 这里需要先挂载LeakCanary的heap analysis 进程
        // 如果挂载失败是不能使用LeakCanary的。
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
