package com.example.y3spring.web.restapi.http.converter;

import com.example.y3spring.web.restapi.http.HttpInputMessage;
import com.example.y3spring.web.restapi.http.HttpOutputMessage;
import com.example.y3spring.web.restapi.http.MediaType;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;

public interface GenericHttpMessageConverter<T> extends HttpMessageConverter<T>{

    boolean canRead(Type type, @Nullable Class<?> contextClass, @Nullable MediaType mediaType);

    T read(Type type, @Nullable Class<?> contextClass, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException;

    boolean canWrite(@Nullable Type type, Class<?> clazz, @Nullable MediaType mediaType);

    void write(T t, @Nullable Type type, @Nullable MediaType contentType, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException;
}
