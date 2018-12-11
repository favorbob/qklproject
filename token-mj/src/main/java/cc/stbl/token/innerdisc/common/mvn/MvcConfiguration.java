package cc.stbl.token.innerdisc.common.mvn;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.DateCodec;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;

@Configuration
public class MvcConfiguration {

    @Bean
    public HttpMessageConverter httpMessageConverter(){
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        ParserConfig.getGlobalInstance().putDeserializer(Date.class,new FastJsonDateDeserializers());
        fastConverter.setCharset(Charset.forName("UTF-8"));
        fastConverter.setSupportedMediaTypes(Arrays.asList(
                MediaType.APPLICATION_JSON,
//                MediaType.TEXT_HTML,
//                MediaType.TEXT_PLAIN,
                MediaType.APPLICATION_JSON_UTF8
        ));
        fastConverter.setFeatures(
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteNullStringAsEmpty
        );
        return fastConverter;
    }

    public static class FastJsonDateDeserializers extends DateCodec {
        public <T> T cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object val) {
            parser.getLexer().config(Feature.AllowISO8601DateFormat,true);
            return super.cast(parser,clazz,fieldName,val);
        }
    }
}
