package cn.ffcs.itbg.itpd.http;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.ffcs.itbg.itpd.http.cookies.PersistentCookieStore;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by chenqq on 17/1/13.
 * OkHttp二次封装，统一初始化OkHttpClient实例，同时添加相应的拦截器
 */

public class OkHttpUtils {
    private static OkHttpClient mOkHttpClient;
    private static Context mContext;


    //设置缓存目录
    private static File cacheDirectory = null;
    private static Cache cache = null;

    /**
     * 获取OkHttpClient实例
     *
     * @param context Application对应的context：getApplicationContext
     * @return
     */
    public static OkHttpClient getOkHttpClient(Context context, Interceptor interceptor) {
        if (null == mOkHttpClient) {
            mContext = context;
            cacheDirectory = new File(mContext.getCacheDir().getAbsolutePath(), "HttpCache");
            cache = new Cache(cacheDirectory, 10 * 1024 * 1024);

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            // 设置自动管理cookies的工具
            builder.cookieJar(new CookiesManager(mContext));
            // 添加应用程序拦截器，用于统一处理请求发起前与返回接收后的数据加工处理
            // 官方API地址：https://github.com/square/okhttp/wiki/Interceptors
            // 官方API样例实现：打印请求前的请求参数与接收返回的返回数据参数，由于服务器地址重定向，特别注意打印返回数据的url参数
            //builder.addInterceptor(new ApplicationIntercepter());
            // 添加网络拦截器
            //builder.addNetworkInterceptor(new NetworkIntercepter());
            // 设置请求连接超时时间
            builder.connectTimeout(30, TimeUnit.SECONDS);
            // 设置请求写操作超时时间
            builder.writeTimeout(30, TimeUnit.SECONDS);
            // 设置请求读操作超时时间
            builder.readTimeout(30, TimeUnit.SECONDS);
            // 设置缓存
            //builder.cache(cache)

            // 接受外部设置一个自定义的拦截器
            if (interceptor != null) {
                builder.addInterceptor(interceptor);
            }

            mOkHttpClient = builder.build();
        }
        return mOkHttpClient;
    }

    /**
     * Application拦截器
     */
    private static class ApplicationIntercepter implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkStatusUtils.isNetworkReachable(mContext)) {
                Toast.makeText(mContext, "暂无网络", Toast.LENGTH_SHORT).show();
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)//无网络时只从缓存中读取
                        .build();
            }

            Response response = chain.proceed(request);
            if (NetworkStatusUtils.isNetworkReachable(mContext)) {
                int maxAge = 60 * 60; // 有网络时 设置缓存超时时间1个小时
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    }


    /**
     * 自定义实现自动管理Cookies
     */
    private static class CookiesManager implements CookieJar {
        private PersistentCookieStore cookieStore = null;

        public CookiesManager(Context context) {
            cookieStore = new PersistentCookieStore(context);
        }

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (cookies != null && cookies.size() > 0) {
                for (Cookie item : cookies) {
                    cookieStore.add(url, item);
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url);
            return cookies;
        }
    }


    /**
     * 这里提供OkHttp的直接调用方法，目的是为了调试接口方便，防止使用Retrofit封装后，无法调试的一个便捷方案。
     *
     * @param url
     * @param data
     * @param callback
     */
    public static void asyncPost(String url, Object data, Callback callback) {
        Gson gson = new Gson();
        // 创建特点类型的body数据，这里的类型非常关键，调试过程中可能需要修改该类型，目前使用的类型是符合CRM翼销售项目服务器要求的类型。
        RequestBody body = RequestBody.create(MediaType.parse("text/xml; charset=utf-8"), gson.toJson(data));
        // 增加请求tag，以便可以取消请求
        Request request = new Request.Builder().url(url).post(body).build();

        mOkHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 这里提供OkHttp的直接调用方法，目的是为了调试接口方便，防止使用Retrofit封装后，无法调试的一个便捷方案。
     *
     * @param url
     * @param path
     * @param callback
     */
    public static void uploadFile(String url, String path, Callback callback) {
        File file = new File(path);
        // 创建特点类型的body数据，这里的类型非常关键，调试过程中可能需要修改该类型，目前使用的类型是符合CRM翼销售项目服务器要求的类型。
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        // 增加请求tag，以便可以取消请求
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(callback);
    }
}
