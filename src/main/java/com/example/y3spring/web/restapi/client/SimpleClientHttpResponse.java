package com.example.y3spring.web.restapi.client;

import com.example.y3spring.web.restapi.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

final class SimpleClientHttpResponse extends AbstractClientHttpResponse {

    private final HttpURLConnection connection;

    @Nullable
    private HttpHeaders headers;

    @Nullable
    private InputStream responseStream;

    SimpleClientHttpResponse(HttpURLConnection connection){
        this.connection=connection;
    }

    /**
     * @return
     * @throws IOException
     */
    @Override
    public int getRawStatusCode() throws IOException {
        return this.connection.getResponseCode();
    }

    /**
     * @return
     * @throws IOException
     */
    @Override
    public String getStatusText() throws IOException {
        return this.connection.getResponseMessage();
    }

    /**
     *
     */
    @Override
    public void close() {
        if (this.responseStream!=null)
        {
            try{
                try {
                    StreamUtils.drain(this.responseStream);
                    this.responseStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @return
     * @throws IOException
     */
    @Override
    public InputStream getBody() throws IOException {
        InputStream stream =this.connection.getErrorStream();
        if (stream==null){
            this.responseStream=this.connection.getInputStream();
        }
        else{
            this.responseStream=stream;
        }
        return  this.responseStream;
    }

    /**
     * @return
     */
    @Override
    public HttpHeaders getHeaders() {
        if (this.headers == null) {
            this.headers = new HttpHeaders();
            String name = this.connection.getHeaderFieldKey(0);
            if (StringUtils.hasLength(name)) {
                this.headers.add(name, this.connection.getHeaderField(0));
            }
            int i = 1;
            while (true) {
                name = this.connection.getHeaderFieldKey(i);
                if (!StringUtils.hasLength(name)) {
                    break;
                }
                this.headers.add(name, this.connection.getHeaderField(i));
                i++;
            }
        }
        return this.headers;
    }
}
