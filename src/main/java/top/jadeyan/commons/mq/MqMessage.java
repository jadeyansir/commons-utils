package top.jadeyan.commons.mq;

/**
 * mq 消息封装
 *
 * @param <T> 消息体泛型
 */
public class MqMessage<T> {

    /**
     * 消息的唯一标识，可以自定义当发现不是唯一的时候
     */
    private String messageId;
    /**
     * 消息体
     */
    private T messageBody;

    /**
     * 构造
     *
     * @param messageId   消息id
     * @param messageBody 消息体
     */
    public MqMessage(String messageId, T messageBody) {
        this.messageId = messageId;
        this.messageBody = messageBody;
    }

    public String getMessageId() {
        return messageId;
    }

    public T getMessageBody() {
        return messageBody;
    }
}
