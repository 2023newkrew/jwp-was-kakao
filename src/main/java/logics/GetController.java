package logics;

import logics.get.LoginPageController;
import logics.get.UserListController;
import utils.requests.HttpRequest;
import utils.response.HttpResponse;
import java.util.Objects;

/**
 * GetController makes appropriate response when getting "GET request"
 */
public class GetController extends Controller {
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
        return defaultPathHandling(httpRequest);
    }
}
