package application.controller;

import application.enums.ApplicationContentType;
import org.springframework.http.HttpStatus;
import webserver.handler.controller.AbstractController;
import webserver.http.content.ContentData;
import webserver.request.Request;
import webserver.response.Response;
import webserver.response.ResponseBody;
import webserver.response.ResponseHeader;

public class RootController extends AbstractController {

    public RootController() {
        addGetHandler("/", this::helloWorld);
    }

    private Response helloWorld(Request request) {
        ContentData contentData = new ContentData("Hello world");
        ResponseBody responseBody = new ResponseBody(ApplicationContentType.TEXT_HTML, contentData);
        ResponseHeader header = new ResponseHeader(HttpStatus.OK, responseBody);

        return new Response(header, responseBody);
    }
}
