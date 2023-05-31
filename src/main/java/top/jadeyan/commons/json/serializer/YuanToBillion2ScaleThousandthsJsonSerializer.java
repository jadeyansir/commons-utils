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
 * 金额元转亿 json序列化 保留两位小数 千分位
 *
 * @author: yan
 * @Date: 2022/11/1
 */
public class YuanToBillion2ScaleThousandthsJsonSerializer extends JsonSerializer<BigDecimal> {

    private static final String EMPTY_PLACEHOLDER = "--";
    private static final BigDecimal PERCENT_WEIGHT = BigDecimal.valueOf(100_000_000);

    private static final int DEFAULT_SCALE = 2;

    @Override
    public void serialize(BigDecimal number, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String displayStr = getDisplayString(number);
        jsonGenerator.writeString(displayStr);
    }

    String getDisplayString(final BigDecimal number) {
        if (Objects.isNull(number)) {
            return EMPTY_PLACEHOLDER;
        }
        BigDecimal billionAmount = number.divide(PERCENT_WEIGHT, DEFAULT_SCALE, RoundingMode.HALF_UP);
        DecimalFormat df = new DecimalFormat("##,##0.00##");
        return df.format(billionAmount);
    }
}
