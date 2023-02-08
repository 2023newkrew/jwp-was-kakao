package webserver;

import model.MyHttpRequest;
import model.MyHttpResponse;

public interface Controller {

    String process(MyHttpRequest httpRequest, MyHttpResponse httpResponse);
}
