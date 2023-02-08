package application.controller;

import org.springframework.http.HttpStatus;
import webserver.content.Content;
import webserver.content.ContentData;
import webserver.content.ContentType;
import webserver.controller.AbstractController;
import webserver.request.Request;
import webserver.response.Response;

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
