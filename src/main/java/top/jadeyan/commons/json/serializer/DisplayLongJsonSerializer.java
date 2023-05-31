package top.jadeyan.commons.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import static top.jadeyan.commons.object.NumberExtensionUtils.isNullOrZero;

/**
 * 显示Integer json序列化
 *
 * @author yan
 * @date 2020/03/25
 **/
public class DisplayLongJsonSerializer extends JsonSerializer<Long> {
    private static final String EMPTY_PLACEHOLDER = "--";

    @Override
    public void serialize(Long number, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String displayStr = getDisplayString(number);
        jsonGenerator.writeString(displayStr);
    }

    private String getDisplayString(final Long number) {
        if (isNullOrZero(number)) {
            return EMPTY_PLACEHOLDER;
        }
        return number + "";
    }
}
