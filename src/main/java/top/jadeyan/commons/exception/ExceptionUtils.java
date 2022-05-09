package top.jadeyan.commons.exception;

import top.jadeyan.commons.json.JSON;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 异常工具类
 *
 * @author yan
 **/
public final class ExceptionUtils {

    private static final String ARRAY_START_SIGN = "[";
    private static final String ARRAY_END_SIGN = "]";
    private static final String SINGLE_START_SIGN = "{";
    private static final String SINGLE_END_SIGN = "}";


    private ExceptionUtils() {
        // hide constructor
    }

    /**
     * 转化错误
     *
     * @param message 错误消息
     * @return 错误模型
     */
    public static List<ExceptionModel> parseHttpServerErrors(String message) {
        final int arrayStartIndex = message.indexOf(ARRAY_START_SIGN);
        final int singleStartIndex = message.indexOf(SINGLE_START_SIGN);
        if (arrayStartIndex >= 0) {
            final int arrayEndIndex = message.indexOf(ARRAY_END_SIGN);
            final String useMessage = message.substring(arrayStartIndex, arrayEndIndex + 1);
            return JSON.parseArray(useMessage, ExceptionModel.class);
        } else if (singleStartIndex >= 0) {
            final int singleEndIndex = message.indexOf(SINGLE_END_SIGN);
            final String useMessage = message.substring(singleStartIndex, singleEndIndex + 1);
            final ExceptionModel exceptionModel = JSON.parseObject(useMessage, ExceptionModel.class);
            return Collections.singletonList(exceptionModel);
        } else {
            return new ArrayList<>();
        }
    }


    /**
     * 转化错误
     *
     * @param message 错误消息
     * @return 错误模型
     */
    public static Optional<ExceptionModel> parseHttpServerError(String message) {
        final List<ExceptionModel> exceptionModels = parseHttpServerErrors(message);
        if (CollectionUtils.isEmpty(exceptionModels)) {
            return Optional.empty();
        } else {
            return Optional.of(exceptionModels.get(0));
        }
    }
}