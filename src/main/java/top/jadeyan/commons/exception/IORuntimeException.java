package top.jadeyan.commons.exception;

/**
 * IO运行时异常
 *
 * @author yan
 */
public class IORuntimeException extends RuntimeException {
    /**
     * 构造
     *
     * @param message 错误消息
     */
    public IORuntimeException(String message) {
        super(message);
    }

    /**
     * 构造
     *
     * @param message 错误消息
     * @param cause   错误
     */
    public IORuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造
     *
     * @param cause 错误
     */
    public IORuntimeException(Throwable cause) {
        super(cause);
    }
}
