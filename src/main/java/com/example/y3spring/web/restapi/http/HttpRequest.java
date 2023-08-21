package com.example.y3spring.web.restapi.http;

import com.example.y3spring.web.restapi.http.HttpMessage;
import com.example.y3spring.web.restapi.http.HttpMethod;
import org.springframework.lang.Nullable;

import java.net.URI;

public interface HttpRequest extends HttpMessage {
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
