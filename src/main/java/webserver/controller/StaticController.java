package webserver.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.MyHttpRequest;
import model.MyHttpResponse;
import model.MyModelAndView;
import webserver.Controller;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StaticController implements Controller {

    private static final StaticController INSTANCE = new StaticController();

    public static StaticController getInstance() {
        return INSTANCE;
    }

    @Override
    public MyModelAndView process(MyHttpRequest httpRequest, MyHttpResponse httpResponse) {
        httpResponse.setContentType(httpRequest.getContentType());
        return new MyModelAndView("./static", httpRequest.getUrl());
    }
}
