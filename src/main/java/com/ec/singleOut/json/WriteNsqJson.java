package com.ec.singleOut.json;

/**
 * Created by ecuser on 2015/12/30.
 */
public class WriteNsqJson {

    private Data data;
    private String id;
    private String type;

    public Data getData() {
        return data;
    }

    public WriteNsqJson setData(Data data) {
        this.data = data;
        return this;
    }

    public String getType() {
        return type;
    }

    public WriteNsqJson setType(String type) {
        this.type = type;
        return this;
    }

    public String getId() {
        return id;
    }

    public WriteNsqJson setId(String id) {
        this.id = id;
        return this;
    }
}
