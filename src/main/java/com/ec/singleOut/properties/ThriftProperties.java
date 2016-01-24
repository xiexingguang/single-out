package com.ec.singleOut.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by ecuser on 2015/12/31.
 */
@Component
public class ThriftProperties {


    @Value("${thrift.host}")
    public  String host;
    @Value("${thrift.port}")
    public int port;


}
