package com.example.y3spring.web.restapi;


import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;
import java.text.DecimalFormatSymbols;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

public class HttpHeaders implements MultiValueMap<String, String>, Serializable {

    private static final long serialVersionUID = -8578554704772377436L;

    public static final HttpHeaders EMPTY = new HttpHeaders(new LinkedHashMap<>(), true);

    public static final String ACCEPT = "Accept";

    public static final String ACCEPT_CHARSET = "Accept-Charset";

    public static final String ACCEPT_ENCODING = "Accept-Encoding";

    public static final String ACCEPT_LANGUAGE = "Accept-Language";

    public static final String ACCEPT_RANGES = "Accept-Ranges";

    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";

    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";

    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";

    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

    public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";

    public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";

    public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";

    public static final String AGE = "Age";

    public static final String ALLOW = "Allow";

    public static final String AUTHORIZATION = "Authorization";

    public static final String CACHE_CONTROL = "Cache-Control";

    public static final String CONNECTION = "Connection";

    public static final String CONTENT_ENCODING = "Content-Encoding";

    public static final String CONTENT_DISPOSITION = "Content-Disposition";

    public static final String CONTENT_LANGUAGE = "Content-Language";

    public static final String CONTENT_LENGTH = "Content-Length";

    public static final String CONTENT_LOCATION = "Content-Location";

    public static final String CONTENT_RANGE = "Content-Range";

    public static final String CONTENT_TYPE = "Content-Type";

    public static final String COOKIE = "Cookie";

    public static final String DATE = "Date";

    public static final String ETAG = "ETag";

    public static final String EXPECT = "Expect";

    public static final String EXPIRES = "Expires";

    public static final String FROM = "From";

    public static final String HOST = "Host";

    public static final String IF_MATCH = "If-Match";

    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";

    public static final String IF_NONE_MATCH = "If-None-Match";

    public static final String IF_RANGE = "If-Range";

    public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";

    public static final String LAST_MODIFIED = "Last-Modified";

    public static final String LINK = "Link";

    public static final String LOCATION = "Location";

    public static final String MAX_FORWARDS = "Max-Forwards";

    public static final String ORIGIN = "Origin";

    public static final String PRAGMA = "Pragma";

    public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";

    public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";

    public static final String RANGE = "Range";

    public static final String REFERER = "Referer";

    public static final String RETRY_AFTER = "Retry-After";

    public static final String SERVER = "Server";

    public static final String SET_COOKIE = "Set-Cookie";

    public static final String SET_COOKIE2 = "Set-Cookie2";

    public static final String TE = "TE";

    public static final String TRAILER = "Trailer";

    public static final String TRANSFER_ENCODING = "Transfer-Encoding";

    public static final String UPGRADE = "Upgrade";

    public static final String USER_AGENT = "User-Agent";

    public static final String VARY = "Vary";

    public static final String VIA = "Via";

    public static final String WARNING = "Warning";

    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";

    private static final Pattern ETAG_HEADER_VALUE_PATTERN = Pattern.compile("\\*|\\s*((W\\/)?(\"[^\"]*\"))\\s*,?");

    private static final DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols(Locale.ENGLISH);

    private static final ZoneId GMT = ZoneId.of("GMT");

    private static final DateTimeFormatter[] DATE_FORMATTERS = new DateTimeFormatter[] {
            DateTimeFormatter.RFC_1123_DATE_TIME,
            DateTimeFormatter.ofPattern("EEEE, dd-MMM-yy HH:mm:ss zz", Locale.US),
            DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy",Locale.US).withZone(GMT)
    };


    private final Map<String, List<String>> headers;

    private final boolean readOnly;


    public HttpHeaders() {
        this(new LinkedCaseInsensitiveMap<>(8, Locale.ENGLISH), false);
    }

    private HttpHeaders(Map<String, List<String>> headers, boolean readOnly) {
        if (readOnly) {
            Map<String, List<String>> map = new LinkedCaseInsensitiveMap<>(headers.size(), Locale.ENGLISH);
            headers.forEach((key, valueList) -> map.put(key, Collections.unmodifiableList(valueList)));
            this.headers = Collections.unmodifiableMap(map);
        }
        else {
            this.headers = headers;
        }
        this.readOnly = readOnly;
    }


    /**
     * @param key
     * @return
     */
    @Override
    public String getFirst(String key) {
        return null;
    }

    @Override
    public void add(String headerName, @Nullable String headerValue) {
        List<String> headerValues = this.headers.computeIfAbsent(headerName, k -> new LinkedList<>());
        headerValues.add(headerValue);
    }

    /**
     * @param key
     * @param values
     */
    @Override
    public void addAll(String key, List<? extends String> values) {

    }

    /**
     * @param values
     */
    @Override
    public void addAll(MultiValueMap<String, String> values) {

    }

    /**
     * @param key
     * @param value
     */
    @Override
    public void set(String key, String value) {

    }

    /**
     * @param values
     */
    @Override
    public void setAll(Map<String, String> values) {

    }

    /**
     * @return
     */
    @Override
    public Map<String, String> toSingleValueMap() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public int size() {
        return 0;
    }

    /**
     * @return
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * @param key key whose presence in this map is to be tested
     * @return
     */
    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    /**
     * @param value value whose presence in this map is to be tested
     * @return
     */
    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    /**
     * @param key the key whose associated value is to be returned
     * @return
     */
    @Override
    public List<String> get(Object key) {
        return null;
    }

    /**
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return
     */
    @Override
    public List<String> put(String key, List<String> value) {
        return null;
    }

    /**
     * @param key key whose mapping is to be removed from the map
     * @return
     */
    @Override
    public List<String> remove(Object key) {
        return null;
    }

    /**
     * @param m mappings to be stored in this map
     */
    @Override
    public void putAll(Map<? extends String, ? extends List<String>> m) {

    }

    /**
     *
     */
    @Override
    public void clear() {

    }

    /**
     * @return
     */
    @Override
    public Set<String> keySet() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public Collection<List<String>> values() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public Set<Entry<String, List<String>>> entrySet() {
        return null;
    }

    public void setContentLength(long contentLength) {
        set(CONTENT_LENGTH, Long.toString(contentLength));
    }

    public long getContentLength() {
        String value = getFirst(CONTENT_LENGTH);
        return (value != null ? Long.parseLong(value) : -1);
    }

    public static HttpHeaders readOnlyHttpHeaders(HttpHeaders headers) {
        Assert.notNull(headers, "HttpHeaders must not be null");
        return (headers.readOnly ? headers : new HttpHeaders(headers, true));
    }

}
