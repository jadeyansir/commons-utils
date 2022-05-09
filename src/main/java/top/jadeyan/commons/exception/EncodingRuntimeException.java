package top.jadeyan.commons.exception;

/**
 * 编码运行时异常
 *
 * @author yan
 */
public class EncodingRuntimeException extends RuntimeException {

    /**
     * 构造方法
     *
     * @param cause 异常
     */
    public EncodingRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造
     *
     * @param message 错误消息
     * @param cause   错误
     */
    public EncodingRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
