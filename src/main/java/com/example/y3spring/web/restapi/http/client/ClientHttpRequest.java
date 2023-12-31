package com.example.y3spring.web.restapi.http.client;

import com.example.y3spring.web.restapi.http.HttpOutputMessage;
import com.example.y3spring.web.restapi.http.HttpRequest;

import java.io.IOException;

/**
 * 通过客户端HTTP请求的接口
 */
public interface ClientHttpRequest extends HttpRequest, HttpOutputMessage {

    /**
     * 执行请求
     * @return 响应客户端结果
     * @throws IOException
     */
    ClientHttpResponse execute() throws IOException;

}
