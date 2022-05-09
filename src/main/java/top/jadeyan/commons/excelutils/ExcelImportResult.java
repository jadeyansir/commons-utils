package top.jadeyan.commons.excelutils;

import java.util.Collections;
import java.util.List;

/**
 * 导入结果集
 *
 * @param <T> 泛型
 */
public final class ExcelImportResult<T> {

    private List<T> result;

    private ErrorMsg errorMsg;


    public ExcelImportResult() {
    }

    /**
     * 全参构造
     *
     * @param result   结果集
     * @param errorMsg 错误信息
     */
    public ExcelImportResult(List<T> result, ErrorMsg errorMsg) {
        this.result = result == null ? Collections.emptyList() : result;
        this.errorMsg = errorMsg;
    }

    public List<T> getResult() {
        return result == null ? Collections.emptyList() : result;
    }

    public void setResult(List<T> result) {
        this.result = result == null ? Collections.emptyList() : result;
    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(ErrorMsg errorMsg) {
        this.errorMsg = errorMsg;
    }
}
