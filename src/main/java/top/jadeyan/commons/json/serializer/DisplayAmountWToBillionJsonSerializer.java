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
 * 金额万转亿 json序列化
 *
 * @author yan
 * @date 2022/07/19
 **/
public class DisplayAmountWToBillionJsonSerializer extends JsonSerializer<BigDecimal> {

    public static final String EMPTY_PLACEHOLDER = "--";
    public static final BigDecimal PERCENT_WEIGHT = BigDecimal.valueOf(10000);

    public static final int DEFAULT_SCALE = 2;

    @Override
    public void serialize(BigDecimal number, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String displayStr = getDisplayString(number);
        jsonGenerator.writeString(displayStr);
    }

    String getDisplayString(final BigDecimal number) {
        if (Objects.isNull(number) || BigDecimal.ZERO.compareTo(number) == 0) {
            return EMPTY_PLACEHOLDER;
        }
        BigDecimal billionAmount = number.divide(PERCENT_WEIGHT, DEFAULT_SCALE, RoundingMode.HALF_UP);
        DecimalFormat df = new DecimalFormat("0.00##");
        return df.format(billionAmount);
    }
}
