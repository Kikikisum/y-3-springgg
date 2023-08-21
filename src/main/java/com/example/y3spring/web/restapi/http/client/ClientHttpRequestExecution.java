package com.example.y3spring.web.restapi.http.client;

import com.example.y3spring.web.restapi.http.HttpRequest;

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
