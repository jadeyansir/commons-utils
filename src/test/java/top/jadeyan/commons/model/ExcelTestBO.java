package top.jadeyan.commons.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import top.jadeyan.commons.excelutils.ExcelDateConvert;

import java.sql.Date;

/**
 * excelUtil测试
 *
 * @author yan
 * @create 2022/3/4
 */
public class ExcelTestBO {
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("年龄")
    private Integer age;
    @ExcelProperty(value = "生日", converter = ExcelDateConvert.class)
    private Date birthday;

    @JsonIgnore
    @ExcelIgnore
    private Integer rowNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }
}
