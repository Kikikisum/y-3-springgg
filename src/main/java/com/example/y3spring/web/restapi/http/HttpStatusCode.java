package com.example.y3spring.web.restapi.http;

import java.io.Serializable;

public sealed interface HttpStatusCode extends Serializable permits HttpStatus{

    int value();

    boolean is1xxInformational();

    boolean is2xxSuccessful();

    boolean is3xxRedirection();

    boolean is4xxClientError();

    boolean is5xxServerError();

    boolean isError();

    default boolean isSameCodeAs(HttpStatusCode other) {
        return value() == other.value();
    }

    static HttpStatusCode valueOf(int code) {
        HttpStatus status = HttpStatus.resolve(code);
        if (status != null) {
            return status;
        }
        return null;
    }
}
