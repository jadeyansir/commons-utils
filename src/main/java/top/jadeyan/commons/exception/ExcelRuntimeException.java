package top.jadeyan.commons.exception;

/**
 * Excel 运行时异常
 *
 * @author yan
 * @create 2022/04/18
 */
public class ExcelRuntimeException extends RuntimeException {
    /**
     * 构建
     *
     * @param message 错误信息
     */
    public ExcelRuntimeException(String message) {
        super(message);
    }

    /**
     * 构造
     *
     * @param message 错误消息
     * @param cause   错误
     */
    public ExcelRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造
     *
     * @param cause 错误
     */
    public ExcelRuntimeException(Throwable cause) {
        super(cause);
    }
}

