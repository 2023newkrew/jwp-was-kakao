package logics.controller.support;

import java.util.HashMap;
import java.util.Map;

/**
 * ControllerConstants contains several constants used in controller package.
 */
public final class ControllerConstants {
    /**
     * defaultContentType is referred to <a href=https://mimetype.io/all-types/>this</a>.
     */
    public static final Map<String, String> DEFAULT_CONTENT_TYPE = new HashMap<>();
    static{
        // html, cs, js
        DEFAULT_CONTENT_TYPE.put("html", "text/html");
        DEFAULT_CONTENT_TYPE.put("css", "text/css");
        DEFAULT_CONTENT_TYPE.put("js", "text/js");

        // fonts
        DEFAULT_CONTENT_TYPE.put("eot", "application/vnd.ms-fontobject");
        DEFAULT_CONTENT_TYPE.put("svg", "image/svg+xml");
        DEFAULT_CONTENT_TYPE.put("ttf", "application/x-font-ttf");
        DEFAULT_CONTENT_TYPE.put("otf", "application/x-font-opentype");
        DEFAULT_CONTENT_TYPE.put("woff", "application/font-woff");
        DEFAULT_CONTENT_TYPE.put("woff2", "application/font-woff2");
        DEFAULT_CONTENT_TYPE.put("sfnt", "application/font-sfnt");

        // images
        DEFAULT_CONTENT_TYPE.put("gif", "image/gif");
        DEFAULT_CONTENT_TYPE.put("ief", "image/ief");
        DEFAULT_CONTENT_TYPE.put("jpg", "image/jpg");
        DEFAULT_CONTENT_TYPE.put("jpeg", "image/jpeg");
        DEFAULT_CONTENT_TYPE.put("tif", "image/tiff");
        DEFAULT_CONTENT_TYPE.put("tiff", "image/tiff");
        DEFAULT_CONTENT_TYPE.put("ico", "image/x-icon");
        DEFAULT_CONTENT_TYPE.put("cod", "image/cis-cod");
    }
}
