package top.jadeyan.commons.enums;

/**
 * 异常枚举
 *
 * @author yan
 */
public enum ExceptionEnum implements IExceptionEnum {
    /**
     * 远程调用服务失败
     */
    REMOTE_CALL_FAILED(10100, "远程调用服务失败"),
    /**
     * 部分枚举类型无法找到错误
     */
    NON_ENUM_ERROR(10031, "部分枚举类型无法找到错误");

    private final int code;
    private final String message;

    ExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
