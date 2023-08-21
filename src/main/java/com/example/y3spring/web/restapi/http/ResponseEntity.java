package com.example.y3spring.web.restapi.http;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.function.Consumer;

public class ResponseEntity<T> extends HttpEntity<T>{

    private final HttpStatusCode status;

    public ResponseEntity(HttpStatusCode status) {
        this(null, null, status);
    }

    public ResponseEntity(@Nullable T body, HttpStatusCode status) {
        this(body, null, status);
    }

    public ResponseEntity(MultiValueMap<String, String> headers, HttpStatusCode status) {
        this(null, headers, status);
    }

    public ResponseEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers, int rawStatus) {
        this(body, headers, HttpStatusCode.valueOf(rawStatus));
    }

    public ResponseEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers, HttpStatusCode statusCode) {
        super(body, headers);
        Assert.notNull(statusCode, "HttpStatusCode must not be null");

        this.status = statusCode;
    }

    public static BodyBuilder status(int status) {
        return new DefaultBuilder(status);
    }

    public static BodyBuilder status(HttpStatus status) {
        Assert.notNull(status, "HttpStatus must not be null");
        return new DefaultBuilder(status);
    }

    public interface HeadersBuilder<B extends HeadersBuilder<B>> {

        B header(String headerName, String... headerValues);

        B headers(@Nullable HttpHeaders headers);

        B headers(Consumer<HttpHeaders> headersConsumer);

        B allow(HttpMethod... allowedMethods);

        B eTag(@Nullable String etag);

        B lastModified(ZonedDateTime lastModified);

        B lastModified(Instant lastModified);

        B lastModified(long lastModified);

        B location(URI location);

        B cacheControl(CacheControl cacheControl);

        B varyBy(String... requestHeaders);

        <T> ResponseEntity<T> build();
    }

    public interface BodyBuilder extends HeadersBuilder<BodyBuilder> {

        BodyBuilder contentLength(long contentLength);

        BodyBuilder contentType(MediaType contentType);

        <T> ResponseEntity<T> body(@Nullable T body);
    }


    private static class DefaultBuilder implements BodyBuilder {

        private final HttpStatusCode statusCode;

        private final HttpHeaders headers = new HttpHeaders();


        public DefaultBuilder(int statusCode) {
            this(HttpStatusCode.valueOf(statusCode));
        }

        public DefaultBuilder(HttpStatusCode statusCode) {
            this.statusCode = statusCode;
        }


        @Override
        public BodyBuilder header(String headerName, String... headerValues) {
            for (String headerValue : headerValues) {
                this.headers.add(headerName, headerValue);
            }
            return this;
        }

        @Override
        public BodyBuilder headers(@Nullable HttpHeaders headers) {
            if (headers != null) {
                this.headers.putAll(headers);
            }
            return this;
        }

        @Override
        public BodyBuilder headers(Consumer<HttpHeaders> headersConsumer) {
            headersConsumer.accept(this.headers);
            return this;
        }

        @Override
        public BodyBuilder allow(HttpMethod... allowedMethods) {
            this.headers.setAllow(new LinkedHashSet<>(Arrays.asList(allowedMethods)));
            return this;
        }

        @Override
        public BodyBuilder contentLength(long contentLength) {
            this.headers.setContentLength(contentLength);
            return this;
        }

        @Override
        public BodyBuilder contentType(MediaType contentType) {
            this.headers.setContentType(contentType);
            return this;
        }

        @Override
        public BodyBuilder eTag(@Nullable String etag) {
            if (etag != null) {
                if (!etag.startsWith("\"") && !etag.startsWith("W/\"")) {
                    etag = "\"" + etag;
                }
                if (!etag.endsWith("\"")) {
                    etag = etag + "\"";
                }
            }
            this.headers.setETag(etag);
            return this;
        }

        @Override
        public BodyBuilder lastModified(ZonedDateTime date) {
            this.headers.setLastModified(date);
            return this;
        }

        @Override
        public BodyBuilder lastModified(Instant date) {
            this.headers.setLastModified(date);
            return this;
        }

        @Override
        public BodyBuilder lastModified(long date) {
            this.headers.setLastModified(date);
            return this;
        }

        @Override
        public BodyBuilder location(URI location) {
            this.headers.setLocation(location);
            return this;
        }

        @Override
        public BodyBuilder cacheControl(CacheControl cacheControl) {
            this.headers.setCacheControl(cacheControl);
            return this;
        }

        @Override
        public BodyBuilder varyBy(String... requestHeaders) {
            this.headers.setVary(Arrays.asList(requestHeaders));
            return this;
        }

        @Override
        public <T> ResponseEntity<T> build() {
            return body(null);
        }

        @Override
        public <T> ResponseEntity<T> body(@Nullable T body) {
            return new ResponseEntity<>(body, this.headers, this.statusCode);
        }
    }

}
