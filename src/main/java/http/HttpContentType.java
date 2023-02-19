package http;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum HttpContentType {
    TEXT_HTML("text/html", List.of("html")),
    TEXT_CSS("text/css", List.of("css")),
    TEXT_JAVASCRIPT("text/javascript", List.of("js")),

    FONT_WOFF("font/woff", List.of("woff")),
    FONT_WOFF2("font/woff2", List.of("woff2")),
    FONT_TTF("font/ttf", List.of("ttf")),
    FONT_EOT("application/vnd.ms-fontobject", List.of("eot")),

    IMAGE_SVG("image/svg+xml", List.of("svg")),
    IMAGE_PNG("image/png", List.of("png")),
    IMAGE_ICON("image/vnd.microsoft.icon", List.of("ico")),

    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded", List.of());

    private static final Map<String, HttpContentType> extensionMapping = new HashMap<>();

    private final String value;
    private final List<String> extensions;

    static {
        Arrays.stream(values())
                .forEach(httpContentType ->
                    httpContentType.extensions.forEach(extension -> extensionMapping.put(extension, httpContentType)));
    }

    HttpContentType(String value, List<String> extension) {
        this.value = value;
        this.extensions = extension;
    }

    public String getValue() {
        return value;
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public static HttpContentType fromExtension(String extension) {
        return extensionMapping.getOrDefault(extension, TEXT_HTML);
    }

    public static String fromExtensionAndCharset(String extension, Charset charset) {
        return extensionMapping.getOrDefault(extension, TEXT_HTML).value +
                ";charset=" +
                charset.name().toLowerCase();
    }
}
