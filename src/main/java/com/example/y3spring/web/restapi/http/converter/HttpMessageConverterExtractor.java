package com.example.y3spring.web.restapi.http.converter;

import com.example.y3spring.web.restapi.http.client.ClientHttpResponse;
import com.example.y3spring.web.restapi.http.client.ResponseExtractor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class HttpMessageConverterExtractor<T> implements ResponseExtractor<T> {


    private final Type responseType;

    @Nullable
    private final Class<T> responseClass;

    private final List<HttpMessageConverter<?>> messageConverters;

    private final Log logger;

    public HttpMessageConverterExtractor(Type responseType, List<HttpMessageConverter<?>> messageConverters, Log logger) {
        Assert.notNull(responseType, "'responseType' must not be null");
        Assert.notEmpty(messageConverters, "'messageConverters' must not be empty");
        this.responseType = responseType;
        this.responseClass = (responseType instanceof Class ? (Class<T>) responseType : null);
        this.messageConverters = messageConverters;
        this.logger = logger;
    }
    public HttpMessageConverterExtractor(Class<T> responseType, List<HttpMessageConverter<?>> messageConverters) {
        this((Type) responseType, messageConverters);
    }

    public HttpMessageConverterExtractor(Type responseType, List<HttpMessageConverter<?>> messageConverters) {
        this(responseType, messageConverters, LogFactory.getLog(HttpMessageConverterExtractor.class));
    }


    /**
     * @param response
     * @return
     * @throws IOException
     */
    @Override
    public T extractData(ClientHttpResponse response) throws IOException {
        return null;
    }
}
