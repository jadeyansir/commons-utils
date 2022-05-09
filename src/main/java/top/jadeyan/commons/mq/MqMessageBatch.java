package top.jadeyan.commons.mq;

import java.util.ArrayList;

/**
 * mq 批量消息
 *
 * @param <T> 消息体类型
 * @author yan
 */
public class MqMessageBatch<T> extends ArrayList<MqMessage<T>> {

    private static final long serialVersionUID = -6392414389814490088L;

    private final String messageBatchId;

    /**
     * 构造
     *
     * @param messageBatchId 批量消息
     */
    public MqMessageBatch(String messageBatchId) {
        this.messageBatchId = messageBatchId;
    }

    public String getMessageBatchId() {
        return messageBatchId;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
