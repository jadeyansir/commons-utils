package top.jadeyan.commons.json.serializer;

import java.text.DecimalFormat;

import static top.jadeyan.commons.object.NumberExtensionUtils.isNullOrZero;

/**
 * 显示bigDecimal json序列化, 保留两位
 *
 * @author yan
 * @date 2021/6/29
 */
public class DisplayNumber0ScaleJsonSerializer extends DisplayNumberJsonSerializer {

    @Override
    public String getDisplayString(final Number number) {
        if (isNullOrZero(number)) {
            return DisplayNumberJsonSerializer.EMPTY_PLACEHOLDER;
        }
        DecimalFormat df = new DecimalFormat("0");
        return df.format(number);
    }

}
