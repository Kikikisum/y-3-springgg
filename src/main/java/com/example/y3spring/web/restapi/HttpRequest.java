package com.example.y3spring.web.restapi;

import org.springframework.lang.Nullable;

import java.net.URI;

public interface HttpRequest extends HttpMessage{
    @Nullable
    default HttpMethod getMethod(){
        return HttpMethod.resolve(getMethodValue());
    }

    /**
     * 以字符串的形式返回请求的Http方法
     * @return
     */
    String getMethodValue();

    /**
     * 返回URI
     * @return
     */
    URI getURI();

}
