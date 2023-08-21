package com.example.y3spring.web.restapi.http.client;

import com.example.y3spring.web.restapi.http.*;
import com.example.y3spring.web.restapi.http.converter.GenericHttpMessageConverter;
import com.example.y3spring.web.restapi.http.converter.HttpMessageConverter;
import com.example.y3spring.web.restapi.http.converter.HttpMessageConverterExtractor;
import com.example.y3spring.web.restapi.http.utils.DefaultUriTemplateHandler;
import com.example.y3spring.web.restapi.http.utils.UriTemplateHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.y3spring.web.restapi.http.client.AbstractClientHttpRequestFactoryWrapper.createRequest;


public class RestTemplate extends InterceptingClientHttpRequest implements RestOperations{


    private final List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

    private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

    private UriTemplateHandler uriTemplateHandler = new DefaultUriTemplateHandler();

    private final ResponseExtractor<HttpHeaders> headersExtractor = new HeadersExtractor();

    protected final Log logger = LogFactory.getLog(getClass());


    protected RestTemplate(ClientHttpRequestFactory requestFactory, List<ClientHttpRequestInterceptor> interceptors, URI uri, HttpMethod method) {
        super(requestFactory, interceptors, uri, method);
    }

    public List<HttpMessageConverter<?>> getMessageConverters() {
        return this.messageConverters;
    }

    @Override
    public <T> T getForObject(URI url, Class<T> responseType) throws RestClientException {
        // 请求回调
        RequestCallback requestCallback = new AcceptHeaderRequestCallback(responseType);

        // 消息提取器

        HttpMessageConverterExtractor<T> responseExtractor =
                new HttpMessageConverterExtractor<>(responseType, getMessageConverters(), logger);
        // 执行
        return execute(url, HttpMethod.GET, requestCallback, responseExtractor);
    }

    private static <T> T nonNull(@Nullable T result) {
        Assert.state(result != null, "No result");
        return result;
    }

    /**
     * @param url
     * @param responseType
     * @param <T>
     * @return
     * @throws RestClientException
     */
    @Override
    public <T> ResponseEntity<T> getForEntity(URI url, Class<T> responseType) throws RestClientException {
        RequestCallback requestCallback = new AcceptHeaderRequestCallback(responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return nonNull(execute(url, HttpMethod.GET, requestCallback, responseExtractor));
    }

    public <T> ResponseExtractor<ResponseEntity<T>> responseEntityExtractor(Type responseType) {
        return new ResponseEntityResponseExtractor<>(responseType);
    }

    /**
     * @param url
     * @return
     * @throws RestClientException
     */
    @Override
    public HttpHeaders headForHeaders(URI url) throws RestClientException {
        return nonNull(execute(url, HttpMethod.HEAD, null, headersExtractor()));
    }

    /**
     * @param url
     * @param request
     * @return
     * @throws RestClientException
     */
    @Override
    public URI postForLocation(URI url, Object request) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request);
        HttpHeaders headers = execute(url, HttpMethod.POST, requestCallback, headersExtractor());
        return (headers != null ? headers.getLocation() : null);
    }

    /**
     * @param url
     * @param request
     * @param responseType
     * @param <T>
     * @return
     * @throws RestClientException
     */
    @Override
    public <T> T postForObject(URI url, Object request, Class<T> responseType) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request, responseType);
        HttpMessageConverterExtractor<T> responseExtractor =
                new HttpMessageConverterExtractor<>(responseType, getMessageConverters());
        return execute(url, HttpMethod.POST, requestCallback, responseExtractor);
    }

    /**
     * @param url
     * @param request
     * @param responseType
     * @param <T>
     * @return
     * @throws RestClientException
     */
    @Override
    public <T> ResponseEntity<T> postForEntity(URI url, Object request, Class<T> responseType) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return nonNull(execute(url, HttpMethod.POST, requestCallback, responseExtractor));
    }

    /**
     * @param url
     * @param request
     * @throws RestClientException
     */
    @Override
    public void put(URI url, Object request) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request);
        execute(url, HttpMethod.PUT, requestCallback, null);
    }

    /**
     * @param url
     * @param request
     * @param responseType
     * @param <T>
     * @return
     * @throws RestClientException
     */
    @Override
    public <T> T patchForObject(URI url, Object request, Class<T> responseType) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request, responseType);
        HttpMessageConverterExtractor<T> responseExtractor =
                new HttpMessageConverterExtractor<>(responseType, getMessageConverters());
        return execute(url, HttpMethod.PATCH, requestCallback, responseExtractor);
    }

    /**
     * @param url
     * @throws RestClientException
     */
    @Override
    public void delete(URI url) throws RestClientException {
        execute(url, HttpMethod.DELETE, null, null);
    }

    /**
     * @param url
     * @return
     * @throws RestClientException
     */
    @Override
    public Set<HttpMethod> optionsForAllow(URI url) throws RestClientException {
        ResponseExtractor<HttpHeaders> headersExtractor = headersExtractor();
        HttpHeaders headers = execute(url, HttpMethod.OPTIONS, null, headersExtractor);
        return (headers != null ? headers.getAllow() : Collections.emptySet());
    }

    /**
     * @param url
     * @param method
     * @param requestEntity
     * @param responseType
     * @param <T>
     * @return
     * @throws RestClientException
     */
    @Override
    public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        return nonNull(execute(url, method, requestCallback, responseExtractor));
    }

    /**
     * @param url
     * @param method
     * @param requestCallback
     * @param responseExtractor
     * @param <T>
     * @return
     * @throws RestClientException
     */
    @Override
    public <T> T execute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
        return doExecute(url, null, method, requestCallback, responseExtractor);
    }



    protected <T> T doExecute(URI url, @Nullable HttpMethod method, @Nullable RequestCallback requestCallback,
                              @Nullable ResponseExtractor<T> responseExtractor) throws RestClientException {

        return doExecute(url, null, method, requestCallback, responseExtractor);
    }

    protected <T> T doExecute(URI url, @Nullable String uriTemplate, @Nullable HttpMethod method, @Nullable RequestCallback requestCallback,
                              @Nullable ResponseExtractor<T> responseExtractor) throws RestClientException {

        Assert.notNull(url, "URI is required");
        Assert.notNull(method, "HttpMethod is required");
        ClientHttpResponse response = null;
        try {
            ClientHttpRequest request = createRequest(url, method);
            if (requestCallback != null) {
                requestCallback.doWithRequest(request);
            }
            response = request.execute();
            handleResponse(url, method, response);
            return (responseExtractor != null ? responseExtractor.extractData(response) : null);
        }
        catch (IOException ex) {
            throw new RuntimeException();
        }
        finally {
            if (response != null) {
                response.close();
            }
        }
    }

    protected void handleResponse(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        ResponseErrorHandler errorHandler = getErrorHandler();
        boolean hasError = errorHandler.hasError(response);
        if (logger.isDebugEnabled()) {
            try {
                logger.debug(method.name() + " request for \"" + url + "\" resulted in " +
                        response.getRawStatusCode() + " (" + response.getStatusText() + ")" +
                        (hasError ? "; invoking error handler" : ""));
            }
            catch (IOException ex) {
                // ignore
            }
        }
        if (hasError) {
            errorHandler.handleError(url, method, response);
        }
    }

    public ResponseErrorHandler getErrorHandler() {
        return this.errorHandler;
    }


    private class AcceptHeaderRequestCallback implements RequestCallback {

        @Nullable
        private final Type responseType;

        public AcceptHeaderRequestCallback(@Nullable Type responseType) {
            this.responseType = responseType;
        }

        @Override
        public void doWithRequest(ClientHttpRequest request) throws IOException {
            if (this.responseType != null) {
                List<MediaType> allSupportedMediaTypes = getMessageConverters().stream()
                        .filter(converter -> canReadResponse(this.responseType, converter))
                        .flatMap((HttpMessageConverter<?> converter) -> getSupportedMediaTypes(this.responseType, converter))
                        .distinct()
                        .collect(Collectors.toList());
                MimeTypeUtils.sortBySpecificity(allSupportedMediaTypes);
                if (logger.isDebugEnabled()) {
                    logger.debug("Accept=" + allSupportedMediaTypes);
                }
                request.getHeaders().setAccept(allSupportedMediaTypes);
            }
        }

        private boolean canReadResponse(Type responseType, HttpMessageConverter<?> converter) {
            Class<?> responseClass = (responseType instanceof Class<?> clazz ? clazz : null);
            if (responseClass != null) {
                return converter.canRead(responseClass, null);
            }
            return false;
        }

        private Stream<MediaType> getSupportedMediaTypes(Type type, HttpMessageConverter<?> converter) {
            Type rawType = (type instanceof ParameterizedType parameterizedType ? parameterizedType.getRawType() : type);
            Class<?> clazz = (rawType instanceof Class<?> rawClass ? rawClass : null);
            return (clazz != null ? converter.getSupportedMediaTypes(clazz) : converter.getSupportedMediaTypes(clazz))
                    .stream()
                    .map(mediaType -> {
                        if (mediaType.getCharset() != null) {
                            return new MediaType(mediaType.getType(), mediaType.getSubtype());
                        }
                        return mediaType;
                    });
        }
    }

    private static class HeadersExtractor implements ResponseExtractor<HttpHeaders> {

        @Override
        public HttpHeaders extractData(ClientHttpResponse response) {
            return response.getHeaders();
        }
    }

    private class ResponseEntityResponseExtractor<T> implements ResponseExtractor<ResponseEntity<T>> {

        @Nullable
        private final HttpMessageConverterExtractor<T> delegate;

        public ResponseEntityResponseExtractor(@Nullable Type responseType) {
            if (responseType != null && Void.class != responseType) {
                this.delegate = new HttpMessageConverterExtractor<>(responseType, getMessageConverters(), logger);
            }
            else {
                this.delegate = null;
            }
        }

        @Override
        public ResponseEntity<T> extractData(ClientHttpResponse response) throws IOException {
            if (this.delegate != null) {
                T body = this.delegate.extractData(response);
                return ResponseEntity.status(response.getStatusCode()).headers(response.getHeaders()).body(body);
            }
            else {
                return ResponseEntity.status(response.getStatusCode()).headers(response.getHeaders()).build();
            }
        }
    }

    public <T> RequestCallback httpEntityCallback(@Nullable Object requestBody) {
        return new HttpEntityRequestCallback(requestBody);
    }

    public <T> RequestCallback httpEntityCallback(@Nullable Object requestBody, Type responseType) {
        return new HttpEntityRequestCallback(requestBody, responseType);
    }

    private class HttpEntityRequestCallback extends AcceptHeaderRequestCallback {

        private final HttpEntity<?> requestEntity;

        public HttpEntityRequestCallback(@Nullable Object requestBody) {
            this(requestBody, null);
        }

        public HttpEntityRequestCallback(@Nullable Object requestBody, @Nullable Type responseType) {
            super(responseType);
            if (requestBody instanceof HttpEntity<?> httpEntity) {
                this.requestEntity = httpEntity;
            }
            else if (requestBody != null) {
                this.requestEntity = new HttpEntity<>(requestBody);
            }
            else {
                this.requestEntity = HttpEntity.EMPTY;
            }
        }

        @Override
        @SuppressWarnings({"rawtypes", "unchecked"})
        public void doWithRequest(ClientHttpRequest httpRequest) throws IOException {
            super.doWithRequest(httpRequest);
            Object requestBody = this.requestEntity.getBody();
            if (requestBody == null) {
                HttpHeaders httpHeaders = httpRequest.getHeaders();
                HttpHeaders requestHeaders = this.requestEntity.getHeaders();
                if (!requestHeaders.isEmpty()) {
                    requestHeaders.forEach((key, values) -> httpHeaders.put(key, new ArrayList<>(values)));
                }
                if (httpHeaders.getContentLength() < 0) {
                    httpHeaders.setContentLength(0L);
                }
            }
            else {
                Class<?> requestBodyClass = requestBody.getClass();
                Type requestBodyType = (this.requestEntity instanceof RequestEntity<?> _requestEntity ?
                        _requestEntity.getType() : requestBodyClass);
                HttpHeaders httpHeaders = httpRequest.getHeaders();
                HttpHeaders requestHeaders = this.requestEntity.getHeaders();
                MediaType requestContentType = requestHeaders.getContentType();
                for (HttpMessageConverter<?> messageConverter : getMessageConverters()) {
                    if (messageConverter instanceof GenericHttpMessageConverter genericConverter) {
                        if (genericConverter.canWrite(requestBodyType, requestBodyClass, requestContentType)) {
                            if (!requestHeaders.isEmpty()) {
                                requestHeaders.forEach((key, values) -> httpHeaders.put(key, new ArrayList<>(values)));
                            }
                            logBody(requestBody, requestContentType, genericConverter);
                            genericConverter.write(requestBody, requestBodyType, requestContentType, httpRequest);
                            return;
                        }
                    }
                    else if (messageConverter.canWrite(requestBodyClass, requestContentType)) {
                        if (!requestHeaders.isEmpty()) {
                            requestHeaders.forEach((key, values) -> httpHeaders.put(key, new ArrayList<>(values)));
                        }
                        logBody(requestBody, requestContentType, messageConverter);
                        ((HttpMessageConverter<Object>) messageConverter).write(
                                requestBody, requestContentType, httpRequest);
                        return;
                    }
                }
                String message = "No HttpMessageConverter for " + requestBodyClass.getName();
                if (requestContentType != null) {
                    message += " and content type \"" + requestContentType + "\"";
                }
                throw new RestClientException(message);
            }
        }

        private void logBody(Object body, @Nullable MediaType mediaType, HttpMessageConverter<?> converter) {
            if (logger.isDebugEnabled()) {
                if (mediaType != null) {
                    logger.debug("Writing [" + body + "] as \"" + mediaType + "\"");
                }
                else {
                    logger.debug("Writing [" + body + "] with " + converter.getClass().getName());
                }
            }
        }
    }

    protected ResponseExtractor<HttpHeaders> headersExtractor() {
        return this.headersExtractor;
    }


}
