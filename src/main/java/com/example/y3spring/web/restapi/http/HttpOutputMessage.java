package com.example.y3spring.web.restapi.http;

import com.example.y3spring.web.restapi.http.HttpMessage;

import java.io.IOException;
import java.io.OutputStream;

public interface HttpOutputMessage extends HttpMessage {

    /**
     * 将消息的正文作为输出流进行返回
     * @return
     * @throws IOException
     */
    OutputStream getBody() throws IOException;
}
