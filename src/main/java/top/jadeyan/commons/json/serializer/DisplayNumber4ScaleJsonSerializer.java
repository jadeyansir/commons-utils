package top.jadeyan.commons.json.serializer;

import java.text.DecimalFormat;

import static top.jadeyan.commons.object.NumberExtensionUtils.isNullOrZero;

/**
 * 显示bigDecimal json序列化, 保留四位小数，不足补零
 *
 * @author yan
 * @date 2022/04/13
 */
public class DisplayNumber4ScaleJsonSerializer extends DisplayNumberJsonSerializer {

    @Override
    public String getDisplayString(final Number number) {
        if (isNullOrZero(number)) {
            return DisplayNumberJsonSerializer.EMPTY_PLACEHOLDER;
        }
        DecimalFormat df = new DecimalFormat("0.0000");
        return df.format(number);
    }
}
