package com.ec.singleOut.entity;

/**
 * Created by ecuser on 2015/12/29.
 */
public class CrmDetailEntity {


    private long f_crm_id;
    private long f_corp_id;
    private long f_user_id;
    private String f_create_Time;
    private int f_type;
    private int f_step;

    private String f_new_contact_time;
    private String f_lose_time;

    public int getF_step() {
        return f_step;
    }

    public CrmDetailEntity setF_step(int f_step) {
        this.f_step = f_step;
        return this;
    }

    public int getF_type() {
        return f_type;
    }

    public void setF_type(int f_type) {
        this.f_type = f_type;
    }

    public String getF_create_Time() {
        return f_create_Time;
    }

    public void setF_create_Time(String f_create_Time) {
        this.f_create_Time = f_create_Time;
    }

    public String getF_lose_time() {
        return f_lose_time;
    }

    public CrmDetailEntity setF_lose_time(String f_lose_time) {
        this.f_lose_time = f_lose_time;
        return this;
    }

    public String getF_new_contact_time() {
        return f_new_contact_time;
    }

    public CrmDetailEntity setF_new_contact_time(String f_new_contact_time) {
        this.f_new_contact_time = f_new_contact_time;
        return this;
    }

    public long getF_crm_id() {
        return f_crm_id;
    }

    public CrmDetailEntity setF_crm_id(long f_crm_id) {
        this.f_crm_id = f_crm_id;
        return this;
    }

    public long getF_corp_id() {
        return f_corp_id;
    }

    public CrmDetailEntity setF_corp_id(long f_corp_id) {
        this.f_corp_id = f_corp_id;
        return this;
    }

    public long getF_user_id() {
        return f_user_id;
    }

    public CrmDetailEntity setF_user_id(long f_user_id) {
        this.f_user_id = f_user_id;
        return this;
    }


    public boolean  equals(Object o) {
        if (o instanceof CrmDetailEntity) {
            return this.getF_corp_id() == ((CrmDetailEntity) o).getF_corp_id() && this.getF_crm_id() == ((CrmDetailEntity) o).getF_crm_id();
        }
        return false;
    }
}
