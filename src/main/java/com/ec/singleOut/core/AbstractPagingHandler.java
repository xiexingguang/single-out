package com.ec.singleOut.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by jasshine_xxg on 2016/1/2.
 */
public abstract  class AbstractPagingHandler<T> implements  PagingHandler {

    private final Logger LOG = LogManager.getLogger(AbstractPagingHandler.class);

    public  void dealPageList(List<T> collections,int skipNum,T corpId) {

        if (collections == null) {
            return;
        }
        if (skipNum < 0) {
            throw new IllegalArgumentException("skpNum is not illegal ");
        }
        if ( collections.size() <= skipNum) {
            // throw new IllegalArgumentException("skipNum ");
            try {
                LOG.info("paginghandle corpid ,the corpid of cmrids < skipNUm , the corpid is  :" + corpId);
                pagingDealListCrmid(collections, corpId);
            } catch (Exception e) {
                //todo
            }
            return;
        }
        LOG.info("PageingHanlde deal the collections size is =============>" + collections.size());
        int index = 0;
        final int collectionSize = collections.size();
        int lookupCount = (collectionSize + skipNum - 1) / skipNum;
        LOG.info("need ===" + lookupCount +"==numbers to deal");
        int tempSkipNum = skipNum;
        if (collectionSize < skipNum) {
            tempSkipNum = collectionSize;
        }

        List<T> subList = null;
        for (int i = index ; i < lookupCount; i++) {
            subList = collections.subList(index, tempSkipNum);
            if (subList == null || subList.size() == 0) {
                continue;
            }
            beforePagingDealListCrmid();
            try {
                LOG.info("PageingHanlde deal the sub of collections size is =============>" + subList.size());
                pagingDealListCrmid(subList,corpId);
            } catch (Exception e) {
                dealExceptionWhenPageingDealL(e);
            }
            afterPagingDealListCrmid();
            index = index + skipNum;
            tempSkipNum = index + skipNum;
            if (tempSkipNum > collectionSize || index > collectionSize) {
                tempSkipNum = collectionSize;
            }
        }
    }

    protected abstract  void    beforePagingDealListCrmid();
    protected abstract void  afterPagingDealListCrmid();
    protected abstract void  dealExceptionWhenPageingDealL(Throwable throwable); //防御性容错
}
