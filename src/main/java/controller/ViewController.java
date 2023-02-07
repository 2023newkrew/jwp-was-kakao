package controller;

import controller.annotation.CustomRequestMapping;
import model.http.CustomHttpHeader;
import model.http.CustomHttpMethod;
import model.http.CustomHttpResponse;
import model.http.CustomHttpStatus;
import utils.FileIoUtils;

public class ViewController extends BaseController {

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
