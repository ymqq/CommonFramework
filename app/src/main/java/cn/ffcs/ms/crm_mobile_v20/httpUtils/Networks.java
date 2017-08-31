package cn.ffcs.ms.crm_mobile_v20.httpUtils;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import cn.ffcs.itbg.itpd.http.AbsRetrofit;
import cn.ffcs.itbg.itpd.http.PostFileMapUtils;
import cn.ffcs.itbg.itpd.http.StringConverterFactory;
import cn.ffcs.ms.crm_mobile_v20.MyApplication;
import cn.ffcs.ms.crm_mobile_v20.entities.Message;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenqq on 17/1/19.
 *
 * 重载RetrofitUtils.class实现具体项目中需要的API功能实现，完成具体接口配置工作。
 *
 * 封装参考资料：
 * BasicParamsInterceptor - 为 OkHttp 请求添加公共参数：http://www.jkyeo.com/2016/04/18/BasicParamsInterceptor-%E4%B8%BA-OkHttp-%E8%AF%B7%E6%B1%82%E6%B7%BB%E5%8A%A0%E5%85%AC%E5%85%B1%E5%8F%82%E6%95%B0/
 * Retrofit2.0 公共参数（固定参数）：https://segmentfault.com/a/1190000006151767
 * 给retrofit 的 添加okhttp的拦截器，可以让她显示 请求和返回的信息 ，便于查找错误：http://blog.csdn.net/minle_/article/details/52451097
 * Retrofit自定义Converter之StringConverterFactory：http://blog.csdn.net/gengqiquan/article/details/52473334
 * Retrofit 自定义Converter.Factory实现直接接收String：http://www.jianshu.com/p/5598cbe19d6b
 */
public class Networks extends AbsRetrofit {
    // 使用Application的上下文
    private static final Context CONTEXT = MyApplication.INSTANCE.getApplicationContext();
    // 请求接口的基础url部分
//    private static final String BASE_URL = "http://218.85.155.71:7003/m/";
    private static final String BASE_URL = "http://192.168.58.140:9999";
    /*
    * RetrofitUtils中，默认使用Gson作为数据转换器，但是CRM翼销售服务器接口交互数据格式为String，
    * 所以这里添加自定义String格式转换器
    */
    private static final StringConverterFactory mStringConverterFactory
            = StringConverterFactory.create(new StringRequestBodyConverter(), new StringResponseBodyConverter());
    protected static final NetService mNetService =
            getRetrofit(CONTEXT, BASE_URL, mStringConverterFactory, null).create(NetService.class);

    private static final Gson mGson = new Gson();


    private interface NetService {

        /**
         * encoding=010101：加密压缩
         * @param data
         * @return
         */
        @POST("promotAction!login?encoding=010101&appId=205")
        Observable<String> login(@Body String data);

        @Multipart
        @POST("/api/upload/photos")
        Observable<String> uploadLog(@PartMap Map<String, RequestBody> map);

        @Multipart
        @POST("/api/upload/photos")
        Observable<String> uploadFiles(@PartMap Map<String, RequestBody> map);
    }

    /**
     * 登陆接口，使用CRM翼销售系统的登陆接口进行测试
     * 主要是由于翼销售接口使用了加密传输，需要自定义字符串转换工厂
     *
     * @param message
     * @param observer
     */
    public static void login(Message message, Observer<String> observer) {
        setSubscribe(mNetService.login(mGson.toJson(message)), observer);
    }

    /**
     *
     * @param key
     * @param paths
     * @param observer
     */
    public static void uploadLog(String key, List<String> paths, Observer<String> observer) {
        setSubscribe(mNetService.uploadLog(PostFileMapUtils.getRequestBodyMap(key, paths.get(0))), observer);
    }

    /**
     * 文件上传接口，使用Nodejs搭建的文件服务器，本地搭建
     * 该接口能测试所有文件类型，已经测试过：jpg、png、txt、apk、log、mp4、mp3
     *
     * @param key
     * @param path
     * @param observer
     */
    public static void uploadFile(String key, String path, Observer<String> observer) {
        setSubscribe(mNetService.uploadFiles(PostFileMapUtils.getRequestBodyMap(key, path)), observer);
    }

    /**
     * 文件上传接口，使用Nodejs搭建的文件服务器，本地搭建
     * 该接口能测试所有文件类型，已经测试过：jpg、png、txt、apk、log、mp4、mp3
     *
     * @param key
     * @param paths
     * @param observer
     */
    public static void uploadFiles(String key, List<String> paths, Observer<String> observer) {
        setSubscribe(mNetService.uploadFiles(PostFileMapUtils.getRequestBodyMap(key, paths)), observer);
    }

    /**
     * 插入观察者
     *
     * @param observable
     * @param observer
     * @param <T>
     */
    public static <T> void setSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread()) // 子线程访问网络
                .observeOn(AndroidSchedulers.mainThread()) // 回调到主线程
                .subscribe(observer);
    }

    /**
     * 自定义拦截器
     * 实现对RequestBody数据重新处理，设置RequestBody数据类型为："text/xml; charset=UTF-8"
     * 因为CRM翼销售服务器接收的类型是这个。
     */
    public static class MyInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            // Retrofit2.0 公共参数（固定参数）:https://segmentfault.com/a/1190000006151767
            Request request = chain.request();
            Request.Builder newBuilder = chain.request().newBuilder();
            String postBody = bodyToString(request.body());
            // 重置请求数据MediaType为："text/xml; charset=UTF-8"
            newBuilder.post(RequestBody.create(MediaType.parse("text/xml; charset=UTF-8"), postBody));

            request = newBuilder.build();

            return chain.proceed(request);
        }

        /**
         * 讲RequestBody中的请求数据重新取出，转换成String
         * @param request
         * @return
         */
        private String bodyToString(final RequestBody request) {
            try {
                final RequestBody copy = request;
                final Buffer buffer = new Buffer();
                if (copy != null)
                    copy.writeTo(buffer);
                else
                    return "";
                return buffer.readUtf8();
            } catch (final IOException e) {
                return "did not work";
            }
        }
    }

    /**
     * 自定义Request转换器
     */
    public static class StringRequestBodyConverter implements Converter<String, RequestBody> {
        private final MediaType MEDIA_TYPE = MediaType.parse("text/xml; charset=UTF-8");
        private final Charset UTF_8 = Charset.forName("UTF-8");

        @Override
        public RequestBody convert(String value) throws IOException {
            // TODO 这里可以对value添加处理，然后再执行下方的语句

            // 这里的代码处理是标准的、统一的处理方式
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            writer.write(value);
            writer.close();
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        }
    }

    /**
     * 自定义response转换器
     */
    public static class StringResponseBodyConverter implements Converter<ResponseBody, String> {
        @Override
        public String convert(ResponseBody value) throws IOException {
            try {
                String result = value.string();
                // TODO 这里可以对result添加处理，然后再返回
                // 由于接口使用encoding=010101加密压缩，所以这里应该转换，解压解密
                return result;
            } finally {
                value.close();
            }
        }
    }
}
