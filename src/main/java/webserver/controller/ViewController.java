package webserver.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.MyHttpRequest;
import model.MyHttpResponse;
import model.MyModelAndView;
import webserver.Controller;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ViewController implements Controller {

    private static final ViewController INSTANCE = new ViewController();

    public static ViewController getInstance() {
        return INSTANCE;
    }

    @Override
    public MyModelAndView process(MyHttpRequest httpRequest, MyHttpResponse httpResponse) {
        httpResponse.setContentType(httpRequest.getContentType());
        return new MyModelAndView("./templates" + httpRequest.getUrl());
    }
}
