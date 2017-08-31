package cn.ffcs.itbg.itpd.http;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by chenqq on 17/2/20.
 * 基本的String数据格式转换工厂，若有特殊需求时，请按照该类的形式，
 * 进行StringRequestBodyConverter与StringResponseBodyConverter类的重写，并且调用带参数的#create方法。
 */
public class StringConverterFactory extends Converter.Factory {

    private Converter<String, RequestBody> mRequestBodyConverter;
    private Converter<ResponseBody, String> mResponseBodyConverter;

    /**
     * 创建默认的String转换工厂
     * @return
     */
    public static StringConverterFactory create() {
        return new StringConverterFactory(null, null);
    }

    /**
     * 创建定制的String转换工厂
     * @param requestBodyConverter 自定义request转换器
     * @param responseBodyConverter 自定义的response转换器
     * @return
     */
    public static StringConverterFactory create(Converter<String, RequestBody> requestBodyConverter,
                                                Converter<ResponseBody, String> responseBodyConverter) {
        return new StringConverterFactory(requestBodyConverter, responseBodyConverter);
    }

    private StringConverterFactory(Converter<String, RequestBody> requestBodyConverter,
                                   Converter<ResponseBody, String> responseBodyConverter) {
        mRequestBodyConverter = requestBodyConverter;
        mResponseBodyConverter = responseBodyConverter;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return mResponseBodyConverter != null ? mResponseBodyConverter : new StringResponseBodyConverter();
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations, Retrofit retrofit) {
        return mRequestBodyConverter != null ? mRequestBodyConverter : new StringRequestBodyConverter();
    }


    /**
     * 默认的request转换器
     */
    public class StringRequestBodyConverter implements Converter<String, RequestBody> {
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
     * 默认的response转换器
     */
    public class StringResponseBodyConverter implements Converter<ResponseBody, String> {
        @Override
        public String convert(ResponseBody value) throws IOException {
            try {
                String result = value.string();
                // TODO 这里可以对result添加处理，然后再返回

                return result;
            } finally {
                value.close();
            }
        }
    }
}
