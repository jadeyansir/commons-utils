package top.jadeyan.commons.object.tuple;

/**
 * 二元组
 *
 * @param <T1> 第一个数据
 * @param <T2> 第二个数据
 * @author yan
 * @date 2023/1/10
 */
public class Tuple2<T1, T2> {
    private T1 t1;
    private T2 t2;

    public T1 getT1() {
        return t1;
    }

    public void setT1(T1 t1) {
        this.t1 = t1;
    }

    public T2 getT2() {
        return t2;
    }

    public void setT2(T2 t2) {
        this.t2 = t2;
    }

    /**
     * 构造函数
     */
    public Tuple2() {
    }

    /**
     * 构造函数
     *
     * @param t1 第一个值
     * @param t2 第二个值
     */
    public Tuple2(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }
}
