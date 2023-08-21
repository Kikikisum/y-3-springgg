package com.example.y3spring.web.restapi.http.client;

import com.example.y3spring.web.restapi.http.HttpEntity;
import com.example.y3spring.web.restapi.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Type;
import java.net.URI;

public class RequestEntity<T> extends HttpEntity<T> {
    @Nullable
    private final HttpMethod method;

    private final URI url;

    @Nullable
    private final Type type;

    public RequestEntity(HttpMethod method, URI url) {
        this(null, null, method, url, null);
    }

    public RequestEntity(@Nullable T body, HttpMethod method, URI url) {
        this(body, null, method, url, null);
    }

    public RequestEntity(@Nullable T body, HttpMethod method, URI url, Type type) {
        this(body, null, method, url, type);
    }
    
    public RequestEntity(MultiValueMap<String, String> headers, HttpMethod method, URI url) {
        this(null, headers, method, url, null);
    }
    
    public RequestEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers,
                         @Nullable HttpMethod method, URI url) {

        this(body, headers, method, url, null);
    }

    public RequestEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers,
                         @Nullable HttpMethod method, URI url, @Nullable Type type) {

        super(body, headers);
        this.method = method;
        this.url = url;
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
