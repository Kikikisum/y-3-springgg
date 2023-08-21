package com.example.y3spring.web.restapi.http.converter;

import com.example.y3spring.web.restapi.http.HttpInputMessage;
import com.example.y3spring.web.restapi.http.HttpOutputMessage;
import com.example.y3spring.web.restapi.http.MediaType;
import org.springframework.lang.Nullable;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public interface HttpMessageConverter<T> {

    /**
     * 该转换器是否能读取指定的类
     * @param clazz
     * @param mediaType
     * @return
     */
    boolean canRead(Class<?> clazz, @Nullable MediaType mediaType);

    /**
     * 该转换器是否能写入指定的类
     * @param clazz
     * @param mediaType
     * @return
     */
    boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType);

    /**
     * 返回该转换器支持的对象列表
     * @return
     */
    List<MediaType> getSupportedMediaTypes(Class<?> clazz);

    /**
     * 从给定的输入消息中读取给定类型的对象，并返回它
     * @param clazz
     * @param inputMessage
     * @return
     * @throws IOException
     * @throws HttpMessageNotReadableException
     */
    T read(Class<? extends T> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException;

    /**
     * 将给定的对象写入给定的输出消息
     * @param t
     * @param contentType
     * @param outputMessage
     * @throws IOException
     * @throws HttpMessageNotWritableException
     */
    void write(T t, @Nullable MediaType contentType, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException;
}
