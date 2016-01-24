package com.ec.singleOut.entity;

/**
 * Created by ecuser on 2015/12/30.
 */
public class NsqTaskEntity {

    private String f_topic_name;
    private long f_corp_id;
    private long f_user_id;
    private String f_message;
    private int f_create_time;
    private int f_from;



    public String getF_topic_name() {
        return f_topic_name;
    }

    public NsqTaskEntity setF_topic_name(String f_topic_name) {
        this.f_topic_name = f_topic_name;
        return this;
    }

    public int getF_from() {
        return f_from;
    }

    public NsqTaskEntity setF_from(int f_from) {
        this.f_from = f_from;
        return this;
    }

    public int getF_create_time() {
        return f_create_time;
    }

    public NsqTaskEntity setF_create_time(int f_create_time) {
        this.f_create_time = f_create_time;
        return this;
    }

    public String getF_message() {
        return f_message;
    }

    public NsqTaskEntity setF_message(String f_message) {
        this.f_message = f_message;
        return this;
    }

    public long getF_user_id() {
        return f_user_id;
    }

    public NsqTaskEntity setF_user_id(long f_user_id) {
        this.f_user_id = f_user_id;
        return this;
    }

    public long getF_corp_id() {
        return f_corp_id;
    }

    public NsqTaskEntity setF_corp_id(long f_corp_id) {
        this.f_corp_id = f_corp_id;
        return this;
    }




}
