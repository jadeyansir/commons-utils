package top.jadeyan.commons.model;

import java.util.Map;

/**
 * 初始化map
 *
 * @param <K> key 泛型
 * @param <V> value 泛型
 */
public class InitMap<K, V> {

    private Map<K, V> map;

    /**
     * 初始化
     *
     * @param map map 对象
     */
    public InitMap(Map<K, V> map) {
        this.map = map;
    }

    /**
     * 设置 key value
     *
     * @param key   key
     * @param value value
     * @return 当期对象
     */
    public InitMap<K, V> keyValue(K key, V value) {
        map.put(key, value);
        return this;
    }

    /**
     * 获取map
     *
     * @return map 对象
     */
    public Map<K, V> getMap() {
        return this.map;
    }
}
