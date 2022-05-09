package top.jadeyan.commons.excelutils;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.DateUtils;
import top.jadeyan.commons.object.DateExtensionUtils;

import java.sql.Date;
import java.text.ParseException;

/**
 * 字符类型日期转换
 *
 * @author Tangshengbo
 * @date 2021/03/04
 */
public class StringToSqlDateConvert implements Converter<Date> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return Date.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Date convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
                                  GlobalConfiguration globalConfiguration) throws ParseException {

        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return DateExtensionUtils.parseDate(cellData.getStringValue());
        } else {
            return new Date(DateUtils.parseDate(cellData.getStringValue(),
                    contentProperty.getDateTimeFormatProperty().getFormat()).getTime());
        }
    }

    @Override
    public CellData convertToExcelData(Date value, ExcelContentProperty contentProperty,
                                       GlobalConfiguration globalConfiguration) {
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return new CellData(DateUtils.format(value, DateExtensionUtils.FORMAT_DATE_SIMPLE));
        } else {
            return new CellData(DateUtils.format(value, contentProperty.getDateTimeFormatProperty().getFormat()));
        }
    }
}

