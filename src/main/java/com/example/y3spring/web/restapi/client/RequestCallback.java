package com.example.y3spring.web.restapi.client;

import java.io.IOException;

public interface RequestCallback {

    void doWithRequest(ClientHttpRequest request) throws IOException;
}
