package com.ec.singleOut.core;

import com.ec.singleOut.bean.LoseCrmId;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by ecuser on 2015/12/30.
 *
 * 掉单duobobo服务的上下文
 * 数据环境
 *
 */
@Component
public class SingleOutContext {

    /**
     * 存储执行调单失败的 crmids,
     */
    public final static BlockingQueue<LoseCrmId> FAIL_TO_EXCUTE_LOSECRM = new ArrayBlockingQueue<LoseCrmId>(2000);



}
