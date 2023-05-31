package top.jadeyan.commons.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * 十分制转换 保留一位小数
 *
 * @author yan
 * @date 2022/11/30
 */
public class TenPointScale1JsonSerializer extends JsonSerializer<Number> {

    private static final String EMPTY_PLACEHOLDER = "--";
    private static final BigDecimal TEN = new BigDecimal(10);

    @Override
    public void serialize(Number number, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String displayStr = this.getDisplayString(number);
        jsonGenerator.writeString(displayStr);
    }

    String getDisplayString(Number number) {
        if (Objects.isNull(number)) {
            return EMPTY_PLACEHOLDER;
        } else {
            DecimalFormat df = new DecimalFormat("0.0");
            return df.format(new BigDecimal(number.toString()).divide(TEN, 1, RoundingMode.HALF_UP));
        }
    }
}
