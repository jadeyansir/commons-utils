package top.jadeyan.commons.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Objects;

/**
 * Long 转 string
 *
 * @author wuyh
 * @date 2021/09/13
 **/
public class LongToStringJsonSerializer extends JsonSerializer<Long> {
    @Override
    public void serialize(Long aLong, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (Objects.isNull(aLong)) {
            jsonGenerator.writeString("");
        } else {
            jsonGenerator.writeString(aLong.toString());
        }
    }
}