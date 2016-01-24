package com.ec.singleOut.entity;

/**
 * Created by ecuser on 2015/12/28.
 */
public class CrmLoseRuleEntity {

     private long f_corp_id;
     private int f_tag_type;
     private String f_tag_set;
     private int f_time_set;
     private int f_rule_status;
     private int f_create_time;
     private int f_update_time;
     private int f_user_id;
     private int f_no_contact1;
     private int f_no_contact2;
     private int f_no_update;
     private String f_contact_type;


    @Override
    public String toString() {
        return "{ f_crop_id : "+f_corp_id +"[ contact_1 :" + f_no_contact1 +", contact_2:"+f_no_contact2 +",no_update :" +f_no_update +"] }";
    }


    public long getF_crop_id() {
        return f_corp_id;
    }

    public CrmLoseRuleEntity setF_crop_id(long f_crop_id) {
        this.f_corp_id = f_crop_id;
        return this;
    }

    public int getF_tag_type() {
        return f_tag_type;
    }

    public CrmLoseRuleEntity setF_tag_type(int f_tag_type) {
        this.f_tag_type = f_tag_type;
        return this;
    }

    public String getF_tag_set() {
        return f_tag_set;
    }

    public CrmLoseRuleEntity setF_tag_set(String f_tag_set) {
        this.f_tag_set = f_tag_set;
        return this;
    }

    public int getF_time_set() {
        return f_time_set;
    }

    public CrmLoseRuleEntity setF_time_set(int f_time_set) {
        this.f_time_set = f_time_set;
        return this;
    }

    public int getF_rule_status() {
        return f_rule_status;
    }

    public CrmLoseRuleEntity setF_rule_status(int f_rule_status) {
        this.f_rule_status = f_rule_status;
        return this;
    }

    public int getF_create_time() {
        return f_create_time;
    }

    public CrmLoseRuleEntity setF_create_time(int f_create_time) {
        this.f_create_time = f_create_time;
        return this;
    }

    public int getF_update_time() {
        return f_update_time;
    }

    public CrmLoseRuleEntity setF_update_time(int f_update_time) {
        this.f_update_time = f_update_time;
        return this;
    }

    public int getF_user_id() {
        return f_user_id;
    }

    public CrmLoseRuleEntity setF_user_id(int f_user_id) {
        this.f_user_id = f_user_id;
        return this;
    }

    public int getF_no_contact1() {
        return f_no_contact1;
    }

    public CrmLoseRuleEntity setF_no_contact1(int f_no_contact1) {
        this.f_no_contact1 = f_no_contact1;
        return this;
    }

    public int getF_no_contact2() {
        return f_no_contact2;
    }

    public CrmLoseRuleEntity setF_no_contact2(int f_no_contact2) {
        this.f_no_contact2 = f_no_contact2;
        return this;
    }

    public int getF_no_update() {
        return f_no_update;
    }

    public CrmLoseRuleEntity setF_no_update(int f_no_update) {
        this.f_no_update = f_no_update;
        return this;
    }

    public String getF_contact_type() {
        return f_contact_type;
    }

    public CrmLoseRuleEntity setF_contact_type(String f_contact_type) {
        this.f_contact_type = f_contact_type;
        return this;
    }
}
