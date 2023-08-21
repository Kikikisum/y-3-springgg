package com.example.y3spring.web.restapi.http.client;

import com.example.y3spring.web.restapi.http.HttpInputMessage;
import com.example.y3spring.web.restapi.http.HttpStatus;

import java.io.Closeable;
import java.io.IOException;

public interface ClientHttpResponse extends Closeable, HttpInputMessage {

    /**
     * 返回响应的Http状态码
     * @return HttpStatus
     * @throws IOException
     */
    HttpStatus getStatusCode() throws IOException;

    /**
     * 返回响应的Http状态码
     * @return 整数
     * @throws IOException
     */
    int getRawStatusCode() throws IOException;

    /**
     * 返回响应的Http状态文本
     * @return
     * @throws IOException
     */
    String getStatusText() throws IOException;

    /**
     * 关闭响应
     */
    @Override
    void close();
}
