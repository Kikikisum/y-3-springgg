package com.example.y3spring.web.restapi.http.client;

import com.example.y3spring.web.restapi.http.HttpMethod;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.URI;

public abstract class AbstractClientHttpRequestFactoryWrapper implements ClientHttpRequestFactory{
    private final ClientHttpRequestFactory requestFactory;


    protected AbstractClientHttpRequestFactoryWrapper(ClientHttpRequestFactory requestFactory) {
        // 检查requestFactory是否为空
        Assert.notNull(requestFactory, "ClientHttpRequestFactory must not be null");
        this.requestFactory = requestFactory;
    }


    @Override
    public static final ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
        return createRequest(uri, httpMethod, this.requestFactory);
    }

    protected abstract ClientHttpRequest createRequest(
            URI uri, HttpMethod httpMethod, ClientHttpRequestFactory requestFactory) throws IOException;

}
