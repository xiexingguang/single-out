package com.ec.singleOut.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by ecuser on 2016/1/4.
 */
@Component
public class ConfigProperties {

    @Value("${task.dealLosePath}")
    public String DEAL_LOSE_PATH;
    @Value("${task.dealWillLosePath}")
    public String DEAL_WILL_LOSE_PATH;
    @Value("${intervalHour}")
    public int intervalHour;
    @Value("${IM.host}")
    public String IM_HOST;
    @Value("${IM.port}")
    public int IM_PORT;
    @Value("${operations.number}")
    public int OPEAATION_NUMBER;

}
