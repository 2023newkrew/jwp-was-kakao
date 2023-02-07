package logics;

import utils.FileIoUtils;
import utils.requests.HttpRequest;
import utils.requests.RequestMethod;
import utils.response.HttpResponse;
import utils.response.HttpResponseVersion1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Controller class does similar role to Controller component of Spring framework.
 */
public class Controller {
    private final Service service = new Service();

    /**
     * defaultContentType is referred to <a href=https://mimetype.io/all-types/>this</a>.
     */
    private static final Map<String, String> defaultContentType = new HashMap<>();
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

    /**
     * make response when httpRequest is given.
     * @param httpRequest from clients.
     * @return HttpResponse which can directly respond to client.
     * @throws IllegalArgumentException when request contains wrong information such as inappropriate path.
     */
    public HttpResponse makeResponse(HttpRequest httpRequest) {
        try {
            return makeResponseByMethod(httpRequest);
        } catch (IOException e){
            // IOException in Controller is related to file access in resources,
            // which should be distinguished from network IO exception(connection error).
            // Therefore, It should be converted to IllegalArgumentException(given request contains inappropriate path).
            throw new IllegalArgumentException("Request contains inappropriate path.");
        }
    }

    private HttpResponse makeResponseByMethod(HttpRequest httpRequest) throws IOException {
        if (httpRequest.getRequestMethod().equals(RequestMethod.GET)) {
            return respondGET(httpRequest);
        }
        return respondPOST(httpRequest);
    }

    private HttpResponse respondGET(HttpRequest httpRequest) throws IOException {
        try {
            String modifiedURL = urlConverter(httpRequest.getURI().getPath());
            return new HttpResponseVersion1().setResponseCode(200)
                    .setHeader("Content-Type", getAppropriateContentType(httpRequest))
                    .setBody(FileIoUtils.loadFileFromClasspath(modifiedURL));
        } catch(URISyntaxException e){
            return new HttpResponseVersion1().setResponseCode(404);
        }
    }

    private String getAppropriateContentType(HttpRequest httpRequest){
        String fileFormat = getFileFormat(httpRequest.getURI().getPath());
        if (Objects.nonNull(fileFormat) && Objects.nonNull(defaultContentType.get(fileFormat))){
            return defaultContentType.get(fileFormat)+";charset=utf-8";
        }
        if (Objects.nonNull(httpRequest.getHeaderParameter("Accept"))){
            return httpRequest.getHeaderParameter("Accept").split(";")[0] + ";charset=utf-8";
        }
        return "text/plane;charset=utf-8";
    }

    private String getFileFormat(String path){
        String[] splitPath = path.split("/");
        String filename = splitPath[splitPath.length-1];
        String[] splitFilename = filename.split("\\.");

        if (splitFilename.length <= 1){
            return null;
        }
        return splitFilename[splitFilename.length-1];
    }

    private String urlConverter(String url){
        if (!url.startsWith("/")){
            url = "/" + url;
        }
        if (url.endsWith("html")){
            return "./templates" + url;
        }
        return "./static" + url;
    }

    private HttpResponse respondPOST(HttpRequest httpRequest) {
        if (httpRequest.getURI().getPath().equals("/user/create")){
            service.updateUser(httpRequest.getBody());
            return new HttpResponseVersion1().setResponseCode(302)
                    .setHeader("Location", "/index.html");
        }
        return new HttpResponseVersion1().setResponseCode(404);
    }
}
