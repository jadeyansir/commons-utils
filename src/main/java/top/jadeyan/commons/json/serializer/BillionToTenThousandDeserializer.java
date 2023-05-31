package top.jadeyan.commons.json.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.BigDecimalValidator;
import top.jadeyan.commons.object.BigDecimalUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * 金额亿转万 json反序列化
 *
 * @author yan
 */
public class BillionToTenThousandDeserializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (StringUtils.isNotBlank(p.getText()) && BigDecimalValidator.getInstance().isValid(p.getText())) {
            BigDecimal bigDecimalVal = new BigDecimal(p.getText());
            Optional<BigDecimal> optional = BigDecimalUtils.convertBillionToW(bigDecimalVal);

            if (optional.isPresent()) {
                return optional.get();
            }
        }
        return null;
    }
}
