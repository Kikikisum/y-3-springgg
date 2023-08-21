package com.example.y3spring.web.restapi.http.utils;

import java.net.URI;
import java.util.Map;

public class DefaultUriTemplateHandler extends AbstractUriTemplateHandler{

    private boolean parsePath;

    private boolean strictEncoding;


    /**
     * @param uriTemplate
     * @param uriVariables
     * @return
     */
    @Override
    protected URI expandInternal(String uriTemplate, Map<String, ?> uriVariables) {
        return null;
    }

    /**
     * @param uriTemplate
     * @param uriVariables
     * @return
     */
    @Override
    protected URI expandInternal(String uriTemplate, Object... uriVariables) {
        return null;
    }
}
