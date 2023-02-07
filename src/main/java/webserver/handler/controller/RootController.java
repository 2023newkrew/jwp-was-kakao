package webserver.handler.controller;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import webserver.content.Content;
import webserver.content.ContentData;
import webserver.content.ContentType;
import webserver.request.Request;
import webserver.response.Response;

public class RootController extends Controller {

    public RootController() {
        super("");

        methodHandlers.put(HttpMethod.GET, "/", this::helloWorld);
    }

    private Response helloWorld(Request request) {
        byte[] data = "Hello world".getBytes();
        ContentData contentData = new ContentData(data);
        Content content = new Content(ContentType.TEXT_HTML, contentData);

        return new Response(HttpStatus.OK, content);
    }
}
