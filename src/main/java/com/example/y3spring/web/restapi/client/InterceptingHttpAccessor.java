package com.example.y3spring.web.restapi.client;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class InterceptingHttpAccessor extends HttpAccessor{
    // 装载需要作用在RestTemplate上的拦截器
    private final List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

    @Nullable
    private volatile ClientHttpRequestFactory interceptingRequestFactory;

    public List<ClientHttpRequestInterceptor> getInterceptors() {
        return this.interceptors;
    }

    // 这里语意是set，所以是完全的替换掉（支持ordered排序）
    public void setInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
        if (this.interceptors != interceptors) {
            this.interceptors.clear();
            this.interceptors.addAll(interceptors);
            AnnotationAwareOrderComparator.sort(this.interceptors);
        }
    }

    @Override
    public void setRequestFactory(ClientHttpRequestFactory requestFactory) {
        super.setRequestFactory(requestFactory);
        this.interceptingRequestFactory = null;
    }

    // 若配置了拦截器，那么默认就使用InterceptingClientHttpRequestFactory,而不再是SimpleClientHttpRequestFactory了
    @Override
    public ClientHttpRequestFactory getRequestFactory() {
        List<ClientHttpRequestInterceptor> interceptors = getInterceptors();
        if (!CollectionUtils.isEmpty(interceptors)) {
            ClientHttpRequestFactory factory = this.interceptingRequestFactory;
            if (factory == null) {
                factory = new InterceptingClientHttpRequestFactory(super.getRequestFactory(), interceptors);
                this.interceptingRequestFactory = factory;
            }
            return factory;
        } else {
            return super.getRequestFactory();
        }
    }
}
