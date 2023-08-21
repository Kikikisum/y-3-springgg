package com.example.y3spring.web.restapi.http.utils;

import java.net.URI;
import java.util.Map;

/**
 * 对url进行路径的替换
 */
public interface UriTemplateHandler {


    /**
     * 对url进行路径的参数替换
     * @param uriTemplate
     * @param uriVariables
     * @return
     */
    URI expand(String uriTemplate, Map<String, ?> uriVariables);

    URI expand(String uriTemplate, Object... uriVariables);
}
