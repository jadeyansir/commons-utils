package top.jadeyan.commons.exception;


import top.jadeyan.commons.enums.IExceptionEnum;

/**
 * 运行时异常
 *
 * @author yan
 */
public class BusinessException extends RuntimeException {
    private final int code;
    private final String message;

    /**
     * 构建 使用exception枚举构建业务exception
     *
     * @param iExceptionEnum exception
     */
    public BusinessException(IExceptionEnum iExceptionEnum) {
        super(iExceptionEnum.getMessage());
        this.code = iExceptionEnum.getCode();
        this.message = iExceptionEnum.getMessage();

    }

    public int getCode() {
        return code;
    }

    /**
     * 构建 业务exception
     *
     * @param code    错误码
     * @param message 错误信息
     */
    public BusinessException(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public BusinessException(String message) {
        this.code = 0;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
