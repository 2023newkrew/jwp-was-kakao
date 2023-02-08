package webserver.domain;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public enum MediaType {
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    IMAGE_GIF("image/gif"),
    IMAGE_X_ICON("image/x-icon"),
    IMAGE_SVG_XML("image/svg+xml"),
    TEXT_PLAIN("text/plain"),
    TEXT_HTML("text/html"),
    TEXT_CSS("text/css"),
    TEXT_JAVASCRIPT("text/javascript"),
    APPLICATION_XML("application/xml"),
    APPLICATION_JSON("application/json"),
    APPLICATION_X_FONT_TTF("application/x-font-ttf"),
    APPLICATION_X_FONT_WOFF("application/x-font-woff"),
    APPLICATION_VND_MS_FONDOBJECT("application/vnd.ms-fontobject"),
    APPLICATION_ZIP("application/zip"),
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
    ;

    private static final Map<String, MediaType> enumReverseMapper = new HashMap<>();

    static {
        Arrays.stream(MediaType.values())
                .forEach(item -> enumReverseMapper.putIfAbsent(item.value(), item));
    }

    private final String mediaType;

    public static MediaType from(String mediaType) {
        return Optional.ofNullable(enumReverseMapper.get(mediaType))
                .orElseThrow(IllegalArgumentException::new);
    }

    public String value() {
        return mediaType;
    }
}
