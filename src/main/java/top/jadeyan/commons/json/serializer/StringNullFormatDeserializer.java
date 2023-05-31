package top.jadeyan.commons.json.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * JSON String ""/null 反序列化 "--"
 *
 * @author yan
 * @date 2022/11/30
 */
public class StringNullFormatDeserializer extends JsonDeserializer<String> {

    private static final String EMPTY_PLACEHOLDER = "--";

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (StringUtils.isBlank(p.getText())) {
            return EMPTY_PLACEHOLDER;
        }
        return p.getText();
    }
}
