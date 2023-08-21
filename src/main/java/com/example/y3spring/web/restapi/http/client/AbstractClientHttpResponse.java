package com.example.y3spring.web.restapi.http.client;



import com.example.y3spring.web.restapi.http.HttpStatus;

import java.io.IOException;

public abstract class AbstractClientHttpResponse implements ClientHttpResponse {

    @Override
    public HttpStatus getStatusCode() throws IOException {
        return HttpStatus.valueOf(getRawStatusCode());
    }
}
