package top.jadeyan.commons.excelutils;

import java.util.Collections;
import java.util.List;

/**
 * 错误信息
 */
public class ErrorMsg {
    private Integer errorNumber;
    private List<ExcelMsgInfo> excelMsgInfo;

    /**
     * 全参构造
     *
     * @param errorNumber  错误数量
     * @param excelMsgInfo 错误的详细信息
     */
    public ErrorMsg(Integer errorNumber, List<ExcelMsgInfo> excelMsgInfo) {
        this.errorNumber = errorNumber;
        this.excelMsgInfo = excelMsgInfo == null ? Collections.emptyList() : excelMsgInfo;
    }

    public Integer getErrorNumber() {
        return errorNumber;
    }

    public void setErrorNumber(Integer errorNumber) {
        this.errorNumber = errorNumber;
    }

    public List<ExcelMsgInfo> getMsgInfo() {
        return excelMsgInfo == null ? Collections.emptyList() : excelMsgInfo;
    }

    public void setMsgInfo(List<ExcelMsgInfo> excelMsgInfo) {
        this.excelMsgInfo = excelMsgInfo == null ? Collections.emptyList() : excelMsgInfo;
    }
}