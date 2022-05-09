//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package top.jadeyan.commons.http;

import java.io.Serializable;

/**
 * http 通用返回
 *
 * @param <T> 实体
 * @author yan
 */
@SuppressWarnings({"squid:S00100", "squid:S1948"})
public class RestResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Integer CODE_SUCCESS = 0;
    private static final Integer CODE_FAILURE = -1;
    private static final String MSG_SUCCESS = "success";
    private static final String MSG_FAILURE = "failure";
    private Integer code;
    private String message;
    private T data;

    public RestResponse() {
    }

    /**
     * 构造方法
     *
     * @param code    代码
     * @param message 消息
     * @param data    数据
     */
    public RestResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功
     *
     * @param data 成功数据
     * @param <T>  实体类型
     * @return 成功响应
     */
    public static <T> RestResponse<T> Success(T data) {
        return new RestResponse<>(CODE_SUCCESS, MSG_SUCCESS, data);
    }

    /**
     * 成功
     *
     * @param message 消息
     * @param data    成功数据
     * @param <T>     实体类型
     * @return 成功响应
     */
    public static <T> RestResponse<T> Success(String message, T data) {
        return new RestResponse<>(CODE_SUCCESS, message, data);
    }

    /**
     * 失败
     *
     * @param data 成功数据
     * @param <T>  实体类型
     * @return 失败响应
     */
    public static <T> RestResponse<T> Fail(T data) {
        return new RestResponse<>(CODE_FAILURE, MSG_FAILURE, data);
    }

    /**
     * 失败
     *
     * @param message 消息
     * @param data    成功数据
     * @param <T>     实体类型
     * @return 失败响应
     */
    public static <T> RestResponse<T> Fail(String message, T data) {
        return new RestResponse<>(CODE_FAILURE, message, data);
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public T getData() {
        return this.data;
    }
}
