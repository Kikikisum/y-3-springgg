package com.example.y3spring.web.restapi;



import java.io.IOException;

public abstract class AbstractClientHttpResponse implements ClientHttpResponse{

    @Override
    public HttpStatus getStatusCode() throws IOException {
        return HttpStatus.valueOf(getRawStatusCode());
    }
}
