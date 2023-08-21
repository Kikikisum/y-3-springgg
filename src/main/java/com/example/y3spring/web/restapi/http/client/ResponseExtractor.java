package com.example.y3spring.web.restapi.http.client;

import org.springframework.lang.Nullable;

import java.io.IOException;

/**
 * 执行提取数据的实际工作
 * @param <T>
 */
@FunctionalInterface
public interface ResponseExtractor<T>{

    @Nullable
    T extractData(ClientHttpResponse response) throws IOException;
}
