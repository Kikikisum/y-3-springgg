package com.example.y3spring.web.restapi.http;

import java.io.IOException;
import java.io.InputStream;

/**
 * Http输入的消息
 */
public interface HttpInputMessage extends HttpMessage {

    /**
     * 将Http消息的正文作为输入流返回
     * @return
     * @throws IOException
     */
    InputStream getBody() throws IOException;
}
