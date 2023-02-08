package webserver.controller;

import model.MyHttpRequest;
import webserver.Controller;

public class ViewController implements Controller {

    @Override
    public String process(MyHttpRequest httpRequest) {
        return "./templates" + httpRequest.getUrl();
    }

    @Override
    public boolean isRedirectRequired() {
        return false;
    }
}
