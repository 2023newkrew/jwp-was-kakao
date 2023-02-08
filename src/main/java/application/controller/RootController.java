package application.controller;

import org.springframework.http.HttpStatus;
import webserver.handler.controller.AbstractController;
import webserver.http.content.Content;
import webserver.http.content.ContentData;
import webserver.http.content.ContentType;
import webserver.http.request.Request;
import webserver.http.response.Response;

public class RootController extends AbstractController {

    public RootController() {
        addGetHandler("/", this::helloWorld);
    }

    private Response helloWorld(Request request) {
        byte[] data = "Hello world".getBytes();
        ContentData contentData = new ContentData(data);
        Content content = new Content(ContentType.TEXT_HTML, contentData);

        return new Response(HttpStatus.OK, content);
    }
}
