package com.example.y3spring.web.restapi.client;

import com.example.y3spring.web.restapi.HttpRequest;

import java.io.IOException;

/**
 * 拦截客户端Http请求
 */
public interface ClientHttpRequestInterceptor {

    /**
     * 拦截请求
     * @param request
     * @param body
     * @param execution
     * @return
     * @throws IOException
     */
    ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException;
}
