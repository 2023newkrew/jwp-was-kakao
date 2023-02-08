package webserver;

import model.MyHttpRequest;

public interface Controller {

    String process(MyHttpRequest httpRequest);

    boolean isRedirectRequired();
}
