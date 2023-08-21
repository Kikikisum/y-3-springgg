package com.example.y3spring.web.restapi.http.client;

import com.example.y3spring.web.restapi.http.HttpMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.URI;

public class HttpAccessor {
    protected final Log logger = LogFactory.getLog(getClass());

    private ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();


    public void setRequestFactory(ClientHttpRequestFactory requestFactory) {
        // 检查requestFactory是否为空
        Assert.notNull(requestFactory, "ClientHttpRequestFactory must not be null");
        this.requestFactory = requestFactory;
    }

    public ClientHttpRequestFactory getRequestFactory() {
        return this.requestFactory;
    }

    /**
     * 通过自身的requestFactory来创建一个新的request
     * @param url
     * @param method
     * @return
     * @throws IOException
     */
    protected ClientHttpRequest createRequest(URI url, HttpMethod method) throws IOException {
        ClientHttpRequest request = getRequestFactory().createRequest(url, method);
        return request;
    }
}
