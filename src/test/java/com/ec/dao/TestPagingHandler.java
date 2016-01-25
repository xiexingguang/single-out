package com.ec.dao;

import com.ec.singleOut.core.AbstractPagingHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ecuser on 2016/1/25.
 */
public class TestPagingHandler {


    public static void main(String[] args) {

        // 封装crmdetail 数据

        List<Long> crmDetailEntities = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            crmDetailEntities.add(10L);
        }

        clasagingExcuteCrmId pagingExcuteCrmId = new clasagingExcuteCrmId();
        // pagingExcuteCrmId.setLoseCrmIds(crmDetailEntities);
        pagingExcuteCrmId.dealPageList(crmDetailEntities, 200, 444L);

    }

    static class clasagingExcuteCrmId extends AbstractPagingHandler<Long> {

        @Override
        protected void beforePagingDealListCrmid() {

        }

        @Override
        protected void afterPagingDealListCrmid() {

        }

        @Override
        protected void dealExceptionWhenPageingDealL(Throwable throwable) {

        }

        @Override
        public void pagingDealListCrmid(List collections, Object corpId) throws InterruptedException {
            System.out.println("============:" + collections.size());
        }
    }

}
