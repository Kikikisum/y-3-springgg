package com.example.y3spring.web.restapi.http.client;

import com.example.y3spring.web.restapi.http.*;
import org.springframework.lang.Nullable;

import java.net.URI;
import java.util.Set;

public interface RestOperations {

    // GET

    /**
     * 对URL执行GET
     * @param url
     * @param responseType
     * @return
     * @param <T>
     * @throws RestClientException
     */
    @Nullable
    <T> T getForObject(URI url, Class<T> responseType) throws RestClientException;

    /**
     * 对URL执行GET，用ResponseEntity来保存
     *
     * @param url
     * @param responseType
     * @param <T>
     * @return
     * @throws RestClientException
     */
    <T> ResponseEntity<T> getForEntity(URI url, Class<T> responseType) throws RestClientException;


    // HEAD

    /**
     * 返回资源的Http的headers
     *
     * @param url
     * @return
     * @throws RestClientException
     */
    HttpHeaders headForHeaders(URI url) throws RestClientException;


    // POST

    /**
     * 把对象粘贴到url来创建新资源
     * @param url
     * @param request
     * @return 新资源的存储位置
     * @throws RestClientException
     */
    @Nullable
    URI postForLocation(URI url, @Nullable Object request) throws RestClientException;

    /**
     * 把对象粘贴到url来创建新资源
     * @param url
     * @param request
     * @param responseType
     * @return 转换后的对象
     * @param <T>
     * @throws RestClientException
     */
    @Nullable
    <T> T postForObject(URI url, @Nullable Object request, Class<T> responseType) throws RestClientException;

    /**
     * 把创建的新资源存储到ResponseEntity中，并返回
     *
     * @param url
     * @param request
     * @param responseType
     * @param <T>
     * @return
     * @throws RestClientException
     */
    <T> ResponseEntity<T> postForEntity(URI url, @Nullable Object request, Class<T> responseType)
            throws RestClientException;


    // PUT

    /**
     *
     * @param url
     * @param request
     * @throws RestClientException
     */
    void put(URI url, @Nullable Object request) throws RestClientException;


    // PATCH

    /**
     * 更新资源
     * @param url
     * @param request
     * @param responseType
     * @return
     * @param <T>
     * @throws RestClientException
     */
    @Nullable
    <T> T patchForObject(URI url, @Nullable Object request, Class<T> responseType)
            throws RestClientException;



    // DELETE

    /**
     * 对url进行删除资源
     * @param url
     * @throws RestClientException
     */
    void delete(URI url) throws RestClientException;


    // OPTIONS

    /**
     * 返回url的allow标头的值
     * @param url
     * @return
     * @throws RestClientException
     */
    Set<HttpMethod> optionsForAllow(URI url) throws RestClientException;


    // exchange

    /**
     * 执行相应的Http方法，对请求实体写入请求的内容
     * @param url
     * @param method
     * @param requestEntity
     * @param responseType
     * @return
     * @param <T>
     * @throws RestClientException
     */
    <T> ResponseEntity<T> exchange(URI url, HttpMethod method, @Nullable HttpEntity<?> requestEntity,
                                              Class<T> responseType) throws RestClientException;

    // General execution


    /**
     * 对url执行Http方法
     * @param url
     * @param method
     * @param requestCallback
     * @param responseExtractor
     * @return
     * @param <T>
     * @throws RestClientException
     */
    @Nullable
    <T> T execute(URI url, HttpMethod method, @Nullable RequestCallback requestCallback,
                  @Nullable ResponseExtractor<T> responseExtractor) throws RestClientException;

}
