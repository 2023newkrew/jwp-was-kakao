package webserver;

import model.MyHttpRequest;
import model.MyHttpResponse;
import model.MyModelAndView;

public interface Controller {

    MyModelAndView process(MyHttpRequest httpRequest, MyHttpResponse httpResponse);
}
