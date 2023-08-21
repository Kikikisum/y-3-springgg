package com.example.y3spring.web.restapi.http.utils;

import org.springframework.lang.Nullable;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractUriTemplateHandler implements UriTemplateHandler{
    @Nullable
    private String baseUrl;

    private final Map<String, Object> defaultUriVariables = new HashMap<>();


    public Map<String, ?> getDefaultUriVariables() {
        return Collections.unmodifiableMap(this.defaultUriVariables);
    }

    @Nullable
    public String getBaseUrl() {
        return this.baseUrl;
    }

    /**
     * @param uriTemplate
     * @param uriVariables
     * @return
     */
    @Override
    public URI expand(String uriTemplate, Map<String, ?> uriVariables) {
        if (!getDefaultUriVariables().isEmpty()) {
            Map<String, Object> map = new HashMap<>();
            map.putAll(getDefaultUriVariables());
            map.putAll(uriVariables);
            uriVariables = map;
        }
        URI url = expandInternal(uriTemplate, uriVariables);
        return insertBaseUrl(url);
    }

    /**
     * @param uriTemplate
     * @param uriVariables
     * @return
     */
    @Override
    public URI expand(String uriTemplate, Object... uriVariables) {
        URI url = expandInternal(uriTemplate, uriVariables);
        return insertBaseUrl(url);
    }

    private URI insertBaseUrl(URI url) {
        try {
            String baseUrl = getBaseUrl();
            if (baseUrl != null && url.getHost() == null) {
                url = new URI(baseUrl + url.toString());
            }
            return url;
        }
        catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Invalid URL after inserting base URL: " + url, ex);
        }
    }

    protected abstract URI expandInternal(String uriTemplate, Map<String, ?> uriVariables);

    protected abstract URI expandInternal(String uriTemplate, Object... uriVariables);
}
