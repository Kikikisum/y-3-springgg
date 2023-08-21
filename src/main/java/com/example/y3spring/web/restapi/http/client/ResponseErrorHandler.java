package com.example.y3spring.web.restapi.http.client;

import com.example.y3spring.web.restapi.http.HttpMethod;

import java.io.IOException;
import java.net.URI;

public interface ResponseErrorHandler {

    /**
     * 是否状态码中有错误码
     * @param response
     * @return
     * @throws IOException
     */
    boolean hasError(ClientHttpResponse response) throws IOException;

    /**
     * 实际执行应对错误码的操作
     * @param response
     * @throws IOException
     */
    void handleError(ClientHttpResponse response) throws IOException;

    default void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        handleError(response);
    }
}
