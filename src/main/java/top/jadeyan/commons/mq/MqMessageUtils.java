package top.jadeyan.commons.mq;

import com.fasterxml.jackson.core.type.TypeReference;
import top.jadeyan.commons.json.JSON;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * mq 消息工具类
 *
 * @author yan
 */
public final class MqMessageUtils {

    private static final String ARRAY_PREFIX = "[";

    private MqMessageUtils() {
        // hide construct
    }

    /**
     * 转化mq 消息成为对象
     *
     * @param messageId       消息id
     * @param messageBodyJson 消息体json
     * @param clazz           消息体类型
     * @param <T>             泛型
     * @return 对象
     */
    public static <T> Optional<MqMessage<T>> parseMessage(String messageId, String messageBodyJson, Class<T> clazz) {
        if (StringUtils.isBlank(messageBodyJson)) {
            return Optional.empty();
        }
        T messageBody = JSON.parseObject(messageBodyJson, clazz);
        return Optional.of(new MqMessage<>(messageId, messageBody));
    }

    /**
     * 转化mq 消息成为对象
     *
     * @param messageId       消息id
     * @param messageBodyJson 消息体json
     * @param typeReference   复杂泛型类型
     * @param <T>             实体类
     * @return 对象
     */
    public static <T> Optional<MqMessage<T>> parseMessage(String messageId, String messageBodyJson, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(messageBodyJson)) {
            return Optional.empty();
        }
        T messageBody = JSON.parseObject(messageBodyJson, typeReference);
        return Optional.of(new MqMessage<>(messageId, messageBody));
    }

    /**
     * 转化消息为一个批量消息， 兼容多条和单条消息
     *
     * @param messageBatchId    消息id
     * @param messageBodyJson   json消息体
     * @param clazz             转化类型
     * @param eachMessageIdFunc 获取自定义每条消息 MessageId 方法，最好能保证唯一
     * @param <T>               消息体泛型
     * @return 转化消息为一个列表
     */
    public static <T> MqMessageBatch<T> parseMessageToBatch(
            String messageBatchId, String messageBodyJson, Class<T> clazz, Function<T, String> eachMessageIdFunc) {
        if (StringUtils.isBlank(messageBodyJson)) {
            return new MqMessageBatch<>("");
        }
        List<T> messageBodyList;
        if (messageBodyJson.startsWith(ARRAY_PREFIX)) {
            messageBodyList = JSON.parseArray(messageBodyJson, clazz);
        } else {
            messageBodyList = new ArrayList<>();
            messageBodyList.add(JSON.parseObject(messageBodyJson, clazz));
        }
        MqMessageBatch<T> result = new MqMessageBatch<>(messageBatchId);
        for (T messageBody : messageBodyList) {
            String newMessageId = eachMessageIdFunc.apply(messageBody);
            MqMessage<T> mqMessage = new MqMessage<>(newMessageId, messageBody);
            result.add(mqMessage);
        }
        return result;
    }
}
