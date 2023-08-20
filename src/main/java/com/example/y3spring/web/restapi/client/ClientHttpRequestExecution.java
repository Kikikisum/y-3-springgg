package com.example.y3spring.web.restapi.client;

import com.example.y3spring.web.restapi.HttpRequest;

import java.io.IOException;

@FunctionalInterface
public interface ClientHttpRequestExecution {

    /**
     * 执行请求
     * @param request
     * @param body
     * @return
     * @throws IOException
     */
    ClientHttpResponse execute(HttpRequest request, byte[] body) throws IOException;
}
