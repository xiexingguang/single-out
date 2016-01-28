/**
 * @Package com.ec.nsqc.bean
 * @Description: TODO
 * @author Comsys-ecuser
 * @date 2015年7月24日 上午10:14:46
 */

package com.ec.singleOut.bean;

/**
 * @ClassName: EsTask
 * @Description: TODO
 * @author Comsys-ecuser
 * @date 2015年7月24日 上午10:14:46
 */

public class EsTask {

    private long f_corp_id;
    private String f_crm_ids;
    private int f_type;
    private int f_time;
    private long f_user_id;
    private int f_status;
    private int f_from;

    public long getF_corp_id() {
        return f_corp_id;
    }

    public void setF_corp_id(long f_corp_id) {
        this.f_corp_id = f_corp_id;
    }

    public String getF_crm_ids() {
        return f_crm_ids;
    }

    public void setF_crm_ids(String f_crm_ids) {
        this.f_crm_ids = f_crm_ids;
    }

    public int getF_type() {
        return f_type;
    }

    public void setF_type(int f_type) {
        this.f_type = f_type;
    }

    public int getF_time() {
        return f_time;
    }

    public void setF_time(int f_time) {
        this.f_time = f_time;
    }

    public long getF_user_id() {
        return f_user_id;
    }

    public void setF_user_id(long f_user_id) {
        this.f_user_id = f_user_id;
    }

    public int getF_status() {
        return f_status;
    }

    public void setF_status(int f_status) {
        this.f_status = f_status;
    }

    public int getF_from() {
        return f_from;
    }

    public void setF_from(int f_from) {
        this.f_from = f_from;
    }

    @Override
    public String toString() {
        return "EsTask [f_corp_id=" + f_corp_id + ", f_crm_ids=" + f_crm_ids + ", f_type=" + f_type + ", f_time=" + f_time + ", f_user_id="
                        + f_user_id + ", f_status=" + f_status + ", f_from=" + f_from + "]";
    }

}
