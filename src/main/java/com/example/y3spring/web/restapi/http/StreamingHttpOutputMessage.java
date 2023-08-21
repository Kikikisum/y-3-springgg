package com.example.y3spring.web.restapi.http;

import com.example.y3spring.web.restapi.http.HttpOutputMessage;

import java.io.IOException;
import java.io.OutputStream;

public interface StreamingHttpOutputMessage extends HttpOutputMessage {

    void setBody(Body body);

    @FunctionalInterface
    interface Body {

        void writeTo(OutputStream outputStream) throws IOException;
    }
}
