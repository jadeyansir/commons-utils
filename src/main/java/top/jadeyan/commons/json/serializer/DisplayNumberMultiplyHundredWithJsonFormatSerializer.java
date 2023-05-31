package top.jadeyan.commons.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * 在序列化前，把数字乘以100， 显示bigDecimal json序列化
 *
 * @author yan
 * @date 2020/03/25
 **/
public class DisplayNumberMultiplyHundredWithJsonFormatSerializer extends DisplayNumberJsonWithFormatSerializer {

    private static final BigDecimal WEIGHT = BigDecimal.valueOf(100);

    @Override
    public void serialize(Number number, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Number newNumber = Objects.isNull(number) ? null : new BigDecimal(number.toString()).multiply(WEIGHT);
        String displayStr = getDisplayString(newNumber);
        jsonGenerator.writeString(displayStr);
    }
}
