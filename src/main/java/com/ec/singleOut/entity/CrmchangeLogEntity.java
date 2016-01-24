package com.ec.singleOut.entity;

/**
 * Created by ecuser on 2016/1/6.
 */
public class CrmchangeLogEntity {

    private long f_crm_id;
    private long f_corp_id;
    private long f_user_id;
    private long f_act_userid;
    private int f_act_type;
    private int f_act_detail_type;
    private String f_time;
    private String f_memo;

    public long getF_crm_id() {
        return f_crm_id;
    }

    public CrmchangeLogEntity setF_crm_id(long f_crm_id) {
        this.f_crm_id = f_crm_id;
        return this;
    }

    public long getF_corp_id() {
        return f_corp_id;
    }

    public CrmchangeLogEntity setF_corp_id(long f_corp_id) {
        this.f_corp_id = f_corp_id;
        return this;
    }

    public long getF_user_id() {
        return f_user_id;
    }

    public CrmchangeLogEntity setF_user_id(long f_user_id) {
        this.f_user_id = f_user_id;
        return this;
    }

    public long getF_act_userid() {
        return f_act_userid;
    }

    public CrmchangeLogEntity setF_act_userid(long f_act_userid) {
        this.f_act_userid = f_act_userid;
        return this;
    }

    public int getF_act_type() {
        return f_act_type;
    }

    public CrmchangeLogEntity setF_act_type(int f_act_type) {
        this.f_act_type = f_act_type;
        return this;
    }

    public int getF_act_detail_type() {
        return f_act_detail_type;
    }

    public CrmchangeLogEntity setF_act_detail_type(int f_act_detail_type) {
        this.f_act_detail_type = f_act_detail_type;
        return this;
    }

    public String getF_time() {
        return f_time;
    }

    public CrmchangeLogEntity setF_time(String f_time) {
        this.f_time = f_time;
        return this;
    }

    public String getF_memo() {
        return f_memo;
    }

    public CrmchangeLogEntity setF_memo(String f_memo) {
        this.f_memo = f_memo;
        return this;
    }
}
