package webserver.controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import utils.FileIoUtils;
import webserver.RequestHandler;
import webserver.request.support.FormData;
import webserver.request.support.QueryParameters;
import webserver.request.Request;

import java.util.HashMap;
import java.util.Map;

public class RequestController {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final PathMapKey RESOURCE_KEY = PathMapKey.of(HttpMethod.GET, "RESOURCE");
    private static final Map<PathMapKey, ControllerMethod> pathMap = new HashMap<>();

    static {
        pathMap.put(RESOURCE_KEY, (req, res) -> {
            // resource 응답
            String rootPath = "./templates";
            if (req.hasStaticPath()){
                rootPath = "./static";
            }
            try {
                res.setBody(FileIoUtils.loadFileFromClasspath(rootPath + req.getPath()));
                res.setStatus(HttpStatus.OK);
            } catch (Exception e) {
                logger.error(e.getMessage());
                res.setBody("404 Not Found - 요청한 페이지를 찾을 수 없습니다.".getBytes());
                res.setStatus(HttpStatus.NOT_FOUND);
            }
        });

        pathMap.put(PathMapKey.of(HttpMethod.GET, "/"), (req, res) -> {
            byte[] body = "Hello world".getBytes();
            res.setBody(body);
            res.setStatus(HttpStatus.OK);
        });

        pathMap.put(PathMapKey.of(HttpMethod.GET, "/user/create"), (req, res) -> {
            QueryParameters parameters = new QueryParameters(req.getURL());
            User user = new User(
                    parameters.getValue("userId"),
                    parameters.getValue("password"),
                    parameters.getValue("name"),
                    parameters.getValue("email")
            );
            DataBase.addUser(user);
            res.setStatus(HttpStatus.OK);
        });

        pathMap.put(PathMapKey.of(HttpMethod.POST, "/user/create"), (req, res) -> {
            FormData formData = new FormData(req.getBody());
            User user = new User(
                    formData.getValue("userId"),
                    formData.getValue("password"),
                    formData.getValue("name"),
                    formData.getValue("email")
            );
            DataBase.addUser(user);
            res.setStatus(HttpStatus.FOUND);
            res.setLocation("http://localhost:8080/index.html");
        });
    }

    public static ControllerMethod getMappedMethod(Request request) {
        ControllerMethod method = pathMap.get(PathMapKey.of(request.getHttpMethod(), request.getPath()));
        if (method == null) {
            method = pathMap.get(RESOURCE_KEY);
        }
        return method;
    }
}
