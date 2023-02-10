package logics;

import utils.requests.HttpRequest;
import utils.response.HttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller abstract class contains defaultContentType,
 * and defines what should be implemented in concrete class.
 */
// 인터페이스로 만들고 싶었지만, defualtContentType를 정의하기 위해서는 클래스를 만들 수 밖에 없었습니다.
public abstract class Controller {
    /**
     * defaultContentType is referred to <a href=https://mimetype.io/all-types/>this</a>.
     */
    public static final Map<String, String> defaultContentType = new HashMap<>();
    static{
        // html, cs, js
        defaultContentType.put("html", "text/html");
        defaultContentType.put("css", "text/css");
        defaultContentType.put("js", "text/js");

        // fonts
        defaultContentType.put("eot", "application/vnd.ms-fontobject");
        defaultContentType.put("svg", "image/svg+xml");
        defaultContentType.put("ttf", "application/x-font-ttf");
        defaultContentType.put("otf", "application/x-font-opentype");
        defaultContentType.put("woff", "application/font-woff");
        defaultContentType.put("woff2", "application/font-woff2");
        defaultContentType.put("sfnt", "application/font-sfnt");

        // images
        defaultContentType.put("gif", "image/gif");
        defaultContentType.put("ief", "image/ief");
        defaultContentType.put("jpg", "image/jpg");
        defaultContentType.put("jpeg", "image/jpeg");
        defaultContentType.put("tif", "image/tiff");
        defaultContentType.put("tiff", "image/tiff");
        defaultContentType.put("ico", "image/x-icon");
        defaultContentType.put("cod", "image/cis-cod");
    }

    public abstract HttpResponse makeResponse(HttpRequest httpRequest);
}
