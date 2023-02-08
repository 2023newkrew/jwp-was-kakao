package webserver.controller;

import model.MyHttpRequest;
import model.MyHttpResponse;
import webserver.Controller;

public class StaticController implements Controller {

    @Override
    public String process(MyHttpRequest httpRequest, MyHttpResponse httpResponse) {
        httpResponse.setContentType(httpRequest.getContentType());
        return "./static" + httpRequest.getUrl();
    }
}
