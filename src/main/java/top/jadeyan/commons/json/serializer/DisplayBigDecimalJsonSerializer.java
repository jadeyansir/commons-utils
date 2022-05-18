package top.jadeyan.commons.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Optional;


/**
 * 显示bigDecimal json序列化
 * 区别于Number对 0 的处理
 *
 * @author yan
 * @date 2022/04/15
 */
public class DisplayBigDecimalJsonSerializer extends JsonSerializer<Number> implements ContextualSerializer {

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
     * 处理数据保留 最多保留 4 位小数，没有四位的保留两位
     *
     * @param number 需要处理的数据
     * @return 处理后的数字字符串
     */
    public String getDisplayString(final Number number) {
        if (Objects.isNull(number)) {
            return EMPTY_PLACEHOLDER;
        }
        if (StringUtils.isBlank(numberPattern)) {
            numberPattern = "0.00##";
        }
        DecimalFormat df = new DecimalFormat(numberPattern);
        return df.format(number);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        DisplayBigDecimalJsonSerializer displayNumberJsonSerializer = new DisplayBigDecimalJsonSerializer();
        if (property != null) {
            Optional.ofNullable(property.findPropertyFormat(prov.getConfig(), handledType()).getPattern())
                    .ifPresent(x -> displayNumberJsonSerializer.numberPattern = x);
            return displayNumberJsonSerializer;
        }
        return displayNumberJsonSerializer;
    }
}
