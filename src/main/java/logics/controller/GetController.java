package logics.controller;

import logics.controller.get.DefaultPathController;
import logics.controller.support.UrlMatcher;
import logics.controller.get.LoginPageController;
import logics.controller.get.UserListController;
import utils.requests.HttpRequest;
import utils.response.HttpResponse;
import java.util.Objects;

/**
 * GetController makes appropriate response when getting "GET request"
 */
public class GetController implements Controller {
    private final UrlMatcher urlMatcher = new UrlMatcher();
    public GetController(){
        urlMatcher.addMatch("/user/login.html", new LoginPageController());
        urlMatcher.addMatch("/user/list.html", new UserListController());
    }

            //Content-Type: text/html;charset=utf-8
    public HttpResponse makeResponse(HttpRequest httpRequest) {
        Controller matchedController = urlMatcher.getMatchingUrl(httpRequest.getURI().getPath());
        if (Objects.nonNull(matchedController)){
            return matchedController.makeResponse(httpRequest);
        }
        return new DefaultPathController().makeResponse(httpRequest);
    }
}
