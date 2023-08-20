package com.example.y3spring.web.restapi.client;

import com.example.y3spring.web.restapi.HttpHeaders;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

final class SimpleStreamingClientHttpRequest extends AbstractClientHttpRequest {
    private final HttpURLConnection connection;

    private final int chunkSize;

    @Nullable
    private OutputStream body;

    private final boolean outputStreaming;


    SimpleStreamingClientHttpRequest(HttpURLConnection connection, int chunkSize, boolean outputStreaming) {
        this.connection = connection;
        this.chunkSize = chunkSize;
        this.outputStreaming = outputStreaming;
    }


    @Override
    public String getMethodValue() {
        return this.connection.getRequestMethod();
    }

    @Override
    public URI getURI() {
        try {
            return this.connection.getURL().toURI();
        }
        catch (URISyntaxException ex) {
            throw new IllegalStateException("Could not get HttpURLConnection URI: " + ex.getMessage(), ex);
        }
    }



    @Override
    protected ClientHttpResponse executeInternal(HttpHeaders headers) {
        try {
            if (this.body != null) {
                this.body.close();
            }
            else {
                SimpleBufferingClientHttpRequest.addHeaders(this.connection, headers);
                this.connection.connect();
                this.connection.getResponseCode();
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("");
        }
        return new SimpleClientHttpResponse(this.connection);
    }

    /**
     * 还未实现
     * @param headers
     * @return
     * @throws IOException
     */
    @Override
    protected OutputStream getBodyInternal(HttpHeaders headers) throws IOException {
        return null;
    }
}
