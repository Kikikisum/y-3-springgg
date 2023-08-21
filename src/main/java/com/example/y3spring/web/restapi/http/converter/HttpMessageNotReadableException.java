package com.example.y3spring.web.restapi.http.converter;

import com.example.y3spring.web.restapi.http.HttpInputMessage;
import org.springframework.lang.Nullable;

public class HttpMessageNotReadableException extends HttpMessageConversionException{

    @Nullable
    private final HttpInputMessage httpInputMessage;


    @Deprecated
    public HttpMessageNotReadableException(String msg) {
        super(msg);
        this.httpInputMessage = null;
    }

    @Deprecated
    public HttpMessageNotReadableException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
        this.httpInputMessage = null;
    }


}
