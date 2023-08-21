package com.example.y3spring.web.restapi.http;

import com.example.y3spring.web.restapi.http.HttpHeaders;

public interface HttpMessage {

    /**
     * 获取请求的头
     * @return
     */
    HttpHeaders getHeaders();

}
