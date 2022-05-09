package top.jadeyan.commons.excelutils;

/**
 * 错误信息
 */
public class ExcelMsgInfo {
    private Integer lineNumber;
    private String errorMsg;

    public ExcelMsgInfo() {
    }

    /**
     * 全参构造
     *
     * @param lineNumber 错误行数
     * @param errorMsg   错误原因
     */
    public ExcelMsgInfo(Integer lineNumber, String errorMsg) {
        this.lineNumber = lineNumber;
        this.errorMsg = errorMsg;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}