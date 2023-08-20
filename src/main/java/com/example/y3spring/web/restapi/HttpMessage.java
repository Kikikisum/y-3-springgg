package com.example.y3spring.web.restapi;

import java.net.http.HttpHeaders;

public interface HttpMessage {

    /**
     * 获取请求的头
     * @return
     */
    HttpHeaders getHeaders();

}
