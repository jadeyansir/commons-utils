package top.jadeyan.commons.model;

public class GenericWrapper<T> {

    private Integer id;

    private T object;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
