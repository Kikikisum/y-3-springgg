package com.example.y3spring.web.restapi.client;

import com.example.y3spring.web.restapi.HttpHeaders;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

final class SimpleBufferingClientHttpRequest extends AbstractBufferingClientHttpRequest {


    private final HttpURLConnection connection;

    private final boolean outputStreaming;


    SimpleBufferingClientHttpRequest(HttpURLConnection connection, boolean outputStreaming) {
        this.connection = connection;
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
    protected ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException {
        addHeaders(this.connection, headers);
        this.connection.connect();
        if (this.connection.getDoOutput()) {
            // 写到输出流中
            FileCopyUtils.copy(bufferedOutput, this.connection.getOutputStream());
        }
        else {
            this.connection.getResponseCode();
        }
        // 响应一个ClientHttpResponse对象
        return new SimpleClientHttpResponse(this.connection);
    }


    /**
     * 把headers添加到http连接上
     * @param connection
     * @param headers
     */
    static void addHeaders(HttpURLConnection connection, HttpHeaders headers) {
        headers.forEach((headerName, headerValues) -> {
            if (HttpHeaders.COOKIE.equalsIgnoreCase(headerName)) {
                String headerValue = StringUtils.collectionToDelimitedString(headerValues, "; ");
                connection.setRequestProperty(headerName, headerValue);
            }
            else {
                for (String headerValue : headerValues) {
                    String actualHeaderValue = headerValue != null ? headerValue : "";
                    connection.addRequestProperty(headerName, actualHeaderValue);
                }
            }
        });
    }


}
