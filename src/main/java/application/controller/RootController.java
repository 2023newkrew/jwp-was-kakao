package application.controller;

import application.enums.ApplicationContentType;
import org.springframework.http.HttpStatus;
import webserver.handler.controller.AbstractController;
import webserver.http.Content;
import webserver.http.ContentData;
import webserver.request.Request;
import webserver.response.Response;

public class RootController extends AbstractController {

    public RootController() {
        addGetHandler("/", this::helloWorld);
    }

    private Response helloWorld(Request request) {
        byte[] data = "Hello world".getBytes();
        ContentData contentData = new ContentData(data);
        Content content = new Content(ApplicationContentType.TEXT_HTML, contentData);

        return new Response(HttpStatus.OK, content);
    }
}
