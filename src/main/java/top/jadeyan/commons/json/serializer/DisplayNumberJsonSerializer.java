package top.jadeyan.commons.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.DecimalFormat;

import static top.jadeyan.commons.object.NumberExtensionUtils.isNullOrZero;

/**
 * 显示bigDecimal json序列化
 *
 * @author yan
 * @date 2020/03/25
 **/
public class DisplayNumberJsonSerializer extends JsonSerializer<Number> {

    /**
     * 数值为空的默认值
     */
    protected static final String EMPTY_PLACEHOLDER = "--";

    protected String numberPattern;

    @Override
    public void serialize(Number number, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String displayStr = getDisplayString(number);
        jsonGenerator.writeString(displayStr);
    }

    /**
     * 处理数据保留 最多保留 4 位小数，没有四位的保留两位。若DTO有注解，则取DTO的格式
     *
     * @param number 需要处理的数据
     * @return 处理后的数字字符串
     */
    public String getDisplayString(final Number number) {
        if (isNullOrZero(number)) {
            return EMPTY_PLACEHOLDER;
        }
        if (StringUtils.isBlank(numberPattern)) {
            numberPattern = "0.00##";
        }
        DecimalFormat df = new DecimalFormat(numberPattern);
        return df.format(number);
    }

}
