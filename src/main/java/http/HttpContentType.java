package http;

import java.util.HashMap;
import java.util.Map;

public class HttpContentType {
    public static final String TEXT_HTML = "text/html";
    public static final String TEXT_CSS = "text/css";
    public static final String TEXT_JAVASCRIPT = "text/javascript";

    public static final String FONT_WOFF = "font/woff";
    public static final String FONT_WOFF2 = "font/woff2";
    public static final String FONT_TTF = "font/ttf";
    public static final String FONT_EOT = "application/vnd.ms-fontobject";

    public static final String IMAGE_SVG = "image/svg+xml";
    public static final String IMAGE_PNG = "image/png";
    public static final String IMAGE_ICON = "image/vnd.microsoft.icon";

    private static final Map<String, String> extensionContentTypeMap = new HashMap<>();

    static {
        extensionContentTypeMap.put("html", TEXT_HTML);
        extensionContentTypeMap.put("css", TEXT_CSS);
        extensionContentTypeMap.put("js", TEXT_JAVASCRIPT);

        extensionContentTypeMap.put("woff", FONT_WOFF);
        extensionContentTypeMap.put("woff2", FONT_WOFF2);
        extensionContentTypeMap.put("ttf", FONT_TTF);

        extensionContentTypeMap.put("eot", FONT_EOT);

        extensionContentTypeMap.put("svg", IMAGE_SVG);
        extensionContentTypeMap.put("png", IMAGE_PNG);
        extensionContentTypeMap.put("ico", IMAGE_ICON);
    }

    public static String extensionToContentType(String fileExtension) {
        return extensionContentTypeMap.getOrDefault(fileExtension, TEXT_HTML);
    }
}
