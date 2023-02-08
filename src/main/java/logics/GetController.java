package logics;

import utils.FileIoUtils;
import utils.requests.HttpRequest;
import utils.response.HttpResponse;
import utils.response.HttpResponseVersion1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * GetController makes appropriate response when getting "GET request"
 */
public class GetController extends Controller {
    private final HttpResponse defaultHttpResponse = new HttpResponseVersion1()
            .setResponseCode(200)
            .setHeader("Content-Type", "text/html;charset=utf-8")
            .setBody("Hello world".getBytes(StandardCharsets.UTF_8));
            //Content-Type: text/html;charset=utf-8
    public HttpResponse makeResponse(HttpRequest httpRequest) {
        try {
            String modifiedURL = urlConverter(httpRequest.getURI().getPath());
            return new HttpResponseVersion1().setResponseCode(200)
                    .setHeader("Content-Type", getAppropriateContentType(httpRequest))
                    .setBody(FileIoUtils.loadFileFromClasspath(modifiedURL));
        } catch(URISyntaxException | IOException | NullPointerException e){ // URI가 valid하지 않거나, URI가 null이거나 한다면
            e.printStackTrace();
            return defaultHttpResponse;
        }
    }

    // urlConverter는 지금 당장은 GetController에서밖에 사용하지 않아 GetController 내에 위치해 있지만,
    // Controller 등 상위 클래스에 구현해도 될 것 같습니다.
    // Step 2에서 다른 컨트롤러에서도 사용한다면 Controller 상위 클래스에 옮길 수 있습니다.
    private String urlConverter(String url){
        if (!url.startsWith("/")){
            url = "/" + url;
        }
        if (url.endsWith("html")){
            return "./templates" + url;
        }
        return "./static" + url;
    }

    private String getAppropriateContentType(HttpRequest httpRequest){
        String fileFormat = extractFileFormat(httpRequest.getURI().getPath());
        if (Objects.nonNull(fileFormat) && Objects.nonNull(defaultContentType.get(fileFormat))){
            return defaultContentType.get(fileFormat)+";charset=utf-8"; // 파일 형식에 따라서 적절한 Content-Type를 지정
        }
        if (Objects.nonNull(httpRequest.getHeaderParameter("Accept"))){ // "파일 형식이 없지만 헤더에 Accept 방식이 있다면
            return httpRequest.getHeaderParameter("Accept").split(";")[0] + ";charset=utf-8";
        }
        return "text/plane;charset=utf-8"; // request에 파일 형식도 없으며, Accept 방식도 없다면 default로 text/plane으로 지정.
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
}
