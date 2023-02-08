package webserver.controller;

import model.MyHttpRequest;
import model.MyHttpResponse;
import webserver.Controller;

public class ViewController implements Controller {

    @Override
    public String process(MyHttpRequest httpRequest, MyHttpResponse httpResponse) {
        httpResponse.setContentType(httpRequest.getContentType());
        return "./templates" + httpRequest.getUrl();
    }
}
