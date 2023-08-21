package com.example.y3spring.web.restapi.http;

import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

public enum HttpMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;
    private static final Map<String, HttpMethod> mappings = new HashMap<>(16);

    static {
        for (HttpMethod httpMethod : values()) {
            mappings.put(httpMethod.name(), httpMethod);
        }
    }

    /**
     * 通过method的名字获取对应的HttpMethod
     * @param method
     * @return
     */
    @Nullable
    public static HttpMethod resolve(@Nullable String method) {
        return (method != null ? mappings.get(method) : null);
    }

    /**
     * 判断是否与方法匹配
     * @param method
     * @return
     */
    public boolean matches(String method) {
        return (this == resolve(method));
    }
}
