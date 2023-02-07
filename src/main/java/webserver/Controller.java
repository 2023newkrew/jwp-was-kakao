package webserver;

import model.HttpRequest;

public interface Controller {

    String process(HttpRequest httpRequest);

    boolean isRedirectRequired();
}
