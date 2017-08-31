package cn.ffcs.itbg.itpd.androidbase;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义程序异常处理器，用于捕获程序代码为处理异常导致App奔溃时，将错误信息保存到日志文件
 * Created by chenqq on 17/2/28.
 */

public class CrashCaptureHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashCaptureHandler";

    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    // CustomCrashHandler实例，采用单例模式
    private static CrashCaptureHandler INSTANCE = new CrashCaptureHandler();
    // Application的上下文
    private Context mContext;
    // 存储设备信息与异常信息
    private Map<String, String> mInfos = new HashMap<>();
    // 格式化日期，作为日志文件名的一部分
    private DateFormat mFormater = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    // 日志保存的文件夹名称
    private static final String FOLDER = "/crash";

    /**
     * 单例模式，构造函数设置为私有
     */
    private CrashCaptureHandler() {
    }

    public static CrashCaptureHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;

        // 获取系统默认的UncaughtExceptionHandler实例
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        // 设置CustomCrashHandler为程序的默认UncaughtExceptionHandler处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 单UncaughtException发生时，会自动转入该方法来进行处理
     *
     * @param t
     * @param e
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!handleException(e) && mDefaultHandler != null) {
            // 如果自定义处理器没有处理该类型错误，则使用系统默认异常处理器进行处理，确保所有异常都能够得到合理的处理
            mDefaultHandler.uncaughtException(t, e);
        } else {
            try {
                // 延迟3后退出程序
                Thread.sleep(3000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义异常处理，收集错误信息，然后保存错误文件等
     *
     * @param ex
     * @return r如果处理了该异常则返回true，否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用Toast提示用户，程序有问题
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉，程序出现异常，即将退出，厂家将会马上修复异常。", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }).start();
        // 收集设备信息
        collectDeviceInfos(mContext);
        // 保存异常信息
        saveCrashInfoToFile(ex);
        return true;
    }

    /**
     * 收集设备相关信息
     *
     * @param context
     */
    private void collectDeviceInfos(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                mInfos.put("versionName", versionName);
                mInfos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "收集App包信息异常", e);
        }

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mInfos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (IllegalAccessException e) {
                Log.e(TAG, "收集设备信息异常", e);
            }
        }
    }

    /**
     * 保存异常信息到日志文件
     *
     * @param ex
     */
    private void saveCrashInfoToFile(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : mInfos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        printWriter.close();
        String result = writer.toString();
        sb.append(result);

        try {
            long timestamp = System.currentTimeMillis();
            String time = mFormater.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            String filePath = getDiskCacheDir(mContext) + FOLDER;
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            FileOutputStream fos = new FileOutputStream(filePath + fileName);
            fos.write(sb.toString().getBytes());
            fos.close();
        } catch (java.io.IOException e) {
            Log.e(TAG, "保存日志文件异常！", e);
        }
    }

    /**
     * 通过context获取APP缓存文件夹路径，用于缓存数据
     *
     * @param context
     * @return
     */
    private String getDiskCacheDir(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    /**
     * 获取奔溃日志保存的文件夹路径
     * @param context
     * @return
     */
    public File getCrashLogsDir(Context context) {
        String logPath = getDiskCacheDir(context) + FOLDER;
        File dir = new File(logPath);
        if (!dir.exists()) {
            dir.mkdir();
        }

        return dir;
    }
}
