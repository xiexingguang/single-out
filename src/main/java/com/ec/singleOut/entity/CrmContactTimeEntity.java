package com.ec.singleOut.entity;

/**
 * Created by ecuser on 2015/12/29.
 */
public class CrmContactTimeEntity {

    private long  f_crm_id;
    private int f_corp_id;
    private int f_type;
    private String f_contact_time;

    public long getF_crm_id() {
        return f_crm_id;
    }

    public CrmContactTimeEntity setF_crm_id(long f_crm_id) {
        this.f_crm_id = f_crm_id;
        return this;
    }

    public String getF_contact_time() {
        return f_contact_time;
    }

    public CrmContactTimeEntity setF_contact_time(String f_contact_time) {
        this.f_contact_time = f_contact_time;
        return this;
    }

    public int getF_type() {
        return f_type;
    }

    public CrmContactTimeEntity setF_type(int f_type) {
        this.f_type = f_type;
        return this;
    }

    public int getF_corp_id() {
        return f_corp_id;
    }

    public CrmContactTimeEntity setF_corp_id(int f_corp_id) {
        this.f_corp_id = f_corp_id;
        return this;
    }





}
