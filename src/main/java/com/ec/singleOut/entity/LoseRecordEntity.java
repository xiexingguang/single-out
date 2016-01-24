package com.ec.singleOut.entity;

/**
 * Created by ecuser on 2016/1/3.
 */
public class LoseRecordEntity {

    private long f_crm_id;
    private long f_user_id;
    private long f_corp_id;
    private int f_type;
    private String f_last_contact_time;
    private String f_lose_time;
    private String f_create_Time;

    public long getF_crm_id() {
        return f_crm_id;
    }

    public LoseRecordEntity setF_crm_id(long f_crm_id) {
        this.f_crm_id = f_crm_id;
        return this;
    }

    public String getF_createTime() {
        return f_create_Time;
    }

    public LoseRecordEntity setF_createTime(String f_createTime) {
        this.f_create_Time = f_createTime;
        return this;
    }

    public String getF_lose_time() {
        return f_lose_time;
    }

    public LoseRecordEntity setF_lose_time(String f_lose_time) {
        this.f_lose_time = f_lose_time;
        return this;
    }

    public String getF_last_contact_name() {
        return f_last_contact_time;
    }

    public LoseRecordEntity setF_last_contact_name(String f_last_contact_name) {
        this.f_last_contact_time = f_last_contact_name;
        return this;
    }

    public int getF_type() {
        return f_type;
    }

    public LoseRecordEntity setF_type(int f_type) {
        this.f_type = f_type;
        return this;
    }

    public long getF_corp_id() {
        return f_corp_id;
    }

    public LoseRecordEntity setF_corp_id(long f_corp_id) {
        this.f_corp_id = f_corp_id;
        return this;
    }

    public long getF_usr_id() {
        return f_user_id;
    }

    public LoseRecordEntity setF_usr_id(long f_user_id) {
        this.f_user_id = f_user_id;
        return this;
    }
}
