package com.ec.singleOut.util;

import java.util.zip.CRC32;

/**
 * Created by ecuser on 2015/12/29.
 */
public class DbUtil {


    public static String getContactTimeTableName(long corpId, int totalTableNum) {

        CRC32 crc32 = new CRC32();
        crc32.update(String.valueOf(corpId).getBytes());
        long crcValue = crc32.getValue();
        int tableNum = (int) (crcValue % totalTableNum);
        String result = "";
        if (tableNum < 10) {
            result = String.format("d_ec_crmextend.t_crm_contact_time_0%d", tableNum);
        } else {
            result = String.format("d_ec_crmextend.t_crm_contact_time_%d", tableNum);
        }
        return result;
    }

    public static String getLoseRecordTableName(long corpId, int totalTableNum) {

        CRC32 crc32 = new CRC32();
        crc32.update(String.valueOf(corpId).getBytes());
        long crcValue = crc32.getValue();
        int tableNum = (int) (crcValue % totalTableNum);
        String result = "";
        if (tableNum < 10) {
            result = String.format("d_ec_crmextend.t_crm_lose_record_0%d", tableNum);
        } else {
            result = String.format("d_ec_crmextend.t_crm_lose_record_%d", tableNum);
        }
        return result;
    }

}
