package top.jadeyan.commons.json.serializer;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.util.Optional;

/**
 * 显示bigDecimal json序列化
 *
 * @author yan
 * @date 2020/03/25
 **/
public class DisplayNumberJsonWithFormatSerializer extends DisplayNumberJsonSerializer implements ContextualSerializer {

    // 使用说明：   @JsonSerialize(using = DisplayNumberJsonWithFormatSerializer.class,nullsUsing = DisplayNumberJsonWithFormatSerializer.class)
    //            @JsonFormat(pattern = "0.0###")
    // 指定任意格式
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property != null) {
            Optional.ofNullable(property.findPropertyFormat(prov.getConfig(), handledType()).getPattern())
                    .ifPresent(x -> this.numberPattern = x);
        }
        return this;
    }
}
