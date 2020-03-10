package com.bannad927.es.entity;

/**
 * @author cbb
 * @date 2020.3.10
 */
public class EsEntity<T> {
    //文档id
    private String id;
    //一条文档
    private T data;

    public EsEntity() {
    }

    public EsEntity(String id, T data) {
        this.data = data;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
