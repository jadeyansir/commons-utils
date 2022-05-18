package top.jadeyan.commons.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DecimalFormat;

import static top.jadeyan.commons.object.NumberExtensionUtils.isNullOrZero;

/**
 * 显示带百分号序列化
 *
 * @author wuyh
 * @create 2022/2/23
 */
public class DisplayPercentJsonSerializer extends JsonSerializer<Number> {
    private static final String EMPTY_PLACEHOLDER = "--";

    @Override
    public void serialize(Number number, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String displayStr = getDisplayString(number);
        jsonGenerator.writeString(displayStr);
    }

    private String getDisplayString(final Number number) {
        if (isNullOrZero(number)) {
            return EMPTY_PLACEHOLDER;
        }
        DecimalFormat df = new DecimalFormat("0.00##");
        return df.format(number) + "%";
    }
}
