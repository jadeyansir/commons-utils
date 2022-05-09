package top.jadeyan.commons.excelutils;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.DateUtils;
import org.apache.poi.ss.usermodel.DateUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.TimeZone;

/**
 * excel number转变timeStamp
 *
 * @Author caodz
 * @Date 2021/12/10
 **/
public class ExcelTimestampNumberConverter implements Converter<Timestamp> {
    @Override
    public Class supportJavaTypeKey() {
        return Timestamp.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }

    @Override
    public Timestamp convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty,
                                       GlobalConfiguration globalConfiguration) throws Exception {
        Date date = excelContentProperty != null && excelContentProperty.getDateTimeFormatProperty() != null ?
                DateUtil.getJavaDate(cellData.getNumberValue().doubleValue(),
                        excelContentProperty.getDateTimeFormatProperty().getUse1904windowing(), (TimeZone) null)
                : DateUtil.getJavaDate(cellData.getNumberValue().doubleValue(), globalConfiguration.getUse1904windowing(), (TimeZone) null);
        return date == null ? null : new Timestamp(date.getTime());
    }

    @Override
    public CellData convertToExcelData(Timestamp timestamp, ExcelContentProperty excelContentProperty,
                                       GlobalConfiguration globalConfiguration) throws Exception {
        Date value = timestamp == null ? null : new Date(timestamp.getTime());
        return excelContentProperty != null && excelContentProperty.getDateTimeFormatProperty() != null ?
                new CellData(DateUtils.format(value, excelContentProperty.getDateTimeFormatProperty().getFormat()))
                : new CellData(DateUtils.format(value, (String) null));
    }
}
