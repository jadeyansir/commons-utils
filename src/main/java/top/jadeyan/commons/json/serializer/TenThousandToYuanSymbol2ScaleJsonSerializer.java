package top.jadeyan.commons.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * 金额万转元 json序列化 带 $ 前缀
 *
 * @author yan
 * @date 2023/1/16
 */
public class TenThousandToYuanSymbol2ScaleJsonSerializer extends JsonSerializer<BigDecimal> {

    private static final String EMPTY_PLACEHOLDER = "--";
    private static final BigDecimal PERCENT_WEIGHT = BigDecimal.valueOf(10000L);

    private static final String SYMBOL = "$";

    @Override
    public void serialize(BigDecimal number, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String displayStr = this.getDisplayString(number);
        jsonGenerator.writeString(displayStr);
    }

    String getDisplayString(BigDecimal number) {
        if (Objects.isNull(number)) {
            return EMPTY_PLACEHOLDER;
        } else {
            BigDecimal billionAmount = number.multiply(PERCENT_WEIGHT);
            DecimalFormat df = new DecimalFormat("##,##0.00##");
            return SYMBOL + df.format(billionAmount);
        }
    }
}
