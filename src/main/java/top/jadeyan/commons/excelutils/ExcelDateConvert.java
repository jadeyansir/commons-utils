package top.jadeyan.commons.excelutils;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import top.jadeyan.commons.object.DateExtensionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * 时间转换器
 *
 * @author yan
 * @date 2021/11/02
 */
public class ExcelDateConvert implements Converter<Date> {

    private static final Integer CALENDER_BASE_YEAR = 1900;
    private static final Integer CALENDER_BASE_MONTH = -1;

    @Override
    public Class supportJavaTypeKey() {
        return Date.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }

    @Override
    public Date convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty,
                                  GlobalConfiguration globalConfiguration) throws Exception {
        Calendar calendar = new GregorianCalendar(CALENDER_BASE_YEAR, 0, CALENDER_BASE_MONTH);
        if (Objects.equals(cellData.getType(), CellDataTypeEnum.NUMBER)) {
            calendar.add(Calendar.DATE, cellData.getNumberValue().intValue());
        }
        String format = DateExtensionUtils.format(calendar.getTime(), "yyyy-MM-dd");
        return DateExtensionUtils.parseDate(format);
    }

    @Override
    public CellData convertToExcelData(Date date, ExcelContentProperty excelContentProperty,
                                       GlobalConfiguration globalConfiguration) {
        return new CellData(DateExtensionUtils.format(date, "yyyy-MM-dd"));
    }
}
