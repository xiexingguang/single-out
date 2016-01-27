package com.ec.singleOut.core;

import java.util.List;

/**
 * Created by jasshine_xxg on 2016/1/2.
 */
public interface PagingHandler<T> {

    void pagingDealListCrmid(List<T> collections,T corpId)throws  Exception;

}
