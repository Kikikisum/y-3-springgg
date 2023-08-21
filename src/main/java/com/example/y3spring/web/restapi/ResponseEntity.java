package com.example.y3spring.web.restapi;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

public class ResponseEntity<T> extends HttpEntity<T>{
    /**
     * 可能是int或者HttpStatus类型
     */
    private final Object status;

    public ResponseEntity(HttpStatus status) {
        this(null, null, status);
    }

    public ResponseEntity(@Nullable T body, HttpStatus status) {
        this(body, null, status);
    }

    public ResponseEntity(MultiValueMap<String, String> headers, HttpStatus status) {
        this(null, headers, status);
    }

    public ResponseEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers);
        // 检查status是否为空
        Assert.notNull(status, "HttpStatus must not be null");
        this.status = status;
    }
}
