package com.ec.singleOut.entity;

/**
 * Created by ecuser on 2015/12/31.
 */
public class CrmDetailClassEntity {

    private int f_id;
    private long f_crm_id;
    private long f_class_id;
    private int f_class_type;

    public int getF_id() {
        return f_id;
    }

    public CrmDetailClassEntity setF_id(int f_id) {
        this.f_id = f_id;
        return this;
    }

    public int getF_class_type() {
        return f_class_type;
    }

    public CrmDetailClassEntity setF_class_type(int f_class_type) {
        this.f_class_type = f_class_type;
        return this;
    }

    public long getF_class_id() {
        return f_class_id;
    }

    public CrmDetailClassEntity setF_class_id(long f_class_id) {
        this.f_class_id = f_class_id;
        return this;
    }

    public long getF_crm_id() {
        return f_crm_id;
    }

    public CrmDetailClassEntity setF_crm_id(long f_crm_id) {
        this.f_crm_id = f_crm_id;
        return this;
    }
}
