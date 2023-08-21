package com.example.y3spring.web.restapi.http;

import org.springframework.lang.Nullable;
import org.springframework.util.*;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MediaType extends MimeType implements Serializable {
    private static final long serialVersionUID = 2069937152339670231L;

    public static final MediaType ALL;

    public static final String ALL_VALUE = "*/*";

    public static final MediaType APPLICATION_ATOM_XML;

    public static final String APPLICATION_ATOM_XML_VALUE = "application/atom+xml";

    public static final MediaType APPLICATION_FORM_URLENCODED;

    public static final String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded";

    public static final MediaType APPLICATION_JSON;

    public static final String APPLICATION_JSON_VALUE = "application/json";

    public static final MediaType APPLICATION_JSON_UTF8;

    public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";

    public static final MediaType APPLICATION_OCTET_STREAM;

    public static final String APPLICATION_OCTET_STREAM_VALUE = "application/octet-stream";

    public static final MediaType APPLICATION_PDF;

    public static final String APPLICATION_PDF_VALUE = "application/pdf";

    public static final MediaType APPLICATION_PROBLEM_JSON;

    public static final String APPLICATION_PROBLEM_JSON_VALUE = "application/problem+json";

    public static final MediaType APPLICATION_PROBLEM_JSON_UTF8;

    public static final String APPLICATION_PROBLEM_JSON_UTF8_VALUE = "application/problem+json;charset=UTF-8";

    public static final MediaType APPLICATION_PROBLEM_XML;

    public static final String APPLICATION_PROBLEM_XML_VALUE = "application/problem+xml";

    public static final MediaType APPLICATION_RSS_XML;

    public static final String APPLICATION_RSS_XML_VALUE = "application/rss+xml";

    public static final MediaType APPLICATION_STREAM_JSON;

    public static final String APPLICATION_STREAM_JSON_VALUE = "application/stream+json";

    public static final MediaType APPLICATION_XHTML_XML;

    public static final String APPLICATION_XHTML_XML_VALUE = "application/xhtml+xml";

    public static final MediaType APPLICATION_XML;

    public static final String APPLICATION_XML_VALUE = "application/xml";

    public static final MediaType IMAGE_GIF;

    public static final String IMAGE_GIF_VALUE = "image/gif";

    public static final MediaType IMAGE_JPEG;

    public static final String IMAGE_JPEG_VALUE = "image/jpeg";

    public static final MediaType IMAGE_PNG;

    public static final String IMAGE_PNG_VALUE = "image/png";

    public static final MediaType MULTIPART_FORM_DATA;

    public static final String MULTIPART_FORM_DATA_VALUE = "multipart/form-data";

    public static final MediaType TEXT_EVENT_STREAM;

    public static final String TEXT_EVENT_STREAM_VALUE = "text/event-stream";

    public static final MediaType TEXT_HTML;

    public static final String TEXT_HTML_VALUE = "text/html";

    public static final MediaType TEXT_MARKDOWN;

    public static final String TEXT_MARKDOWN_VALUE = "text/markdown";

    public static final MediaType TEXT_PLAIN;

    public static final String TEXT_PLAIN_VALUE = "text/plain";

    public static final MediaType TEXT_XML;

    public static final String TEXT_XML_VALUE = "text/xml";

    private static final String PARAM_QUALITY_FACTOR = "q";


    static {
        ALL = valueOf(ALL_VALUE);
        APPLICATION_ATOM_XML = valueOf(APPLICATION_ATOM_XML_VALUE);
        APPLICATION_FORM_URLENCODED = valueOf(APPLICATION_FORM_URLENCODED_VALUE);
        APPLICATION_JSON = valueOf(APPLICATION_JSON_VALUE);
        APPLICATION_JSON_UTF8 = valueOf(APPLICATION_JSON_UTF8_VALUE);
        APPLICATION_OCTET_STREAM = valueOf(APPLICATION_OCTET_STREAM_VALUE);
        APPLICATION_PDF = valueOf(APPLICATION_PDF_VALUE);
        APPLICATION_PROBLEM_JSON = valueOf(APPLICATION_PROBLEM_JSON_VALUE);
        APPLICATION_PROBLEM_JSON_UTF8 = valueOf(APPLICATION_PROBLEM_JSON_UTF8_VALUE);
        APPLICATION_PROBLEM_XML = valueOf(APPLICATION_PROBLEM_XML_VALUE);
        APPLICATION_RSS_XML = valueOf(APPLICATION_RSS_XML_VALUE);
        APPLICATION_STREAM_JSON = valueOf(APPLICATION_STREAM_JSON_VALUE);
        APPLICATION_XHTML_XML = valueOf(APPLICATION_XHTML_XML_VALUE);
        APPLICATION_XML = valueOf(APPLICATION_XML_VALUE);
        IMAGE_GIF = valueOf(IMAGE_GIF_VALUE);
        IMAGE_JPEG = valueOf(IMAGE_JPEG_VALUE);
        IMAGE_PNG = valueOf(IMAGE_PNG_VALUE);
        MULTIPART_FORM_DATA = valueOf(MULTIPART_FORM_DATA_VALUE);
        TEXT_EVENT_STREAM = valueOf(TEXT_EVENT_STREAM_VALUE);
        TEXT_HTML = valueOf(TEXT_HTML_VALUE);
        TEXT_MARKDOWN = valueOf(TEXT_MARKDOWN_VALUE);
        TEXT_PLAIN = valueOf(TEXT_PLAIN_VALUE);
        TEXT_XML = valueOf(TEXT_XML_VALUE);
    }

    public MediaType(String type) {
        super(type);
    }

    public MediaType(String type, String subtype) {
        super(type, subtype, Collections.emptyMap());
    }

    public MediaType(String type, String subtype, Charset charset) {
        super(type, subtype, charset);
    }

    public MediaType(MimeType mimeType) {
        super(mimeType);
        getParameters().forEach(this::checkParameters);
    }



    public static MediaType valueOf(String value) {
        return parseMediaType(value);
    }


    public static MediaType parseMediaType(String mediaType) {
        MimeType type;
        try {
            type = MimeTypeUtils.parseMimeType(mediaType);
        }
        catch (InvalidMimeTypeException ex) {
            throw new RuntimeException();
        }
        try {
            return new MediaType(type);
        }
        catch (IllegalArgumentException ex) {
            throw new RuntimeException();
        }
    }


    public static List<MediaType> parseMediaTypes(@Nullable String mediaTypes) {
        if (!StringUtils.hasLength(mediaTypes)) {
            return Collections.emptyList();
        }
        List<String> tokenizedTypes = MimeTypeUtils.tokenize(mediaTypes);
        List<MediaType> result = new ArrayList<>(tokenizedTypes.size());
        for (String type : tokenizedTypes) {
            if (StringUtils.hasText(type)) {
                result.add(parseMediaType(type));
            }
        }
        return result;
    }

    public static List<MediaType> parseMediaTypes(@Nullable List<String> mediaTypes) {
        if (CollectionUtils.isEmpty(mediaTypes)) {
            return Collections.emptyList();
        }
        else if (mediaTypes.size() == 1) {
            return parseMediaTypes(mediaTypes.get(0));
        }
        else {
            List<MediaType> result = new ArrayList<>(8);
            for (String mediaType : mediaTypes) {
                result.addAll(parseMediaTypes(mediaType));
            }
            return result;
        }
    }

    public static String toString(Collection<MediaType> mediaTypes) {
        return MimeTypeUtils.toString(mediaTypes);
    }
}
