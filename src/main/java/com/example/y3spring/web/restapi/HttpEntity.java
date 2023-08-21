package com.example.y3spring.web.restapi;

import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

public class HttpEntity<T> {

    /**
     * 没有内容和header
     */
    public static final HttpEntity<?> EMPTY = new HttpEntity<>();


    private final HttpHeaders headers;

    @Nullable
    private final T body;

    protected HttpEntity() {
        this(null, null);
    }


    public HttpEntity(T body) {
        this(body, null);
    }

    public HttpEntity(MultiValueMap<String, String> headers) {
        this(null, headers);
    }

    public HttpEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers) {
        this.body = body;
        HttpHeaders tempHeaders = new HttpHeaders();
        if (headers != null) {
            tempHeaders.putAll(headers);
        }
        this.headers = HttpHeaders.readOnlyHttpHeaders(tempHeaders);
    }

}
