package webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import utils.FileIoUtils;
import webserver.RequestHandler;
import webserver.controller.annotation.Handler;
import webserver.controller.support.PathMapKey;
import webserver.request.Request;
import webserver.response.Response;

public class ResourceController {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    @Handler(method = HttpMethod.GET, value = PathMapKey.RESOURCE_PATH)
    public void getResource(Request req, Response res) {
        // resource 응답
        String rootPath = "./templates";
        if (req.hasStaticPath()){
            rootPath = "./static";
        }
        setResource(rootPath + req.getPath(), res);
    }

    private void setResource(String path, Response res) {
        try {
            res.setBody(FileIoUtils.loadFileFromClasspath(path));
            res.setStatus(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            res.setBody("404 Not Found - 요청한 페이지를 찾을 수 없습니다.".getBytes());
            res.setStatus(HttpStatus.NOT_FOUND);
        }
    }
}
