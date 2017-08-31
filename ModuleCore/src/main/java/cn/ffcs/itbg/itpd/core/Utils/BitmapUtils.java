package cn.ffcs.itbg.itpd.core.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Desc: Bitmap 工具类
 *
 * 需要用到mockio，还未编写对应的测试用例
 *
 * 对应编写了测试类：test/cn.ffcs.itbg.itpd.core.Utils.StringUtilsTest.java
 *
 * 快捷定位测试类：
 *  1）鼠标右键->Go To -> Test -> StringUtilsTest
 *  2）Ctrl + Shift + T -> StringUtilsTest
 *
 * 新增方法请同时编写对应的测试方法：快速定位后，现在Create New Test...，然后选择新增方法，更新测试类
 *
 * @Author: Tyras on 2017/4/20 15:21.
 */

public class BitmapUtils {

    /**
     * Base64 照片字符串转换为 Bitmap
     *
     * @param base64
     * @return
     * @throws Exception
     */
    public static final Observable<Bitmap> base64ToBmp(String base64) throws Exception {
        return base64ToBmp(base64, Base64.NO_WRAP);
    }

    /**
     * Base64 照片字符串转换为 Bitmap
     *
     * @param base64
     * @param flags
     * @return
     * @throws Exception
     */
    public static final Observable<Bitmap> base64ToBmp(String base64, final int flags) throws Exception {
        return Observable.just(base64)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, Bitmap>() {

                    @Override
                    public Bitmap call(String base64) {
                        if (base64 == null || "".equals(base64)) {
                            return null;
                        }

                        byte[] bytes = Base64.decode(base64, flags);
                        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    }
                });
    }

    /**
     * 按照默认设置的压缩质量
     * 将 Bitmap  转换为 Base64 照片字符串
     *
     * @param bmp
     * @return
     */
    public static final Observable<String> bmpToBase64(Bitmap bmp) {
        int quality = 100;

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) { // 4.0 及以下
            quality = 50;
        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) { // 4.3 及以下
            quality = 60;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) { // 5.0 以下
            quality = 70;
        }

        return bmpToBase64(bmp, Bitmap.CompressFormat.JPEG, quality, Base64.NO_WRAP);
    }

    /**
     * Bitmap  转换为 Base64 照片字符串
     *
     * @param bmp
     * @param quality
     * @return
     */
    public static final Observable<String> bmpToBase64(Bitmap bmp, int quality) {
        return bmpToBase64(bmp, Bitmap.CompressFormat.JPEG, quality, Base64.NO_WRAP);
    }

    /**
     * Bitmap  转换为 Base64 照片字符串
     *
     * @param bmp
     * @param format
     * @param quality
     * @return
     */
    public static final Observable<String> bmpToBase64(Bitmap bmp, Bitmap.CompressFormat format, int quality) {
        return bmpToBase64(bmp, format, quality, Base64.NO_WRAP);
    }

    /**
     * Bitmap  转换为 Base64 照片字符串
     *
     * @param bmp
     * @param format
     * @param quality
     * @param flags
     * @return
     */
    public static final Observable<String> bmpToBase64(Bitmap bmp, final Bitmap.CompressFormat format, final int quality, final int flags) {
        return Observable.just(bmp)
                .subscribeOn(Schedulers.io())
                .map(new Func1<Bitmap, String>() {

                    @Override
                    public String call(Bitmap bitmap) {
                        return bitmapToBase64(bitmap, format, quality, flags);
                    }
                });
    }

    /**
     * Bitmap  转换为 Base64 照片字符串
     *
     * @param bmp
     * @param format
     * @param quality
     * @param flags
     * @return
     */
    private static String bitmapToBase64(Bitmap bmp, Bitmap.CompressFormat format, int quality, int flags) {
        String base64 = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bmp != null) {
                baos = new ByteArrayOutputStream();
                bmp.compress(format, quality, baos);
                byte[] bytes = baos.toByteArray();
                base64 = Base64.encodeToString(bytes, flags);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return base64;
    }

    /**
     * 将 Bitmap 压缩到指定的宽度尺寸大小
     *
     * @param bmp
     * @param exceptedWidth
     * @return
     */
    public static final Observable<Bitmap> scaleBmpByMatrix(final Bitmap bmp, final int exceptedWidth) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                int bmpWidth = bmp.getWidth();
                int bmpHeight = bmp.getHeight();
                float scale = (float) exceptedWidth / bmpWidth;

                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale); // 长和宽放大缩小的比例
                Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight, matrix, true);

                subscriber.onNext(resizeBmp);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }
}
