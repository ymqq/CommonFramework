package cn.ffcs.itbg.itpd.http;

import android.content.Context;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chenqq on 17/1/13.
 *
 * Retrofit工具类，这里对Retrofit进行二次封装，具体项目使用请继承该类，然后进行Http接口代码编写。
 */

public abstract class AbsRetrofit {

    private static Retrofit mRetrofit;
    private static OkHttpClient mOkHttpClient;


    /**
     * 获取通用配置的Retrofit实例，同时提供数据转换工厂定制，与拦截器定制
     * @param context Application对应的context：getApplicationContext
     * @param baseUrl 服务器接口url通用部分
     * @param converterFactory 定制的数据转换工厂
     * @param interceptor 定制的拦截器
     * @return
     */
    protected static Retrofit getRetrofit(
            Context context, String baseUrl, Converter.Factory converterFactory, Interceptor interceptor) {
        if (null == mRetrofit) {
            if (null == mOkHttpClient) {
                mOkHttpClient = OkHttpUtils.getOkHttpClient(context, interceptor);
            }

            Retrofit.Builder builder = new Retrofit.Builder();
            // 设置服务器路径Url通用部分
            builder.baseUrl(baseUrl);
            // 添加数据转换工厂，支持自定义数据转换工厂，默认使用Gson
            if (converterFactory != null) {
                builder.addConverterFactory(converterFactory);
            } else {
                builder.addConverterFactory(GsonConverterFactory.create());
            }
            // 添加回调工厂，采用RxJava
            builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
            // 设置使用OkHttp网络请求库
            builder.client(mOkHttpClient);
            mRetrofit = builder.build();
        }

        return mRetrofit;
    }
}
