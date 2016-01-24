package com.ec.singleOut.bean;

import java.util.List;

/**
 * Created by jasshine_xxg on 2016/1/2.
 */
public class LoseCrmId {

    private long corpId;
    private List<Long> crmIds;
    private int stage;

    public long getCorpId() {
        return corpId;
    }

    public void setCorpId(long corpId) {
        this.corpId = corpId;
    }

    public List<Long> getCrmIds() {
        return crmIds;
    }

    public void setCrmIds(List<Long> crmIds) {
        this.crmIds = crmIds;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }
}
