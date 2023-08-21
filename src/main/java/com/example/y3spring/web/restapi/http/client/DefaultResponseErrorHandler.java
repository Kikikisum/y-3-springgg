package com.example.y3spring.web.restapi.http.client;

import com.example.y3spring.web.restapi.http.HttpStatus;

import java.io.IOException;

public class DefaultResponseErrorHandler implements ResponseErrorHandler{

    /**
     * @param response
     * @return
     * @throws IOException
     */
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = HttpStatus.resolve(response.getRawStatusCode());
        return (statusCode != null && (statusCode.series() == HttpStatus.Series.CLIENT_ERROR ||
                statusCode.series() == HttpStatus.Series.SERVER_ERROR));
    }

    /**
     * @param response
     * @throws IOException
     */
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = HttpStatus.resolve(response.getRawStatusCode());
        if (statusCode == null) {
            throw new RuntimeException("code为空！");
        }
        // 应该要对不同类型的错误进行分开处理（未完成）
        throw new RuntimeException("需要对错误码进行处理!");
    }
}
