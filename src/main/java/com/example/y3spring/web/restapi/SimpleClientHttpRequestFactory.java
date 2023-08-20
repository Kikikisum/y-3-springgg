package com.example.y3spring.web.restapi;

import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.net.Proxy;
import java.net.URI;

public class SimpleClientHttpRequestFactory implements ClientHttpRequestFactory{

    private static final int DEFAULT_CHUNK_SIZE = 4096;


    @Nullable
    private Proxy proxy;

    private boolean bufferRequestBody = true;

    private int chunkSize = DEFAULT_CHUNK_SIZE;

    private int connectTimeout = -1;

    private int readTimeout = -1;

    private boolean outputStreaming = true;

    @Nullable
    private AsyncListenableTaskExecutor taskExecutor;

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public void setBufferRequestBody(boolean bufferRequestBody) {
        this.bufferRequestBody = bufferRequestBody;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setOutputStreaming(boolean outputStreaming) {
        this.outputStreaming = outputStreaming;
    }

    public void setTaskExecutor(AsyncListenableTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }


    /**
     * 创造一个请求
     * @param uri
     * @param httpMethod
     * @return
     * @throws IOException
     */
    @Override
    public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
        return null;
    }
}
