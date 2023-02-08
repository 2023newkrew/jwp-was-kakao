package logics;

import logics.get.DefaultResponseController;
import utils.FileIoUtils;
import utils.requests.HttpRequest;
import utils.response.HttpResponse;
import utils.response.HttpResponseVersion1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    protected String getAppropriateContentType(HttpRequest httpRequest){
        String fileFormat = extractFileFormat(httpRequest.getURI().getPath());
        if (Objects.nonNull(fileFormat) && Objects.nonNull(defaultContentType.get(fileFormat))){
            return defaultContentType.get(fileFormat)+";charset=utf-8"; // 파일 형식에 따라서 적절한 Content-Type를 지정
        }
        if (Objects.nonNull(httpRequest.getHeaderParameter("Accept"))){ // "파일 형식이 없지만 헤더에 Accept 방식이 있다면
            return httpRequest.getHeaderParameter("Accept").split(";")[0] + ";charset=utf-8";
        }
        return "text/plane;charset=utf-8"; // request에 파일 형식도 없으며, Accept 방식도 없다면 default로 text/plane으로 지정.
    }
    // Controller 등 상위 클래스에 구현해도 될 것 같습니다.
    // Step 2에서 다른 컨트롤러에서도 사용한다면 Controller 상위 클래스에 옮길 수 있습니다.
    protected String urlConverter(String url){
        if (!url.startsWith("/")){
            url = "/" + url;
        }
        if (url.endsWith("html")){
            return "./templates" + url;
        }
        return "./static" + url;
    }


    private String extractFileFormat(String path){
        String[] splitPath = path.split("/");
        if (splitPath.length == 0){
            return null;
        }
        String filename = splitPath[splitPath.length-1];
        String[] splitFilename = filename.split("\\.");
        if (splitFilename.length <= 1){
            return null;
        }
        return splitFilename[splitFilename.length-1];
    }

    protected HttpResponse defaultPathHandling(HttpRequest httpRequest){
        try {
            String modifiedURL = urlConverter(httpRequest.getURI().getPath());
            return new HttpResponseVersion1().setResponseCode(200)
                    .setHeader("Content-Type", getAppropriateContentType(httpRequest))
                    .setBody(FileIoUtils.loadFileFromClasspath(modifiedURL));
        } catch(URISyntaxException | IOException | NullPointerException e){ // URI가 valid하지 않거나, URI가 null이거나 한다면
            e.printStackTrace();
            return new DefaultResponseController().makeResponse(httpRequest);
        }
    }

}
