package top.jadeyan.commons.json.serializer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * 金额万转亿 json序列化(0值会被展示，而不是转为--)
 *
 * @author yan
 * @date 2023/3/16
 **/
public class DisplayAmountWToBillionIncludingZeroJsonSerializer extends DisplayAmountWToBillionJsonSerializer {

    @Override
    String getDisplayString(final BigDecimal number) {
        if (Objects.isNull(number)) {
            return EMPTY_PLACEHOLDER;
        }
        BigDecimal billionAmount = number.divide(PERCENT_WEIGHT, DEFAULT_SCALE, RoundingMode.HALF_UP);
        DecimalFormat df = new DecimalFormat("0.00##");
        return df.format(billionAmount);
    }
}
