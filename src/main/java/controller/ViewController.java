package controller;

import controller.annotation.CustomRequestMapping;
import model.http.*;
import utils.FileIoUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ViewController extends BaseController {

    @Override
    public Method getProperMethod(CustomHttpRequest request) throws NoSuchMethodException {
        return Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(CustomRequestMapping.class)
                        && method.getDeclaredAnnotation(CustomRequestMapping.class).url().equals(request.getUrl())
                        && method.getDeclaredAnnotation(CustomRequestMapping.class).httpMethod().equals(request.getHttpMethod())
                ).findFirst().orElse(this.getClass().getMethod("resource", CustomHttpRequest.class));
    }

    @CustomRequestMapping(url = "/", httpMethod = CustomHttpMethod.GET)
    public CustomHttpResponse main() {
        CustomHttpHeader headers = new CustomHttpHeader();
        headers.put("Content-Type", "text/html;charset=utf-8");
        headers.put("Content-Length", "11");
        return new CustomHttpResponse.Builder()
                .httpStatus(CustomHttpStatus.OK)
                .headers(headers)
                .body("Hello world")
                .build();
    }

    @CustomRequestMapping(url = "", httpMethod = CustomHttpMethod.GET)
    public CustomHttpResponse resource(CustomHttpRequest request) {
        CustomHttpHeader headers = new CustomHttpHeader();
        String url = request.getUrl();
        String[] fileWithExt = url.split("\\.");
        String ext = fileWithExt[fileWithExt.length - 1];
        String filePath = "static";
        if (ext.equals("html")) {
            filePath = "templates";
        }
        headers.put("Content-Type", request.getHeaders().getOrDefault("Accept", "*/*").split(",")[0]);
        try {
            return new CustomHttpResponse.Builder()
                    .httpStatus(CustomHttpStatus.OK)
                    .headers(headers)
                    .body(new String(FileIoUtils.loadFileFromClasspath(filePath + url)))
                    .build();
        } catch (Exception e) {
            return new CustomHttpResponse.Builder()
                    .httpStatus(CustomHttpStatus.OK)
                    .headers(headers)
                    .body("404 NOT FOUND")
                    .build();
        }
    }

    @CustomRequestMapping(url = "/index.html", httpMethod = CustomHttpMethod.GET)
    public CustomHttpResponse index() {
        CustomHttpHeader headers = new CustomHttpHeader();
        headers.put("Content-Type", "text/html;charset=utf-8");
        headers.put("Content-Length", "6902");
        return new CustomHttpResponse.Builder()
                .httpStatus(CustomHttpStatus.OK)
                .headers(headers)
                .body(new String(FileIoUtils.loadFileFromClasspath("templates/index.html")))
                .build();
    }

    @CustomRequestMapping(url = "/css/styles.css", httpMethod = CustomHttpMethod.GET)
    public CustomHttpResponse css() {
        CustomHttpHeader headers = new CustomHttpHeader();
        headers.put("Content-Type", "text/css;charset=utf-8");
        headers.put("Content-Length", "7065");
        return new CustomHttpResponse.Builder()
                .httpStatus(CustomHttpStatus.OK)
                .headers(headers)
                .body(new String(FileIoUtils.loadFileFromClasspath("static/css/styles.css")))
                .build();
    }

}
