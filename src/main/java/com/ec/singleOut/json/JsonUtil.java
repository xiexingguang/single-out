package com.ec.singleOut.json;

import com.ec.singleOut.entity.CrmDetailEntity;
import com.ec.singleOut.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ecuser on 2015/12/30.
 */
public class JsonUtil {


    public static WriteNsqJson  chanageCrmDetail2WriteNsqJson(CrmDetailEntity crmDetailEntity) {

        Data data = new Data();
        DefaultMessage defaultMessage = new DefaultMessage();
        WriteNsqJson writeNsqJson1 = new WriteNsqJson();

        defaultMessage.setF_contact_num("");
        defaultMessage.setF_content("触发掉单规则，批量放弃客户");
        defaultMessage.setF_creat_time(DateUtil.convertDate2String(new Date()));
        defaultMessage.setF_crm_id(crmDetailEntity.getF_crm_id());
        defaultMessage.setF_from(10);
        defaultMessage.setF_is_addclient(0);
        defaultMessage.setF_msg_type(0);
        defaultMessage.setF_operate_type(131);
        defaultMessage.setF_ontime_flag(0);
        defaultMessage.setF_provice("");
        defaultMessage.setF_static_time(0);
        defaultMessage.setF_style(3);
        defaultMessage.setF_type(1);
        defaultMessage.setF_user_id(0);
        defaultMessage.setF_waste_id("");
        defaultMessage.setF_corp_id(crmDetailEntity.getF_corp_id());

        data.setDefault_message(defaultMessage);
        data.setGroup(1);
        data.setSubtype("");

        writeNsqJson1.setData(data);
        writeNsqJson1.setId("0");
        writeNsqJson1.setType("1");

        return writeNsqJson1;
    }

    public static List<WriteNsqJson> chanageCrmDetail2WriteNsqJsons(List<CrmDetailEntity> crmDetailEntities) {
        List<WriteNsqJson> writeNsqJsons = new ArrayList<WriteNsqJson>();
        for (CrmDetailEntity crmDetailEntity : crmDetailEntities) {
            WriteNsqJson writeNsqJson = chanageCrmDetail2WriteNsqJson(crmDetailEntity);
            writeNsqJsons.add(writeNsqJson);
        }
        return writeNsqJsons;
    }




}
