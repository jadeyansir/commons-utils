package top.jadeyan.commons.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * 显示String json序列化
 *
 * @author wuyh
 * @date 2021/09/13
 **/
public class DisplayStringJsonSerializer extends JsonSerializer<String> {
    private static final String EMPTY_PLACEHOLDER = "--";

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String displayStr = getDisplayString(s);
        jsonGenerator.writeString(displayStr);
    }

    private String getDisplayString(String s) {
        if (StringUtils.isBlank(s) || "0".equalsIgnoreCase(s)) {
            return EMPTY_PLACEHOLDER;
        }
        return s;
    }
}