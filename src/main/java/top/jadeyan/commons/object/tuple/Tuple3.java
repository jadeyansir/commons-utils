package top.jadeyan.commons.object.tuple;

/**
 * 三元组
 *
 * @param <T1> 第一个元素
 * @param <T2> 第二个元素
 * @param <T3> 第三个元素\
 * @author yan
 * @date 2023/1/10
 */
public class Tuple3<T1, T2, T3> extends Tuple2<T1, T2> {
    private T3 t3;

    public T3 getT3() {
        return t3;
    }

    public void setT3(T3 t3) {
        this.t3 = t3;
    }

    /**
     * 构造方法
     */
    public Tuple3() {
        super();
    }

    /**
     * 构造方法
     *
     * @param t1 第一个值
     * @param t2 第二个值
     * @param t3 第三个值
     */
    public Tuple3(T1 t1, T2 t2, T3 t3) {
        super(t1, t2);
        this.t3 = t3;
    }
}
