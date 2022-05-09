package top.jadeyan.commons.excelutils;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.date.DateNumberConverter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.sql.Date;

/**
 * 时间转换器
 *
 * @author yan
 * @date 2021/11/02
 */
public class ExcelSqlDateConvert implements Converter<Date> {

    private DateNumberConverter dateNumberConverter = new DateNumberConverter();

    @Override
    public Class supportJavaTypeKey() {
        return Date.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }

    @Override
    public Date convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
                                  GlobalConfiguration globalConfiguration) throws Exception {
        java.util.Date date = dateNumberConverter.convertToJavaData(cellData, contentProperty, globalConfiguration);
        return date == null ? null : new Date(date.getTime());
    }

    @Override
    public CellData convertToExcelData(Date date, ExcelContentProperty excelContentProperty,
                                       GlobalConfiguration globalConfiguration) {
        return dateNumberConverter.convertToExcelData(date, excelContentProperty, globalConfiguration);
    }

}
