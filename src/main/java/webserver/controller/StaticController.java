package webserver.controller;

import model.MyHttpRequest;
import webserver.Controller;

public class StaticController implements Controller {

    @Override
    public String process(MyHttpRequest httpRequest) {
        return "./static" + httpRequest.getUrl();
    }

    @Override
    public boolean isRedirectRequired() {
        return false;
    }
}
