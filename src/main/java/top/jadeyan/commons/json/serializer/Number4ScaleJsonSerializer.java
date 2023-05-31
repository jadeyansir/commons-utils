package top.jadeyan.commons.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * 显示bigDecimal json序列化, 保留两位
 * 去除当数值为0时，返回--
 *
 * @author yan
 * @date 2022/08/18
 */
public class Number4ScaleJsonSerializer extends JsonSerializer<Number> {

    private static final String EMPTY_PLACEHOLDER = "--";

    @Override
    public void serialize(Number number, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String displayStr = getDisplayString(number);
        jsonGenerator.writeString(displayStr);
    }

    String getDisplayString(final Number number) {
        if (Objects.isNull(number)) {
            return EMPTY_PLACEHOLDER;
        }
        DecimalFormat df = new DecimalFormat("0.0000");
        return df.format(number);
    }

}
