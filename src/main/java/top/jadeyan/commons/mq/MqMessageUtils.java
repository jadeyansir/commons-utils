package top.jadeyan.commons.mq;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import top.jadeyan.commons.enums.DataOperationEnum;
import top.jadeyan.commons.json.JSON;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static top.jadeyan.commons.object.ObjectExtensionUtils.getProperty;

/**
 * mq 消息工具类
 *
 * @author yan
 */
public final class MqMessageUtils {
    private static final Log logger = LogFactory.getLog(MqMessageUtils.class);

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

    /**
     * 处理mq数据方法类
     *
     * @param mqMessage            mq 消息
     * @param operationFunctionMap 操作数组
     * @param messageInfo          需要打印的日志信息
     */
    public static <T> void handleMqData(
            MqMessage<CanalFlatMessageBO<T>> mqMessage,
            Map<DataOperationEnum, Consumer<Collection<T>>> operationFunctionMap,
            String messageInfo) {
        Optional<DataOperationEnum> dataOperationEnumOptional =
                DataOperationEnum.getDataOperationEnumByText(mqMessage.getMessageBody().getType());
        if (!dataOperationEnumOptional.isPresent()) {
            String errMsg = JSON.toJSONString(mqMessage);
            logger.warn(String.format("[%s] can not resolve operation, message: %s", messageInfo, errMsg));
            return;
        }
        Consumer<Collection<T>> consumer = operationFunctionMap.get(dataOperationEnumOptional.get());
        if (Objects.isNull(consumer)) {
            String errMsg = JSON.toJSONString(mqMessage);
            logger.warn(String.format("[%s] can not find match func, message: %s", messageInfo, errMsg));
            return;
        }
        List<T> dataList = getProperty(mqMessage, MqMessage::getMessageBody, CanalFlatMessageBO::getData);
        if (CollectionUtils.isNotEmpty(dataList)) {
            consumer.accept(dataList);
        }
    }
}
