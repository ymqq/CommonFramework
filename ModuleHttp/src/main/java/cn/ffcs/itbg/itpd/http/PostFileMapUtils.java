package cn.ffcs.itbg.itpd.http;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by chenqq on 17/2/28.
 *
 * 处理文件上传时拼装RequestBody Map工具类
 * 参考资料：<br/>
 * http://blog.csdn.net/cjh_android/article/details/51695291
 * Android网络开源库-Retrofit(二)文件上传、下载: http://blog.csdn.net/qq_21430549/article/details/51212977
 */

public class PostFileMapUtils {

    private static final String TAG = "FileMapUtils";

    /**
     * 获取单文件上传使用的RequestBody Map
     *
     * @param key 作为Map中key值的前缀
     * @param path 文件绝对路径
     * @return
     */
    public static Map<String, RequestBody> getRequestBodyMap(String key, String path) {
        Map<String, RequestBody> map = new TreeMap<>();

        File file = new File(path);
        RequestBody requestBody = getRequestBody(file);

        map.put(key + "\";filename=\"" + file.getName(), requestBody);
        return map;
    }

    /**
     * 获取多文件上传使用的RequestBody Map
     * @param key 作为Map中key值的前缀
     * @param paths 文件绝对路径
     * @return
     */
    public static Map<String, RequestBody> getRequestBodyMap(String key, List<String> paths) {
        Map<String, RequestBody> map = new TreeMap<>();

        for (int i = 0, size = paths.size(); i < size; i++) {
            String path = paths.get(i);
            File file = new File(path);
            RequestBody requestBody = getRequestBody(file);

            map.put(key + "[" + i + "]" + "\";filename=\"" + file.getName(), requestBody);
        }
        return map;
    }

    /**
     * 获取文件上传使用的RequestBody，MediaType，通过具体文件获取，当获取到null时，使用“text/plain”，作为默认值
     *
     * @param file
     * @return
     */
    private static RequestBody getRequestBody(File file) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(file.getAbsolutePath());
        contentTypeFor = contentTypeFor == null || "".equals(contentTypeFor) ? "text/plain" : contentTypeFor;
        MediaType mediaType = MediaType.parse(contentTypeFor);

        return RequestBody.create(mediaType, file);
    }
}
