package webserver;

import model.MyHttpRequest;
import model.MyHttpResponse;

public interface MyFilter {

    public void doChain(MyHttpRequest httpRequest, MyHttpResponse httpResponse, MyFilterChain chain);
}
