package com.example.y3spring.web.restapi.http.client;

import com.example.y3spring.web.restapi.http.HttpHeaders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class AbstractBufferingClientHttpRequest extends AbstractClientHttpRequest {

    private ByteArrayOutputStream bufferedOutput = new ByteArrayOutputStream(1024);

    @Override
    protected ClientHttpResponse executeInternal(HttpHeaders headers) throws IOException
    {
        byte[] bytes = this.bufferedOutput.toByteArray();
        if (headers.getContentLength() < 0) {
            headers.setContentLength(bytes.length);
        }
        ClientHttpResponse response=executeInternal(headers,bytes);
        this.bufferedOutput = new ByteArrayOutputStream(0);
        return response;
    }

    protected abstract ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput)
            throws IOException;

    protected OutputStream getBodyInternal(HttpHeaders headers) throws IOException{
        return this.bufferedOutput;
    }

}
