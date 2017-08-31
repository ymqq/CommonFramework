package cn.ffcs.itbg.itpd.diskcache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by chenqq on 17/1/4.
 * 参考资料：http://blog.csdn.net/guolin_blog/article/details/28863651
 *
 * DiskLruCache的二次封装，方便适用DiskLruCache进行一些数据缓存。
 * 适用单例模式进行链式调用。
 */

public class DiskCacheHelper {

    /**
     * 定义缓存数据类型
     */
    public enum CACHE_TYPE {
        FILE, // 文件类型
        BITMAP, // 图片类型
        DATA // 数据字符串类型
    }

    // 缓存数据不同类型对应的目录
    private final static String[] UNIQUE_NAME = {"file", "bitmap", "data"};
    // 单次最大缓存大小
    private final static int MAX_CACHE_SIZE = 10 * 1024 * 1024;

    // 单例模式适用的实例
    private static DiskCacheHelper mInstance;

    private int mMaxCacheSize = 0;

    // DiskLruCache实例
    private DiskLruCache mDiskLruCache = null;


    private DiskCacheHelper() {
    }

    /**
     * 获取单例实例
     * @return
     */
    public static DiskCacheHelper getInstance() {
        if (mInstance == null) {
            mInstance = new DiskCacheHelper();
        }
        return mInstance;
    }

    /**
     * 打开DiskLruCache，然后才能进行Cache操作，该方法只需执行一次，多次执行无效。
     * @param context Context 上下文环境
     * @param cacheType CACHE_TYPE 缓存数据类型
     * @param maxCacheSize int 自定义最大缓存，0~MAX_CACHE_SIZE（10M）之间，<=0则使用默认的最大值
     * @return 返回单例实例，提供链式调用支持
     */
    public DiskCacheHelper open(Context context, CACHE_TYPE cacheType, int maxCacheSize) {
        if (mInstance != null && mDiskLruCache != null) {
            return mInstance;
        }
        try {
            File cacheDir = getDiskCacheDir(context, cacheType);

            if (maxCacheSize > 0 && maxCacheSize <= MAX_CACHE_SIZE) {
                mMaxCacheSize = maxCacheSize;
            } else {
                mMaxCacheSize = MAX_CACHE_SIZE;
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, mMaxCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mInstance;
    }

    /**
     * 缓存Bitmap数据
     * @param key String 缓存数据对应的文件名称，唯一值
     * @param bitmap Bitmap 需要缓存的图片数据
     * @return 返回单例实例，提供链式调用支持
     */
    public DiskCacheHelper writeBitmap(String key, Bitmap bitmap) {
        try {
            String cacheKey = hashKeyForDisk(key);
            DiskLruCache.Editor editor = mDiskLruCache.edit(cacheKey);
            if (editor != null) {
                String bitmapString = bitmapToString(bitmap);
                if (bitmapString != null) {
                    editor.set(0, bitmapString);
                    editor.commit();
                } else {
                    editor.abort();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return mInstance;
        }
    }

    /**
     * 读取缓存的图片资源
     * @param key String 缓存数据对应的文件名称，唯一值
     * @return 返回Bitmap
     */
    public Bitmap readBitmap(String key) {
        Bitmap bitmap = null;
        try {
            String cacheKey = hashKeyForDisk(key);
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(cacheKey);
            if (snapshot != null) {
                InputStream is = snapshot.getInputStream(0);
                bitmap = BitmapFactory.decodeStream(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return bitmap;
        }
    }

    /**
     * 缓存数据对象，以JSON字符串格式保存
     * @param key String 缓存数据对应的文件名称，唯一值
     * @param data 需要缓存的JSON字符串
     * @return 返回单例实例，提供链式调用支持
     */
    public DiskCacheHelper writeData(String key, String data) {
        try {
            String cacheKey = hashKeyForDisk(key);
            DiskLruCache.Editor editor = mDiskLruCache.edit(cacheKey);
            if (editor != null) {
                if (data != null) {
                    editor.set(0, data);
                    editor.commit();
                } else {
                    editor.abort();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return mInstance;
        }
    }

    /**
     * 读取缓存的数据对象
     * @param key String 缓存数据对应的文件名称，唯一值
     * @return 返回数据对象JSON字符串
     */
    public String readData(String key) {
        String data = null;
        try {
            String cacheKey = hashKeyForDisk(key);
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(cacheKey);
            if (snapshot != null) {
                data = snapshot.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return data;
        }
    }

    /**
     * 删除key对应的缓存
     * @param key
     * @return DiskCacheHelper 链式调用
     */
    public DiskCacheHelper remove(String key) {
        try {
            mDiskLruCache.remove(hashKeyForDisk(key));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return mInstance;
        }
    }

    /**
     * 用于将内存中的操作记录同步到日志文件（也就是journal文件）当中。
     * 这个方法非常重要，因为DiskLruCache能够正常工作的前提就是要依赖于journal文件中的内容。
     * 并不是每次写入缓存都要调用一次flush()，频繁地调用并不会带来任何好处，会额外增加同步journal文件的时间。
     * 比较标准的做法就是在Activity的onPause()方法中去调用一次flush()方法就可以。
     * @return DiskCacheHelper 链式调用
     */
    public DiskCacheHelper flush() {
        try {
            mDiskLruCache.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return mInstance;
        }
    }

    /**
     * 删除当前缓存路径下的所有缓存
     * @return DiskCacheHelper 链式调用
     */
    public DiskCacheHelper delete() {
        try {
            mDiskLruCache.delete();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return mInstance;
        }
    }

    /**
     * 将DiskLruCache关闭，open()方法对应的一个方法
     */
    public void close() {
        try {
            mDiskLruCache.close();
            mDiskLruCache = null;
            mInstance = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private File getDiskCacheDir(Context context, CACHE_TYPE cacheType) {
        String cachePath = FileUtils.getDiskCacheDir(context);
        File cacheDir = new File(cachePath + File.separator + UNIQUE_NAME[cacheType.ordinal()]);
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        return cacheDir;
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(key.getBytes());
            cacheKey = bytesToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

}
