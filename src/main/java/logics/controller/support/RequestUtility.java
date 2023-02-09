package logics.controller.support;

import utils.requests.HttpRequest;

import java.util.Objects;

import static logics.controller.support.Constants.*;

public class RequestUtility {
    public static String getAppropriateContentType(HttpRequest httpRequest){
        String fileFormat = extractFileFormat(httpRequest.getURI().getPath());
        if (Objects.nonNull(fileFormat) && Objects.nonNull(DEFAULT_CONTENT_TYPE.get(fileFormat))){
            return DEFAULT_CONTENT_TYPE.get(fileFormat)+";charset=utf-8"; // 파일 형식에 따라서 적절한 Content-Type를 지정
        }
        if (Objects.nonNull(httpRequest.getHeaderParameter("Accept"))){ // "파일 형식이 없지만 헤더에 Accept 방식이 있다면
            return httpRequest.getHeaderParameter("Accept").split(";")[0] + ";charset=utf-8";
        }
        return "text/plane;charset=utf-8"; // request에 파일 형식도 없으며, Accept 방식도 없다면 default로 text/plane으로 지정.
    }

    public static String urlConverter(String url){
        if (!url.startsWith("/")){
            url = "/" + url;
        }
        if (url.endsWith("html")){
            return "./templates" + url;
        }
        return "./static" + url;
    }


    private static String extractFileFormat(String path){
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
}
