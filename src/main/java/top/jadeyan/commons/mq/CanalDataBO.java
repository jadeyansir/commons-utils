package top.jadeyan.commons.mq;

/**
 * canal 数据内容 DTO
 *
 * @param <T> 泛型
 */
public class CanalDataBO<T> {
    /**
     * 当前值
     */
    private T current;
    /**
     * 更新值
     */
    private T old;

    public T getCurrent() {
        return current;
    }

    public void setCurrent(T current) {
        this.current = current;
    }

    public T getOld() {
        return old;
    }

    public void setOld(T old) {
        this.old = old;
    }
}
