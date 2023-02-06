package controller;

import enums.ContentType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import webserver.HttpRequest;
import webserver.HttpResponse;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HomeController {

    private static HomeController instance;

    public static HomeController getInstance() {
        if (instance == null) {
            return new HomeController();
        }
        return instance;
    }

    public HttpResponse rootPathGet(HttpRequest request) {
        return HttpResponse.of(HttpStatus.OK, ContentType.HTML, "Hello world".getBytes());
    }
}
