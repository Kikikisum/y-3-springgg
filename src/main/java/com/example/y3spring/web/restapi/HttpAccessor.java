package com.example.y3spring.web.restapi;

import com.example.y3spring.web.restapi.client.ClientHttpRequestFactory;
import com.example.y3spring.web.restapi.client.SimpleClientHttpRequestFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpAccessor {
    protected final Log logger = LogFactory.getLog(getClass());

    private ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();


}
