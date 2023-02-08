package controller;

import enums.ContentType;
import http.HttpResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HomeController {
    private static HomeController instance;

    public static HomeController getInstance() {
        if (instance == null) {
            instance = new HomeController();
        }
        return instance;
    }

    public HttpResponse rootPathGet() {
        return HttpResponse.of(HttpStatus.OK, ContentType.HTML, "Hello world".getBytes());
    }
}
