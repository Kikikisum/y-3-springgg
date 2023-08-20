package com.example.y3spring.web.restapi;

import java.io.IOException;
import java.net.URI;

@FunctionalInterface
public interface ClientHttpRequestFactory {
    /**
     * 为url和http方法创建新的请求
     * @param uri
     * @param httpMethod
     * @return
     * @throws IOException
     */
    ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException;
}
