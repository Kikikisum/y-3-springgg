package com.example.y3spring.web.restapi.http.converter;

import org.springframework.core.NestedRuntimeException;
import org.springframework.lang.Nullable;

public class HttpMessageNotWritableException extends HttpMessageConversionException {

    public HttpMessageNotWritableException(String msg) {
        super(msg);
    }

    public HttpMessageNotWritableException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
