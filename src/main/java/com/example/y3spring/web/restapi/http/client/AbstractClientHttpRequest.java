package com.example.y3spring.web.restapi.http.client;

import com.example.y3spring.web.restapi.http.HttpHeaders;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.OutputStream;

public abstract class AbstractClientHttpRequest implements ClientHttpRequest {

    private final HttpHeaders headers = new HttpHeaders();

    private boolean executed = false;

    @Override
    public final ClientHttpResponse execute() throws IOException {
        assertNotExecuted();
        ClientHttpResponse result = executeInternal(this.headers);
        this.executed = true;
        return result;
    }

    /**
     * 判断request是否被执行过
     */
    protected void assertNotExecuted() {
        Assert.state(!this.executed, "ClientHttpRequest already executed");
    }

    /**
     * 给定的标头和内容写入HTTP请求的抽象模板方法
     * @param headers
     * @return
     * @throws IOException
     */
    protected abstract ClientHttpResponse executeInternal(HttpHeaders headers) throws IOException;

    @Override
    public final OutputStream getBody() throws IOException {
        assertNotExecuted();
        return getBodyInternal(this.headers);
    }

    protected abstract OutputStream getBodyInternal(HttpHeaders headers) throws IOException;

    @Override
    public final HttpHeaders getHeaders() {
        return (this.executed ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers);
    }
}
