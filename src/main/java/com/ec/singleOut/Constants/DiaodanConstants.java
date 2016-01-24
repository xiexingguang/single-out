package com.ec.singleOut.Constants;

/**
 * Created by ecuser on 2015/12/30.
 */
public class DiaodanConstants {

    public static final int CONTATC_TYPE_UPDATECRM = 8;

    public static final String LOSE_REMIND_FORMAT="type=999+content=由于您未对这些客户保持良好跟进习惯，将在48小时内掉入公共库，请尽快处理！+title=系统消息+url=http://my.workec.com/crm/index/lose+utltitle=查看+acttitle=我的客户+ctitle=您有%s位客户即将掉入公共库！";

    public static final String EXECUT_SUCCESS = "1";

    public static final String EXECUT_FAIL = "2";


    public static final int UPDATE_DETAIL = 0;
    public static final int UPDATE_CRM_RELATION = 1;
    public static final int DELETE_SHARE_RELATION =2;
    public static final int DELETE_CRM_CORP_RELATION = 3;
    public static final int UPDATE_QQ = 4;
    public static final int UPDATE_CRM_PLAN = 5;
    public static final int UPDATE_CHANGE_LOG = 6;
    public static final int UPDATE_REDIS_LIMIT = 7;
    public static final int WRITE_ES = 8;
    public static final int WRITE_NSQ = 9;
    public static final int UPDATE_MEMCACHE = 10;



    public  static final String CORP_GROUP_NUM = "corpgroup_num";
    public  static final String CRM_GROUP_NUM = "crmgroup_num";


    public static final String CRM_RDIS_KEY = "USER_CRM_LIMIT_";

   //无标签的
    public static final String  NO_TAG = "0";

}
