package com.ec.singleOut.json;

/**
 * Created by ecuser on 2015/12/30.
 */
public class Data {

    private DefaultMessage default_message;
    private int group;
    private String subtype;


    public DefaultMessage getDefault_message() {
        return default_message;
    }

    public Data setDefault_message(DefaultMessage default_message) {
        this.default_message = default_message;
        return this;
    }

    public String getSubtype() {
        return subtype;
    }

    public Data setSubtype(String subtype) {
        this.subtype = subtype;
        return this;
    }

    public int getGroup() {
        return group;
    }

    public Data setGroup(int group) {
        this.group = group;
        return this;
    }
}
